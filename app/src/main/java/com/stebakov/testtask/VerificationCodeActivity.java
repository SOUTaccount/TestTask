package com.stebakov.testtask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class VerificationCodeActivity extends AppCompatActivity {
    String phoneNum, codeSent;
    PhoneAuthProvider.ForceResendingToken codeResent;
    FirebaseAuth mAuth;
    EditText edtCode;
    Button btnEnter , btnResend;
    String LOG_TAG = "MyLog";
    TextView tvTimer;
    private boolean running = true;
    private int seconds = 120;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);
        Intent intent = getIntent();
        phoneNum = intent.getStringExtra("phone");
        mAuth = FirebaseAuth.getInstance();
        sendVerificationCode();
        Log.d(LOG_TAG,"Телефон =  " + phoneNum.toString());
        edtCode = findViewById(R.id.edt_code_login);
        btnEnter = findViewById(R.id.btn_enter_login);
        btnResend = findViewById(R.id.btn_resend);
        tvTimer = findViewById(R.id.tv_timer);
        //runTimer(tvTimer);
       new CountDownTimer(120000, 1000) {

           @SuppressLint("SetTextI18n")
           public void onTick(long millisUntilFinished) {
               tvTimer.setText(String.valueOf(millisUntilFinished / 1000));
               //here you can have your logic to set text to edittext
           }

           @SuppressLint("SetTextI18n")
           public void onFinish() {
               tvTimer.setText("0");
           }

       }.start();
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifySignInCode();
            }
        });
        btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (running){
                    Toast.makeText(VerificationCodeActivity.this, "Подождите", Toast.LENGTH_SHORT).show();
                } else {
                    resendVerificationCode(phoneNum,codeResent);
                    seconds = 120;
                }
            }
        });
    }

    private void sendVerificationCode(){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNum)       // Phone number to verify
                        .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(VerificationCodeActivity.this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void verifySignInCode() {
        String code = edtCode.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(VerificationCodeActivity.this, UserInfoActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(),
                                    "Вы успешно подключены!", Toast.LENGTH_LONG).show();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(),
                                        "Неправильно введен проверочный код!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                120,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallBack,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSent = s;
            codeResent = forceResendingToken;
        }

    };
    public void runTimer(TextView tv){
        final Handler handler=new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int minutes=(seconds%3600)/60;
                int secs = seconds%60;
                String time = String.format(Locale.getDefault(),
                        "%02d:%02d",minutes,secs);
                tv.setText(time);
                if (running){
                    seconds--;
                }
                if (seconds == 0){
                    running = false;
                }
                handler.postDelayed(this,1000);
            }
        });
    }
    public void createTimer(){
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvTimer.setText("seconds remaining: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                tvTimer.setText("done!");
            }

        }.start();
    }
}
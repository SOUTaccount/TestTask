package com.stebakov.testtask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button btnLog;
    EditText edtPhone;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLog = findViewById(R.id.btn_next_login);
        edtPhone = findViewById(R.id.edt_phone_login);
        this.setTitle("Вход по номеру телефона");
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()!=null){
            Intent checkedUserIntent = new Intent(this, UserInfoActivity.class);
            startActivity(checkedUserIntent);
            finish(); // чтобы активности не накладывались друг на друга, из-за этой функции мейн активити завершается
        }
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtPhone.getText().toString().equals("")){
                    char plusInNumber = edtPhone.getText().toString().charAt(0);
                    if (edtPhone.getText().toString().length()==12){
                        if (plusInNumber == '+'){
                            Intent intent = new Intent(MainActivity.this, VerificationCodeActivity.class);
                            intent.putExtra("phone",edtPhone.getText().toString());
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(MainActivity.this,
                                    "Номер телефона должен начинаться с +7",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(MainActivity.this,
                                "Пожалуйста, введите корректный номер телефона, номер телефона должен начинаться с +7",
                                Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(MainActivity.this,
                            "Пожалуйста, введите номер телефона",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
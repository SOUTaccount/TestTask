package com.stebakov.testtask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceListActivity extends AppCompatActivity {
    private LocationManager locationManager;
    String statusGPS, statusNet,enableGPS, enableNet, locationGPS, locationNet;
    NetWorkService netWorkService;
    RecyclerView rvPlaces;
    ArrayList<Places> alPlaces;
    private final String PLACES_LOG_TAG = "PlacesLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);
        rvPlaces = findViewById(R.id.rv_places);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        getPlacesApi();
    }
    private void getPlacesApi(){
        netWorkService.getInstance()
                .getJSONApi()
                .getPlaces()
                .enqueue(new Callback<ArrayList<Places>>()
                {
                    @Override
                    public void onResponse(Call<ArrayList<Places>> call, Response<ArrayList<Places>> response)
                    {
                        if(response.isSuccessful())
                        {
                            Log.d(PLACES_LOG_TAG,"RESPONSE = " + response.body());
                            alPlaces = response.body();
                            PlacesAdapter placesAdapter = new PlacesAdapter(PlaceListActivity.this,alPlaces);
                            rvPlaces.setAdapter(placesAdapter);
                            rvPlaces.setLayoutManager(new LinearLayoutManager(PlaceListActivity.this));

                        } else

                        /**
                         Обработка исключений типа JSONException, IOException
                         */
                        {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast.makeText(PlaceListActivity.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                                Log.d(PLACES_LOG_TAG,"try error responce = " + response.body());
                            } catch (Exception e) {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    Log.d(PLACES_LOG_TAG,"json eror = " + jObjError.getString("message"));
                                } catch (JSONException jsonException) {
                                    jsonException.printStackTrace();
                                    Log.d(PLACES_LOG_TAG,"json exception = " + jsonException.getMessage());
                                } catch (IOException ioException) {
                                    ioException.printStackTrace();
                                    Log.d(PLACES_LOG_TAG,"io exception = " + ioException.getMessage());
                                }
                                Toast.makeText(PlaceListActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                Log.d(PLACES_LOG_TAG,"catch error responce = " + e.getMessage());
                                Log.d(PLACES_LOG_TAG,"error_body = " + response.errorBody());
                                Log.d(PLACES_LOG_TAG,"responce_size = " + response.body());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Places>> call, Throwable t)
                    {
                        Log.d(PLACES_LOG_TAG,"fail");
                        Toast toast = Toast.makeText(PlaceListActivity.this,
                                "Проверьте ваше интернет соединение", Toast.LENGTH_LONG);
                        toast.show();
                        t.printStackTrace();
                    }
                });
    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            checkEnabled();
        }

        @Override
        public void onProviderEnabled(String provider) {
            checkEnabled();
            if (ActivityCompat.checkSelfPermission(PlaceListActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PlaceListActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            showLocation(locationManager.getLastKnownLocation(provider));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (provider.equals(LocationManager.GPS_PROVIDER)) {
                statusGPS = ("Status: " + String.valueOf(status));
            } else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
                statusNet = ("Status: " + String.valueOf(status));
            }
        }
    };

    private void showLocation(Location location) {
        if (location == null)
            return;
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            locationGPS = (formatLocation(location));
        } else if (location.getProvider().equals(
                LocationManager.NETWORK_PROVIDER)) {
            locationNet = (formatLocation(location));
        }
    }

    @SuppressLint("DefaultLocale")
    private String formatLocation(Location location) {
        if (location == null)
            return "";
        return String.format(
                "Coordinates: lat = %1$.4f, lon = %2$.4f, time = %3$tF %3$tT",
                location.getLatitude(), location.getLongitude(), new Date(
                        location.getTime()));
    }

    private void checkEnabled() {
        enableGPS = ("Enabled: "
                + locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER));
        enableNet = ("Enabled: "
                + locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }

    public void onClickLocationSettings(View view) {
        startActivity(new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    };

}
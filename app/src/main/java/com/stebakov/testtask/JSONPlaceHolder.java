package com.stebakov.testtask;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface JSONPlaceHolder {
    @Headers("Content-Type: application/json")
    @GET("user")
    Call<ArrayList<User>> getUser();

}

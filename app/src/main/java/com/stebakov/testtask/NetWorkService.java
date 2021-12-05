package com.stebakov.testtask;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetWorkService {
    private static NetWorkService mInstance;
    private static final String BASE_URL = "http://smart.eltex-co.ru:8271/api/v1/";
    private static Retrofit mRetrofit;
    /**
     Инициализируем Retrofit в конструкторе NewWorkService
     */
    private NetWorkService()
    {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    /**
     Создание singleton объекта
     */

    public static NetWorkService getInstance()
    {
        if (mInstance==null)
        {
            mInstance = new NetWorkService();
        }
        // Возвращаем переменную того же типа, что и класс
        return mInstance;
    }
    public JSONPlaceHolder getJSONApi ()
    {
        return mRetrofit.create(JSONPlaceHolder.class);
    }
}



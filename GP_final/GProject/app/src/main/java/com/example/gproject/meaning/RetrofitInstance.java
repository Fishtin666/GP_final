package com.example.gproject.meaning;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static final String BASE_URL = "https://api.dictionaryapi.dev/api/v2/entries/";

    public static Retrofit getInstance() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static final DictionaryApi dictionaryApi = getInstance().create(DictionaryApi.class);
    public static final DictionaryApi dictionaryApi2 = getInstance().create(DictionaryApi.class);
}

package com.example.gproject.dictionary;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static final String BASE_URL = "https://api.dictionaryapi.dev/api/v2/entries/";

    private static Retrofit getInstance() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static final DictionaryApi dictionaryApi = getInstance().create(DictionaryApi.class);
}

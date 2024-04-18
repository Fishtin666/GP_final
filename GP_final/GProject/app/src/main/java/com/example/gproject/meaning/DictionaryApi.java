package com.example.gproject.meaning;

import com.example.gproject.WordCard.WordResult2;
import com.example.gproject.dictionary.WordResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DictionaryApi {
    @GET("en/{word}")
    Call<List<WordResult2>> getMeaning(@Path("word") String word);
}




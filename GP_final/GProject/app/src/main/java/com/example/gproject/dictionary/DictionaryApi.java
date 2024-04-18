package com.example.gproject.dictionary;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import com.example.gproject.dictionary.WordResult;

//public interface DictionaryApi {
//    @GET("en/{word}")
//    Response<List<WordResult>> getMeaning(@Path("word") String word);
//}
//public interface DictionaryApi {
//    @GET("meaning")
//    Call<Response<List<WordResult.Meaning>>> getMeaning(@Query("word") String word);
//
//}

public interface DictionaryApi {

    @GET("en/{word}")
    Call<List<WordResult>> getMeaning(@Path("word") String word);

}
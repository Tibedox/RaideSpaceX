package ru.samsung.raidespacex;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MyApi {
    @GET("/spacex.php")
    Call<List<DataFromBase>> send(@Query("name") String name, @Query("score") int score, @Query("kills") int kills);

    @GET("/spacex.php")
    Call<List<DataFromBase>> send(@Query("q") String word);
}

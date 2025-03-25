package ru.samsung.spaceinvaders;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MyApi {
    @GET("/invaders.php")
    Call<List<DataFromDB>> sendToServer(@Query("q") String s);

    @GET("/invaders.php")
    Call<List<DataFromDB>> sendToServer(@Query("name") String n, @Query("score") int s, @Query("kills") int k);
}

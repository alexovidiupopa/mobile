package ro.alexpopa.games.service;

import java.util.List;


import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ro.alexpopa.games.model.Game;

public interface ApiService {
    String ENDPOINT = "http://192.168.0.106:2502";

    @GET("/allGames")
    Observable<List<Game>> getAllGames();

    @GET("/ready")
    Observable<List<Game>> getAvailableGames();

    @GET("/games/{user}")
    Observable<List<Game>> getGamesForUser(@Path("user") String user);

    @POST("/book")
    Observable<Game> borrowGame(@Body Game game);

    @POST("/game")
    Observable<Game> recordGame(@Body Game game);


}

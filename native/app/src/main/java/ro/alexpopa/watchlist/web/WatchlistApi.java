package ro.alexpopa.watchlist.web;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import ro.alexpopa.watchlist.domain.Movie;

public interface WatchlistApi {
    @GET("/api/watchlist/{id}")
    Call<Movie> getMovieById(@Path("id") int id);

    @GET("/api/watchlist")
    Call<List<Movie>> getAllMovies();

    @POST("/api/watchlist")
    Call<Movie> addMovie(@Body Movie movie);

    @PUT("/api/watchlist")
    Call<Movie> updateMovie(@Body Movie movie);

    @DELETE("/api/watchlist/{id}")
    Call<Movie> deleteMovie(@Path("id") int id);
}

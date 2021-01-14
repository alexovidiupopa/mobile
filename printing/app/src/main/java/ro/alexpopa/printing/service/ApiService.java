package ro.alexpopa.printing.service;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ro.alexpopa.printing.model.Model;

public interface ApiService {
    String ENDPOINT = "http://192.168.0.106:2901";

    @GET("/all")
    Observable<List<Model>> getAllModels();

    @GET("/models/{clientId}")
    Observable<List<Model>> getModelsForClient(@Path("clientId") int client);

    @GET("/filled")
    Observable<List<Model>> getFilledExpenses();

    @POST("/process")
    Observable<Model> updateModel(@Body Model model);

    @POST("/model")
    Observable<Model> recordModel(@Body Model doc);


}

package ro.alexpopa.filescatalog.service;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ro.alexpopa.filescatalog.model.File;

public interface ApiService {
    String ENDPOINT = "http://192.168.0.106:2702";

    @GET("/all")
    Observable<List<File>> getAllModels();

    @GET("/files/{location}")
    Observable<List<File>> getFilesForLocation(@Path("location") String location);

    @DELETE("/file/{id}")
    Observable<File> deleteFile(@Path("id") int id);

    @GET("/locations")
    Observable<List<String>> getAllLocations();


    @POST("/file")
    Observable<File> recordModel(@Body File doc);


}

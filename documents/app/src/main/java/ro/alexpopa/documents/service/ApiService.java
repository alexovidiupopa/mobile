package ro.alexpopa.documents.service;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ro.alexpopa.documents.model.Document;

public interface ApiService {
    String ENDPOINT = "http://192.168.0.106:2701";

    @GET("/all")
    Observable<List<Document>> getAvailableDocuments();

    @GET("/documents/{user}")
    Observable<List<Document>> getDocumentsForUser(@Path("user") String user);

    @DELETE("/document/{id}")
    Observable<Document> deleteDocument(@Path("id") int id);

    @POST("/document")
    Observable<Document> recordDocument(@Body Document doc);


}

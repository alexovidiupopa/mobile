package ro.alexpopa.robots.service;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ro.alexpopa.robots.model.Robot;

public interface ApiService {
    String ENDPOINT = "http://192.168.0.106:2202";

    @GET("/old")
    Observable<List<Robot>> getAllOrders();

    @GET("/types")
    Observable<List<String>> getAllTypes();

    @GET("/pending")
    Observable<List<Robot>> getPendingOrders();

    @GET("/robots/{type}")
    Observable<List<Robot>> getRobotsOfType(@Path("type") String type);

    @POST("/robot")
    Observable<Robot> recordRobot(@Body Robot o);

    @POST("/height")
    Observable<Robot> updateHeight(@Body Robot o);

    @POST("/age")
    Observable<Robot> updateAge(@Body Robot o);

}

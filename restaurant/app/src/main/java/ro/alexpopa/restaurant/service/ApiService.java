package ro.alexpopa.restaurant.service;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ro.alexpopa.restaurant.model.Order;

public interface ApiService {
    String ENDPOINT = "http://192.168.0.106:2301";

    @GET("/orders")
    Observable<List<Order>> getAvailableOrders();

    @GET("/order/{id}")
    Observable<Order> getOrderDetails(@Path("id") int id);


    @GET("/my/{table}")
    Observable<Order> getMyTable(@Path("table") String table);

    @POST("/order")
    Observable<Order> recordOrder(@Body Order o);

    @POST("/status")
    Observable<Order> updateOrder(@Body Order o);

    @GET("/recorded")
    Observable<List<Order>> getRecordedOrders();


}

package ro.alexpopa.onlineshop.service;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ro.alexpopa.onlineshop.model.Order;

public interface ApiService {
    String ENDPOINT = "http://192.168.0.106:2302";

    @GET("/all")
    Observable<List<Order>> getAllOrders();

    @GET("/pending")
    Observable<List<Order>> getPendingOrders();

    @GET("/orders/{id}")
    Observable<List<Order>> getOrdersForClient(@Path("id") int id);

    @POST("/order")
    Observable<Order> recordOrder(@Body Order o);

    @POST("/status")
    Observable<Order> updateOrder(@Body Order o);



}

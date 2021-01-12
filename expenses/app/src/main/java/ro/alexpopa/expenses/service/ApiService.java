package ro.alexpopa.expenses.service;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ro.alexpopa.expenses.model.Expense;

public interface ApiService {
    String ENDPOINT = "http://192.168.0.106:2902";

    @GET("/open")
    Observable<List<Expense>> getAvailableExpenses();

    @GET("/my/{user}")
    Observable<List<Expense>> getExpensesForUser(@Path("user") String user);

    @GET("/filled")
    Observable<List<Expense>> getFilledExpenses();

    @POST("/change")
    Observable<Expense> updateRequest(@Body Expense expense);

    @POST("/request")
    Observable<Expense> recordDocument(@Body Expense doc);


}

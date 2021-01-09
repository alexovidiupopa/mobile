package ro.alexpopa.books.service;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ro.alexpopa.books.model.Book;

public interface ApiService {

    String ENDPOINT = "http://192.168.0.106:2501";

    @GET("/books/{student}")
    Observable<List<Book>> getBooks(@Path("student") String student);

    @POST("/book")
    Observable<Book> addBook(@Body Book b);

    @GET("/available")
    Observable<List<Book>> getAvailable();

    @POST("/borrow")
    Observable<Book> borrowBook(@Body Book b);

    @GET("/all")
    Observable<List<Book>> getAllBooks();
}

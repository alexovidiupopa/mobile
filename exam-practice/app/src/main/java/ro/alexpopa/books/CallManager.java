package ro.alexpopa.books;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import ro.alexpopa.books.adapter.DataAdapter;
import ro.alexpopa.books.model.Book;
import ro.alexpopa.books.service.ApiService;
import ro.alexpopa.books.service.ServiceFactory;
import ro.alexpopa.books.ui.MyCallback;
import timber.log.Timber;

public class CallManager {
    private MainApp main;
    private ApiService apiService;
    public CallManager(Application application){
        this.main = ((MainApp) application);
        apiService = ServiceFactory.createRetrofitService(ApiService.ENDPOINT);
        listen();
    }

    public boolean networkConnectivity(Context context){
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public void listen() {

        new Thread(()->{
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url("ws:localhost:2501").build();
            WebSocketListener listener = new WebSocketListener() {
                @Override
                public void onMessage(WebSocket webSocket, String text) {

                    JsonParser jsonParser = new JsonParser();
                    JsonObject jsonObject = (JsonObject) jsonParser.parse(text);
                    Timber.v(jsonObject.toString());
                    System.out.println(jsonObject);
                }
            };
            WebSocket ws = okHttpClient.newWebSocket(request, listener);

        }).start();

    }
    public void loadAllBooks(final ProgressBar progressBar, final MyCallback callback, String student) {
        Observable<List<Book>> call = apiService.getBooks(student);
                call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Book>>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while loading the books");
                        callback.showError("Not able to retrieve the data. Displaying local data!");
                    }

                    @Override
                    public void onComplete() {
                        Timber.v("Book Service load completed");
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final List<Book> books) {
                        new Thread(() -> {
                            main.db.getBooksDao().deleteBooks();
                            main.db.getBooksDao().addBooks(books);
                        }).start();
                        Timber.v("Books persisted");
                    }
                });

    }

    public void borrow(Book book, MyCallback callback){
        apiService.borrowBook(book)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Book>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while borrowing an book");
                        callback.showError("Error while borrowing a book");
                    }

                    @Override
                    public void onComplete() {
                        Timber.v("Book Service borrow completed");
                        callback.clear();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Book book) {
                        Timber.v("Book borrowed");
                    }
                });
    }

    public void save(Book book, MyCallback callback) {
        apiService.addBook(book)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Book>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while persisting an book");
                        callback.showError("Not able to connect to the server, will not persist!");
                    }

                    @Override
                    public void onComplete() {
                        addDataLocally(book);
                        Timber.v("Book Service completed");
                        callback.clear();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Book book) {
                        Timber.v("Book persisted");
                    }
                });
    }

    private void addDataLocally(final Book book) {
        new Thread(() -> main.db.getBooksDao().addBook(book)).start();
    }

    public void getAvailableBooks(RecyclerView recyclerView, MyCallback callback){
        Observable<List<Book>> call = apiService.getAvailable();
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Book>>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while loading the books");
                        callback.showError("Error while loading the books");
                    }

                    @Override
                    public void onComplete() {
                        Timber.v("Book Service completed");

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final List<Book> books) {
                        List<Book> newBooks = books.stream().sorted(Comparator.comparing(Book::getUsedCount)).collect(Collectors.toList());
                        Collections.reverse(newBooks);
                        DataAdapter adapter = new DataAdapter();
                        adapter.setItems(newBooks.subList(0,10));
                        recyclerView.setAdapter(adapter);
                        Timber.v("Available books displayed");
                    }
                });
    }

    public void getTopTenBooks(RecyclerView recyclerView, MyCallback callback){
        Observable<List<Book>> call = apiService.getAllBooks();
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Book>>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while loading the books");
                        callback.showError("Error while loading the books");
                    }

                    @Override
                    public void onComplete() {
                        Timber.v("Book Service completed");

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final List<Book> books) {

                        List<Book> newBooks = books.stream().sorted(Comparator.comparing(Book::getUsedCount)).collect(Collectors.toList());
                        Collections.reverse(newBooks);

                        DataAdapter adapter = new DataAdapter();
                        adapter.setItems(newBooks.subList(0,10));
                        recyclerView.setAdapter(adapter);
                        Timber.v("Top 10 books displayed");
                    }
                });
    }
}

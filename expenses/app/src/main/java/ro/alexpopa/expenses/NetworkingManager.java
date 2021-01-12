package ro.alexpopa.expenses;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Arrays;
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
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import ro.alexpopa.expenses.adapter.DataAdapter;
import ro.alexpopa.expenses.model.Expense;
import ro.alexpopa.expenses.service.ApiService;
import ro.alexpopa.expenses.service.ServiceFactory;
import ro.alexpopa.expenses.ui.MyCallback;
import timber.log.Timber;

public class NetworkingManager {
    private MainApp main;
    private ApiService apiService;
    public NetworkingManager(Application application){
        this.main = ((MainApp) application);
        apiService = ServiceFactory.createRetrofitService(ApiService.ENDPOINT);
    }
    //TODO don't forget to change the port!
    public static void initializeWebSocket(View view) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url("ws://192.168.0.106:2902").build();
        WebSocketListener listener = new NetworkingManager.WebSocketListenerImpl(view);
        okHttpClient.newWebSocket(request, listener);
        okHttpClient.dispatcher().executorService().shutdown();
    }

    public boolean networkConnectivity(Context context){
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public void loadAllExpenses(ProgressBar progressBar, MyCallback callback, String user) {
        Observable<List<Expense>> call = apiService.getExpensesForUser(user);
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Expense>>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while loading the Expenses");
                        callback.showError("Not able to retrieve the data. Displaying local data!");
                    }

                    @Override
                    public void onComplete() {
                        Timber.v("Expense Service load completed");
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final List<Expense> expenses) {
                        new Thread(() -> {
                            main.db.getExpensesDao().deleteExpenses();
                            main.db.getExpensesDao().addExpenses(expenses);
                        }).start();
                        Timber.v("Expenses persisted");
                    }
                });

    }

    public void updateRequest(ProgressBar progressBar, Expense request, MyCallback callback){
        apiService.updateRequest(request)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Expense>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while updating a request");
                        callback.showError("Error while updating a request");
                    }

                    @Override
                    public void onComplete() {
                        Timber.v("Expense Service update completed");
                        progressBar.setVisibility(View.GONE);
                        callback.clear();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Expense expense) {
                        Timber.v("Expense updated");
                    }
                });
    }

    public void save(ProgressBar progressBar, Expense expense, MyCallback callback) {
        apiService.recordDocument(expense)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Expense>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while persisting an request");
                        callback.showError("Not able to connect to the server, will not persist!");
                    }

                    @Override
                    public void onComplete() {
                        Timber.v("Expense Service completed");
                        progressBar.setVisibility(View.GONE);
                        callback.clear();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDataLocally(expense);
                    }

                    @Override
                    public void onNext(Expense expense) {
                        Timber.v("Expense persisted");
                    }
                });
    }

    private void addDataLocally(Expense expense) {
        new Thread(() -> main.db.getExpensesDao().addDocument(expense)).start();
    }

    public void getAvailableExpenses(ProgressBar progressBar, RecyclerView recyclerView, MyCallback callback){
        Observable<List<Expense>> call = apiService.getAvailableExpenses();
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Expense>>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while loading the Expenses");
                        callback.showError("Error while loading the Expenses");
                    }

                    @Override
                    public void onComplete() {
                        Timber.v("Expense Service completed");
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final List<Expense> expenses) {
                        DataAdapter adapter = new DataAdapter();
                        adapter.setFields(Arrays.asList("name","eCost"));
                        adapter.setItems(expenses);
                        recyclerView.setAdapter(adapter);
                        Timber.v("Open Expenses displayed");
                    }
                });
    }

    public void getTopTenExpenses(ProgressBar progressBar,RecyclerView recyclerView, MyCallback callback){
        Observable<List<Expense>> call = apiService.getFilledExpenses();
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Expense>>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while loading the Expenses");
                        callback.showError("Error while loading the Expenses");
                    }

                    @Override
                    public void onComplete() {
                        Timber.v("Expense Service completed");
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final List<Expense> expenses) {

                        List<Expense> newExpenses = expenses.stream().sorted(Comparator.comparing(Expense::getCost)).collect(Collectors.toList());
                        Collections.reverse(newExpenses);

                        DataAdapter adapter = new DataAdapter();
                        adapter.setFields(Arrays.asList("name","eCost","cost"));
                        adapter.setItems(newExpenses.subList(0,Math.min(10, newExpenses.size())));
                        recyclerView.setAdapter(adapter);
                        Timber.v("Top 10 Expenses displayed");
                    }
                });
    }

    public static class WebSocketListenerImpl extends WebSocketListener {

        private View view;

        public WebSocketListenerImpl(View view) {
            this.view = view;
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParser.parse(text);
            Timber.v(jsonObject.toString());
            String message = "Someone added: " + jsonObject.get("name") + "," + jsonObject.get("eCost") + "," + jsonObject.get("student") + "!";
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        }

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
        }

    }
}


package ro.alexpopa.onlineshop;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
import ro.alexpopa.onlineshop.adapter.DataAdapter;
import ro.alexpopa.onlineshop.adapter.UserDataAdapter;
import ro.alexpopa.onlineshop.model.Order;
import ro.alexpopa.onlineshop.model.User;
import ro.alexpopa.onlineshop.service.ApiService;
import ro.alexpopa.onlineshop.service.ServiceFactory;
import ro.alexpopa.onlineshop.ui.MyCallback;
import timber.log.Timber;

public class NetworkingManager {
    private MainApp main;
    private ApiService apiService;
    public NetworkingManager(Application application){
        this.main = ((MainApp) application);
        apiService = ServiceFactory.createRetrofitService(ApiService.ENDPOINT);
    }

    public static void initializeWebSocket(View view) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url("ws://192.168.0.106:2302").build();
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

    public void loadOrdersForClient(ProgressBar progressBar, MyCallback callback, int id) {
        Observable<List<Order>> call = apiService.getOrdersForClient(id);
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Order>>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while loading the orders");
                        callback.showError("Not able to retrieve the data. Displaying local data!");
                    }

                    @Override
                    public void onComplete() {
                        Timber.v("Order Service load completed");
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final List<Order> orders) {
                        new Thread(() -> {
                            main.db.getOrderDao().deleteOrders();
                            main.db.getOrderDao().addOrders(orders);
                        }).start();
                        Timber.v("Orders persisted");
                    }
                });

    }


    public void save(ProgressBar progressBar, Order order, MyCallback callback) {
        apiService.recordOrder(order)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Order>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while persisting an document");
                        callback.showError("Not able to connect to the server, will not persist!");
                    }

                    @Override
                    public void onComplete() {
                        Timber.v("Order Service completed");
                        progressBar.setVisibility(View.GONE);
                        callback.clear();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDataLocally(order);
                    }

                    @Override
                    public void onNext(Order order1) {
                        Timber.v("Order persisted");
                    }
                });
    }

    public void updateOrder(ProgressBar progressBar, Order order, MyCallback callback) {
        apiService.updateOrder(order)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Order>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while persisting an document");
                        callback.showError("Not able to connect to the server, will not persist!");
                    }

                    @Override
                    public void onComplete() {
                        Timber.v("Order Service completed");
                        progressBar.setVisibility(View.GONE);
                        callback.clear();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Order order1) {
                        Timber.v("Order updated");
                    }
                });
    }


    private void addDataLocally(Order order) {
        new Thread(() -> main.db.getOrderDao().addOrder(order)).start();
    }

    public void getPendingOrders(ProgressBar progressBar, RecyclerView recyclerView, MyCallback callback){
        Observable<List<Order>> call = apiService.getPendingOrders();
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Order>>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while loading the orders");
                        callback.showError("Error while loading the orders");
                    }

                    @Override
                    public void onComplete() {
                        Timber.v("Order Service completed");
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final List<Order> orders) {
                        DataAdapter adapter = new DataAdapter();
                        adapter.setItems(orders);
                        recyclerView.setAdapter(adapter);
                        Timber.v("Available orders displayed");
                    }
                });
    }



    public void getTopTenUsers(ProgressBar progressBar, RecyclerView recyclerView, MyCallback callback){
        Observable<List<Order>> call = apiService.getAllOrders();
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Order>>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while loading the orders");
                        callback.showError("Error while loading the orders");
                    }

                    @Override
                    public void onComplete() {
                        Timber.v("Order Service completed");
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final List<Order> orders) {
                        UserDataAdapter adapter = new UserDataAdapter();
                        List<User> users = new ArrayList<>();
                        for (Order o: orders){
                            int id = o.getUser();
                            boolean found = false;
                            for (int i = 0; i < users.size(); i++) {
                                if (users.get(i).getId()==id){
                                    found=true;
                                    users.get(i).setNumberOfOrders(users.get(i).getNumberOfOrders()+1);
                                    break;
                                }
                            }
                            if(!found) {
                                users.add(new User(id, 1));
                            }

                        }
                        users.sort(Comparator.comparingInt(User::getNumberOfOrders));
                        Collections.reverse(users);
                        adapter.setItems(users);
                        recyclerView.setAdapter(adapter);
                        Timber.v("Top 10 users displayed");
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
            String message = "Someone added: " + jsonObject.get("details") + "," + jsonObject.get("status") + "!";
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        }

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
        }

    }
}


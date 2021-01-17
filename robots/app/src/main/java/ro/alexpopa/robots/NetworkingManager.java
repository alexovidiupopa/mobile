package ro.alexpopa.robots;

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
import ro.alexpopa.robots.adapter.DataAdapter;
import ro.alexpopa.robots.model.Robot;
import ro.alexpopa.robots.model.Type;
import ro.alexpopa.robots.service.ApiService;
import ro.alexpopa.robots.service.ServiceFactory;
import ro.alexpopa.robots.ui.MyCallback;
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
        Request request = new Request.Builder().url("ws://192.168.0.106:2202").build();
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

    public void getRobotsOfType(ProgressBar progressBar, MyCallback callback, String type) {
        Observable<List<Robot>> call = apiService.getRobotsOfType(type);
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Robot>>() {
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
                    public void onNext(final List<Robot> robots) {
                        new Thread(() -> {
                            main.db.getRobotDao().deleteRobots();
                            main.db.getRobotDao().addRobots(robots);
                        }).start();
                        Timber.v("Orders persisted");
                    }
                });

    }

    public void getTypes(ProgressBar progressBar, MyCallback callback) {
        Observable<List<String>> call = apiService.getAllTypes();
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
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
                    public void onNext(final List<String> types) {
                        List<Type> typesObj = types.stream().map(type->new Type(0, type)).collect(Collectors.toList());
                        new Thread(() -> {
                            main.db.getTypeDao().deleteTypes();
                            main.db.getTypeDao().addTypes(typesObj);
                        }).start();
                        Timber.v("Orders persisted");
                    }
                });

    }

    public void save(ProgressBar progressBar, Robot robot, MyCallback callback) {
        apiService.recordRobot(robot)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Robot>() {
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
                        addDataLocally(robot);
                    }

                    @Override
                    public void onNext(Robot robot1) {
                        Timber.v("Order persisted");
                    }
                });
    }

    public void updateHeight(ProgressBar progressBar, Robot robot, MyCallback callback) {
        apiService.updateHeight(robot)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Robot>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while persisting an document");
                        //callback.showError("Not able to connect to the server, will not persist!");
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
                    public void onNext(Robot robot1) {
                        Timber.v("Order updated");
                        new Thread(()->{
                            main.db.getRobotDao().updateRobot(robot1);
                        }).start();
                    }
                });
    }

    public void updateAge(ProgressBar progressBar, Robot robot, MyCallback callback) {
        apiService.updateAge(robot)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Robot>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while persisting an document");
                        //callback.showError("Not able to connect to the server, will not persist!");
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
                    public void onNext(Robot robot1) {
                        Timber.v("Order updated");
                    }
                });
    }

    private void addDataLocally(Robot robot) {
        new Thread(() -> main.db.getRobotDao().addRobot(robot)).start();
    }




    public void getOldestRobots(ProgressBar progressBar, RecyclerView recyclerView, MyCallback callback){
        Observable<List<Robot>> call = apiService.getAllOrders();
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Robot>>() {
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
                    public void onNext(final List<Robot> robots) {
                        DataAdapter adapter = new DataAdapter();
                        robots.sort(Comparator.comparingInt(Robot::getAge));
                        Collections.reverse(robots);
                        adapter.setItems(robots.subList(0, Math.min(10, robots.size())));
                        recyclerView.setAdapter(adapter);
                        Timber.v("Top 10 robots displayed");
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
            String message = "Someone added: " + jsonObject.get("name") + "," + jsonObject.get("specs") + "!";
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        }

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
        }

    }
}


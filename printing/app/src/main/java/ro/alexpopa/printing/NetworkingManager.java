package ro.alexpopa.printing;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
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
import ro.alexpopa.printing.adapter.DataAdapter;
import ro.alexpopa.printing.model.Model;
import ro.alexpopa.printing.service.ApiService;
import ro.alexpopa.printing.service.ServiceFactory;
import ro.alexpopa.printing.ui.MyCallback;
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
        Request request = new Request.Builder().url("ws://192.168.0.106:2901").build();
        WebSocketListener listener = new NetworkingManager.WebSocketListenerImpl(view);
        okHttpClient.newWebSocket(request, listener);
        okHttpClient.dispatcher().executorService().shutdown();
    }

    public boolean networkConnectivity(Context context){
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkCapabilities networkCapabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
        return networkCapabilities != null && (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
    }

    public void loadAllModels(ProgressBar progressBar, MyCallback callback, int user) {
        Observable<List<Model>> call = apiService.getModelsForClient(user);
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Model>>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while loading the Models");
                        callback.showError("Not able to retrieve the data. Displaying local data!");
                    }

                    @Override
                    public void onComplete() {
                        Timber.v("Model Service load completed");
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final List<Model> models) {
                        new Thread(() -> {
                            main.db.getModelsDao().deleteModels();
                            main.db.getModelsDao().addModels(models);
                        }).start();
                        Timber.v("Models persisted");
                    }
                });

    }

    public void updateRequest(ProgressBar progressBar, Model request, MyCallback callback){
        apiService.updateModel(request)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Model>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while updating a request");
                        callback.showError("Error while updating a request");
                    }

                    @Override
                    public void onComplete() {
                        Timber.v("Model Service update completed");
                        progressBar.setVisibility(View.GONE);
                        callback.clear();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Model model) {
                        Timber.v("Model updated");
                    }
                });
    }

    public void save(ProgressBar progressBar, Model model, MyCallback callback) {
        apiService.recordModel(model)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Model>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while persisting an request");
                        callback.showError("Not able to connect to the server, will not persist!");
                    }

                    @Override
                    public void onComplete() {
                        Timber.v("Model Service completed");
                        progressBar.setVisibility(View.GONE);
                        callback.clear();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDataLocally(model);
                    }

                    @Override
                    public void onNext(Model model) {
                        Timber.v("Model persisted");
                    }
                });
    }

    private void addDataLocally(Model model) {
        new Thread(() -> main.db.getModelsDao().addModel(model)).start();
    }


    public void getTopExpensive(ProgressBar progressBar, RecyclerView recyclerView, MyCallback callback){
        Observable<List<Model>> call = apiService.getAllModels();
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Model>>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while loading the Models");
                        callback.showError("Error while loading the Models");
                    }

                    @Override
                    public void onComplete() {
                        Timber.v("Model Service completed");
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final List<Model> models) {
                        List<Model> newModels = models.stream().sorted(Comparator.comparing(Model::getCost)).collect(Collectors.toList());
                        Collections.reverse(newModels);

                        DataAdapter adapter = new DataAdapter();
                        adapter.setFields(Arrays.asList("model","cost"));
                        adapter.setItems(newModels.subList(0,Math.min(5, newModels.size())));
                        recyclerView.setAdapter(adapter);
                        Timber.v("Top 5 Models by cost displayed");
                    }
                });
    }

    public void getTopEasiest(ProgressBar progressBar, RecyclerView recyclerView, MyCallback callback){
        Observable<List<Model>> call = apiService.getAllModels();
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Model>>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while loading the Models");
                        callback.showError("Error while loading the Models");
                    }

                    @Override
                    public void onComplete() {
                        Timber.v("Model Service completed");
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final List<Model> models) {
                        List<Model> newModels = models.stream().sorted(Comparator.comparing(Model::getTime)).collect(Collectors.toList());

                        DataAdapter adapter = new DataAdapter();
                        adapter.setFields(Arrays.asList("model","time"));
                        adapter.setItems(newModels.subList(0,Math.min(10, newModels.size())));
                        recyclerView.setAdapter(adapter);
                        Timber.v("Top 10 Models by time displayed");
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
            String message = "Someone added: " + jsonObject.get("model") + "," + jsonObject.get("time") + "," + jsonObject.get("cost") + "!";
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        }

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
        }

    }
}


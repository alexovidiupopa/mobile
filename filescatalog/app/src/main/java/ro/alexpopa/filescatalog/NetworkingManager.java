package ro.alexpopa.filescatalog;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
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
import ro.alexpopa.filescatalog.adapter.DataAdapter;
import ro.alexpopa.filescatalog.adapter.DataStringAdapter;
import ro.alexpopa.filescatalog.model.File;
import ro.alexpopa.filescatalog.service.ApiService;
import ro.alexpopa.filescatalog.service.ServiceFactory;
import ro.alexpopa.filescatalog.ui.ManagementActivity;
import ro.alexpopa.filescatalog.ui.MyCallback;
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
        Request request = new Request.Builder().url("ws://192.168.0.106:2702").build();
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

    public void loadAllModels(ProgressBar progressBar, MyCallback callback) {
        Observable<List<File>> call = apiService.getAllModels();
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<File>>() {
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
                    public void onNext(final List<File> files) {
                        new Thread(() -> {
                            main.db.getModelsDao().deleteModels();
                            main.db.getModelsDao().addModels(files);
                        }).start();
                        Timber.v("Models persisted");
                    }
                });

    }


    public void save(ProgressBar progressBar, File file, MyCallback callback) {
        apiService.recordModel(file)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<File>() {
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
                        addDataLocally(file);
                    }

                    @Override
                    public void onNext(File file) {
                        Timber.v("Model persisted");
                    }
                });
    }

    private void addDataLocally(File file) {
        new Thread(() -> main.db.getModelsDao().addModel(file)).start();
    }

    public void deleteById(ProgressBar progressBar, int id, MyCallback callback){
        apiService.deleteFile(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<File>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while deleting an request");
                        progressBar.setVisibility(View.GONE);
                        callback.showError("Not able to connect to the server, will not delete!");
                    }

                    @Override
                    public void onComplete() {
                        Timber.v("Model Service completed");
                        progressBar.setVisibility(View.GONE);
                        callback.clear();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(File file) {
                        Timber.v("Model deleted");
                    }
                });
    }


    public void getAllLocations(ProgressBar progressBar, RecyclerView recyclerView, MyCallback callback){
        Observable<List<String>> call = apiService.getAllLocations();
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
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
                    public void onNext(final List<String> locations) {
                        DataStringAdapter adapter = new DataStringAdapter();
                        adapter.setItems(locations);
                        recyclerView.setAdapter(adapter);
                        Timber.v("All locations fetched");
                    }
                });
    }

    public void getTopExpensive(ProgressBar progressBar, RecyclerView recyclerView, MyCallback callback){
        Observable<List<File>> call = apiService.getAllModels();
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<File>>() {
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
                    public void onNext(final List<File> files) {
                        List<File> newFiles = files.stream().sorted(Comparator.comparing(File::getUsage)).collect(Collectors.toList());
                        Collections.reverse(newFiles);

                        DataAdapter adapter = new DataAdapter();
                        adapter.setFields(Arrays.asList("name","status","location","usage"));
                        adapter.setItems(newFiles.subList(0,Math.min(10, newFiles.size())));

                        recyclerView.setAdapter(adapter);
                        Timber.v("Top 10 Files by usage displayed");
                    }
                });
    }

    public void getFilesForLocation(ProgressBar progressBar, String location, RecyclerView recyclerView, MyCallback callback) {
        Observable<List<File>> call = apiService.getFilesForLocation(location);
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<File>>() {
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
                    public void onNext(final List<File> files) {
                        DataAdapter adapter = new DataAdapter();
                        adapter.setFields(Arrays.asList("name","status","location","usage"));
                        //adapter.setManager(this, progressBar, recyclerView);
                        adapter.setItems(files);
                        recyclerView.setAdapter(adapter);
                        Timber.v("Top 10 Files by usage displayed");
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
            String message = "Someone added: " + jsonObject.get("name") + "," + jsonObject.get("size") + "," + jsonObject.get("location") + "!";
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
        }

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
        }

    }
}


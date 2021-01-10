package ro.alexpopa.documents;

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
import ro.alexpopa.documents.adapter.DataAdapter;
import ro.alexpopa.documents.model.Document;
import ro.alexpopa.documents.service.ApiService;
import ro.alexpopa.documents.service.ServiceFactory;
import ro.alexpopa.documents.ui.MyCallback;
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
        Request request = new Request.Builder().url("ws://192.168.0.106:2701").build();
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

    public void loadAllDocuments(ProgressBar progressBar, MyCallback callback, String user) {
        Observable<List<Document>> call = apiService.getDocumentsForUser(user);
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Document>>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while loading the documents");
                        callback.showError("Not able to retrieve the data. Displaying local data!");
                    }

                    @Override
                    public void onComplete() {
                        Timber.v("Document Service load completed");
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final List<Document> documents) {
                        new Thread(() -> {
                            main.db.getDocumentsDao().deleteDocuments();
                            main.db.getDocumentsDao().addDocuments(documents);
                        }).start();
                        Timber.v("Documents persisted");
                    }
                });

    }

    public void deleteDocument(ProgressBar progressBar,int document, MyCallback callback){
        apiService.deleteDocument(document)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Document>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while borrowing an document");
                        callback.showError("Error while borrowing a document");
                    }

                    @Override
                    public void onComplete() {
                        Timber.v("Document Service borrow completed");
                        progressBar.setVisibility(View.GONE);
                        callback.clear();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Document document) {
                        Timber.v("Document borrowed");
                    }
                });
    }

    public void save(ProgressBar progressBar, Document document, MyCallback callback) {
        apiService.recordDocument(document)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Document>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while persisting an document");
                        callback.showError("Not able to connect to the server, will not persist!");
                    }

                    @Override
                    public void onComplete() {
                        addDataLocally(document);
                        Timber.v("Document Service completed");
                        progressBar.setVisibility(View.GONE);
                        callback.clear();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Document document) {
                        Timber.v("Document persisted");
                    }
                });
    }

    private void addDataLocally(Document document) {
        new Thread(() -> main.db.getDocumentsDao().addDocument(document)).start();
    }

    public void getAvailableDocuments(ProgressBar progressBar, RecyclerView recyclerView, MyCallback callback){
        Observable<List<Document>> call = apiService.getAvailableDocuments();
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Document>>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while loading the documents");
                        callback.showError("Error while loading the documents");
                    }

                    @Override
                    public void onComplete() {
                        Timber.v("Document Service completed");
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final List<Document> documents) {
                        DataAdapter adapter = new DataAdapter();
                        adapter.setItems(documents);
                        recyclerView.setAdapter(adapter);
                        Timber.v("Available documents displayed");
                    }
                });
    }

    public void getTopTenDocuments(ProgressBar progressBar,RecyclerView recyclerView, MyCallback callback){
        Observable<List<Document>> call = apiService.getAvailableDocuments();
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Document>>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while loading the documents");
                        callback.showError("Error while loading the documents");
                    }

                    @Override
                    public void onComplete() {
                        Timber.v("Document Service completed");
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final List<Document> documents) {

                        List<Document> newDocuments = documents.stream().sorted(Comparator.comparing(Document::getUsage)).collect(Collectors.toList());
                        Collections.reverse(newDocuments);

                        DataAdapter adapter = new DataAdapter();
                        adapter.setItems(newDocuments.subList(0,10));
                        recyclerView.setAdapter(adapter);
                        Timber.v("Top 10 documents displayed");
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
            String message = "Someone added: " + jsonObject.get("name") + "," + jsonObject.get("size") + "," + jsonObject.get("usage") + "!";
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        }

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
        }

    }
}


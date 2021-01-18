package ro.alexpopa.exams;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
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

import ro.alexpopa.exams.adapter.DataAdapter;
import ro.alexpopa.exams.model.Exam;
import ro.alexpopa.exams.service.ApiService;
import ro.alexpopa.exams.service.ServiceFactory;
import ro.alexpopa.exams.ui.MyCallback;
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
        Request request = new Request.Builder().url("ws://192.168.0.106:2018").build();
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

    public void getAllExams(ProgressBar progressBar, MyCallback callback, String user) {
        Observable<List<Exam>> call = apiService.getAllExams();
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Exam>>() {
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
                    public void onNext(final List<Exam> exams) {
                        new Thread(() -> {
                            main.db.getDocumentsDao().deleteAllObjects();
                            main.db.getDocumentsDao().addObjects(exams);
                        }).start();
                        Timber.v("Documents persisted");
                    }
                });

    }

    public void getDetailsForExam(ProgressBar progressBar, MyCallback callback, int id) {
        Observable<Exam> call = apiService.getExamDetails(id);
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Exam>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while loading the documents");
                        //callback.showError("Not able to retrieve the data. Displaying local data!");
                        AlertDialog.Builder builder = new AlertDialog.Builder(progressBar.getContext());
                        builder.setMessage("Not able to retrieve the data. Displaying local data!");
                        builder.show();
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
                    public void onNext(final Exam exams) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(progressBar.getContext());
                        builder.setMessage(exams.getDetails());
                        builder.show();

                        Timber.v("Documents persisted");
                    }
                });

    }

    public void joinExam(ProgressBar progressBar, int exam, MyCallback callback){
        apiService.joinExam(new Exam(exam, "","","","", 0,""))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Exam>() {
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
                    public void onNext(Exam exam) {
                        Timber.v("Document borrowed");
                    }
                });
    }

    public void save(ProgressBar progressBar, Exam exam, MyCallback callback) {
        apiService.registerExam(exam)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Exam>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while persisting an document");
                        callback.showError("Not able to connect to the server, will not persist!");
                    }

                    @Override
                    public void onComplete() {
                        addDataLocally(exam);
                        Timber.v("Document Service completed");
                        progressBar.setVisibility(View.GONE);
                        callback.clear();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Exam exam) {
                        Timber.v("Document persisted");
                    }
                });
    }

    private void addDataLocally(Exam exam) {
        new Thread(() -> main.db.getDocumentsDao().addObject(exam)).start();
    }

    public void getDraftedExams(ProgressBar progressBar, RecyclerView recyclerView, MyCallback callback){
        Observable<List<Exam>> call = apiService.getDraftedExams();
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Exam>>() {
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
                    public void onNext(final List<Exam> exams) {
                        DataAdapter adapter = new DataAdapter();
                        adapter.setItems(exams);
                        recyclerView.setAdapter(adapter);
                        Timber.v("Available documents displayed");
                    }
                });
    }

    public void getTopExams(ProgressBar progressBar, RecyclerView recyclerView, MyCallback callback, String group){
        Observable<List<Exam>> call = apiService.getExamsForGroup(group);
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Exam>>() {
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
                    public void onNext(final List<Exam> exams) {

                        List<Exam> newExams = exams.stream().sorted((o1, o2) -> {
                            if (o1.getType().equals(o2.getType()))
                                return o2.getStudents()-o1.getStudents();
                            return o1.getType().compareTo(o2.getType());
                        }).collect(Collectors.toList());

                        DataAdapter adapter = new DataAdapter();
                        adapter.setItems(newExams);
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
            String message = "Someone added an object: name-" + jsonObject.get("name") + ", group-" + jsonObject.get("group") + ", status-" + jsonObject.get("status") + "!";
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        }

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
        }

    }
}


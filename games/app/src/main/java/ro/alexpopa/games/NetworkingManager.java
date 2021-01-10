package ro.alexpopa.games;

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
import ro.alexpopa.games.adapter.DataAdapter;
import ro.alexpopa.games.model.Game;
import ro.alexpopa.games.service.ApiService;
import ro.alexpopa.games.service.ServiceFactory;
import ro.alexpopa.games.ui.MyCallback;
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
        Request request = new Request.Builder().url("ws://192.168.0.106:2502").build();
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

    public void loadAllGames(ProgressBar progressBar, MyCallback callback, String user) {
        Observable<List<Game>> call = apiService.getGamesForUser(user);
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Game>>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while loading the games");
                        callback.showError("Not able to retrieve the data. Displaying local data!");
                    }

                    @Override
                    public void onComplete() {
                        Timber.v("Game Service load completed");
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final List<Game> games) {
                        new Thread(() -> {
                            main.db.getGamesDao().deleteGames();
                            main.db.getGamesDao().addGames(games);
                        }).start();
                        Timber.v("Games persisted");
                    }
                });

    }

    public void borrow(ProgressBar progressBar,Game game, MyCallback callback){
        apiService.borrowGame(game)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Game>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while borrowing an game");
                        callback.showError("Error while borrowing a game");
                    }

                    @Override
                    public void onComplete() {
                        Timber.v("Game Service borrow completed");
                        progressBar.setVisibility(View.GONE);
                        callback.clear();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Game game) {
                        Timber.v("Game borrowed");
                    }
                });
    }

    public void save(ProgressBar progressBar, Game game, MyCallback callback) {
        apiService.recordGame(game)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Game>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while persisting an game");
                        callback.showError("Not able to connect to the server, will not persist!");
                    }

                    @Override
                    public void onComplete() {
                        addDataLocally(game);
                        Timber.v("Game Service completed");
                        progressBar.setVisibility(View.GONE);
                        callback.clear();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Game game) {
                        Timber.v("Game persisted");
                    }
                });
    }

    private void addDataLocally(Game game) {
        new Thread(() -> main.db.getGamesDao().addGame(game)).start();
    }

    public void getAvailableGames(ProgressBar progressBar, RecyclerView recyclerView, MyCallback callback){
        Observable<List<Game>> call = apiService.getAvailableGames();
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Game>>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while loading the games");
                        callback.showError("Error while loading the games");
                    }

                    @Override
                    public void onComplete() {
                        Timber.v("Game Service completed");
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final List<Game> games) {
                        DataAdapter adapter = new DataAdapter();
                        adapter.setItems(games);
                        recyclerView.setAdapter(adapter);
                        Timber.v("Available games displayed");
                    }
                });
    }

    public void getTopTenGames(ProgressBar progressBar,RecyclerView recyclerView, MyCallback callback){
        Observable<List<Game>> call = apiService.getAllGames();
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Game>>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while loading the games");
                        callback.showError("Error while loading the games");
                    }

                    @Override
                    public void onComplete() {
                        Timber.v("Game Service completed");
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final List<Game> games) {

                        List<Game> newGames = games.stream().sorted(Comparator.comparing(Game::getPopularityScore)).collect(Collectors.toList());
                        Collections.reverse(newGames);

                        DataAdapter adapter = new DataAdapter();
                        adapter.setItems(newGames.subList(0,10));
                        recyclerView.setAdapter(adapter);
                        Timber.v("Top 10 games displayed");
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
            String message = "Someone added: " + jsonObject.get("name") + "," + jsonObject.get("size") + "," + jsonObject.get("popularityScore") + "!";
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        }

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
        }

    }
}


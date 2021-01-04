package ro.alexpopa.watchlist.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ro.alexpopa.watchlist.R;
import ro.alexpopa.watchlist.db.DatabaseHandler;
import ro.alexpopa.watchlist.domain.Movie;
import ro.alexpopa.watchlist.web.RestController;

public class ViewMoviesActivity extends AppCompatActivity {

    private static final int ADD_MOVIE_REQUEST_CODE = 1;

    private DatabaseHandler dbHandler = new DatabaseHandler(this);
    private MovieAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        RecyclerView movieList = findViewById(R.id.movieList);

        getData();

        movieList.setLayoutManager(linearLayoutManager);
        movieList.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));

        swipeRefreshLayout = findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this::onResume);

        FloatingActionButton fab = findViewById(R.id.addMovie);
        fab.setOnClickListener(view -> {
            Intent addMovieIntent = new Intent(getApplicationContext(), AddMovieActivity.class);
            startActivityForResult(addMovieIntent,ADD_MOVIE_REQUEST_CODE);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getData(){
        RecyclerView movieList = findViewById(R.id.movieList);

        Call<List<Movie>> movies = RestController.getInstance().getAllMovies();
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);

        movies.enqueue(new Callback<List<Movie>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                Toast.makeText(getApplicationContext(), String.format("CONNECTION IS UP!"), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
                List<Movie> serverMovies = response.body();

                // syncing with local db
                boolean synced = false;
                List<Movie> localMovies = dbHandler.getAllMovies();
                for (Movie movie : localMovies) {
                    if (!serverMovies.contains(movie)) {
                        synced = true;
                        Call<Movie> movieCall = RestController.getInstance().addMovie(movie);
                        movieCall.enqueue(new Callback<Movie>() {
                            @Override
                            public void onResponse(Call<Movie> call, Response<Movie> response) {
                                Toast.makeText(getApplicationContext(), String.format("CONNECTION IS UP...SAVED MOVIE"), Toast.LENGTH_LONG).show();
                                dbHandler.deleteMovie(movie.getId());
                            }

                            @Override
                            public void onFailure(Call<Movie> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), String.format("CONNECTION IS DOWN...USING LOCAL DB"), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
                if (synced){
                    synced = false;
                    getData();
                }
                adapter = new MovieAdapter(serverMovies);
                movieList.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), String.format("CONNECTION IS DOWN!"), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
                adapter = new MovieAdapter(dbHandler.getAllMovies());
                movieList.setAdapter(adapter);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        swipeRefreshLayout.setRefreshing(true);
        getData();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_MOVIE_REQUEST_CODE  && resultCode == RESULT_OK){
            onResume();
        }
    }
}
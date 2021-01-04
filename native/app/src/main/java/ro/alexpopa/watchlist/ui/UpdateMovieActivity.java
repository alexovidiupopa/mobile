package ro.alexpopa.watchlist.ui;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ro.alexpopa.watchlist.R;
import ro.alexpopa.watchlist.db.DatabaseHandler;
import ro.alexpopa.watchlist.domain.Movie;
import ro.alexpopa.watchlist.web.RestController;


public class UpdateMovieActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_movie);

        DatabaseHandler dbHandler = new DatabaseHandler(this);

        int movieId = getIntent().getIntExtra(String.valueOf(R.string.idExtra),-1);

        Call<Movie> movieCall = RestController.getInstance().getMovieById(movieId);
        movieCall.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                Movie movie = response.body();
                Toolbar toolbar = findViewById(R.id.updateToolbar);

                toolbar.setTitle(movie.getTitle());
                setSupportActionBar(toolbar);

                TextView titleView = findViewById(R.id.titleUpdate);
                TextView directorView = findViewById(R.id.directorUpdate);
                TextView yearView = findViewById(R.id.yearUpdate);
                RatingBar rb = findViewById(R.id.ratingBarUpdate);

                titleView.setText(movie.getTitle());
                directorView.setText(movie.getDirector());
                yearView.setText(String.valueOf(movie.getYear()));
                rb.setRating((float) movie.getRating());

                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.INVISIBLE);
                progressBar.setIndeterminate(true);

                Button updateButton = findViewById(R.id.updateMovieButton);
                updateButton.setOnClickListener(view -> {
                    if (titleView.getText().length()>0 && directorView.getText().length()>0 && yearView.getText().length()>0){
                        movie.setTitle(titleView.getText().toString());
                        movie.setDirector(directorView.getText().toString());
                        movie.setYear(Integer.parseInt(yearView.getText().toString()));
                        movie.setRating(rb.getRating());
                        Call<Movie> updateMovieCall = RestController.getInstance().updateMovie(movie);

                        progressBar.setVisibility(View.VISIBLE);
                        updateMovieCall.enqueue(new Callback<Movie>() {
                            @Override
                            public void onResponse(Call<Movie> call, Response<Movie> response) {
                                Toast.makeText(getApplicationContext(),String.format("UPDATING..."),Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                finish();

                            }

                            @Override
                            public void onFailure(Call<Movie> call, Throwable t) {
                                Toast.makeText(getApplicationContext(),String.format("CAN'T UPDATE!"),Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                finish();
                            }
                        });

                    }
                    else{
                        Snackbar.make(view.getRootView(), R.string.fabErrorMessage, Snackbar.LENGTH_LONG)
                                .show();
                    }
                });
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Toast.makeText(getApplicationContext(),String.format("CAN'T UPDATE!"),Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }
}
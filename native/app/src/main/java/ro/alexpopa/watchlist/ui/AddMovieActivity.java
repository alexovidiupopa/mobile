package ro.alexpopa.watchlist.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ro.alexpopa.watchlist.R;
import ro.alexpopa.watchlist.db.DatabaseHandler;
import ro.alexpopa.watchlist.domain.Movie;
import ro.alexpopa.watchlist.web.RestController;


public class AddMovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.newMovie);
        setSupportActionBar(toolbar);

        TextView titleView = findViewById(R.id.titleAdd);
        TextView directorView = findViewById(R.id.directorAdd);
        TextView yearView = findViewById(R.id.yearAdd);
        RatingBar rb = findViewById(R.id.ratingBarAdd);
        rb.setRating(5);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.setIndeterminate(true);

        FloatingActionButton fab = findViewById(R.id.addMovieButton);
        fab.setOnClickListener(view -> {

            if (titleView.getText().length() > 0 && directorView.getText().length() > 0 && yearView.getText().length() > 0) {
                Movie movieToAdd = new Movie(String.valueOf(titleView.getText()), String.valueOf(directorView.getText()), Integer.parseInt(String.valueOf(yearView.getText())), rb.getRating());
                DatabaseHandler dbHandler = new DatabaseHandler(this);
                Call<Movie> movieCall = RestController.getInstance().addMovie(movieToAdd);

                progressBar.setVisibility(View.VISIBLE);
                movieCall.enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(Call<Movie> call, Response<Movie> response) {
                        Toast.makeText(getApplicationContext(), String.format("CONNECTION IS UP...SAVED MOVIE"), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Movie> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), String.format("CONNECTION IS DOWN...USING LOCAL DB"), Toast.LENGTH_LONG).show();
                        dbHandler.addMovie(movieToAdd);
                        progressBar.setVisibility(View.INVISIBLE);
                        setResult(RESULT_OK);
                        finish();
                    }
                });

            } else {
                Snackbar.make(view.getRootView(), R.string.fabErrorMessage, Snackbar.LENGTH_LONG)
                        .show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
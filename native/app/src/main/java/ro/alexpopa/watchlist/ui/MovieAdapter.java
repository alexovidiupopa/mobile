package ro.alexpopa.watchlist.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ro.alexpopa.watchlist.R;
import ro.alexpopa.watchlist.domain.Movie;
import ro.alexpopa.watchlist.web.RestController;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {

    private List<Movie> movieList;

    public MovieAdapter(List<Movie> movies) {
        movieList = movies;
    }


    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovieHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_movie_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
        holder.bindItem(movieList.get(position));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        private Movie movie;


        public MovieHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent updateMovieIntent = new Intent(v.getContext(),UpdateMovieActivity.class);
            updateMovieIntent.putExtra(String.valueOf(R.string.idExtra), movie.getId());
            v.getContext().startActivity(updateMovieIntent);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public boolean onLongClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage("Are you sure you want to delete " + movie.getTitle() + "?")
                    .setPositiveButton(R.string.dialogYes, (dialog, id) -> {
                        Call<Movie> response = RestController.getInstance().deleteMovie(movie.getId());

                        response.enqueue(new Callback<Movie>() {
                            @Override
                            public void onResponse(Call<Movie> call, Response<Movie> response) {
                                Toast.makeText(v.getContext(), String.format("MOVIE DELETED! REFRESH..."), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Call<Movie> call, Throwable t) {
                                Toast.makeText(v.getContext(), String.format("NO DELETE ALLOWED"), Toast.LENGTH_LONG).show();
                            }
                        });

                    })
                    .setNegativeButton(R.string.dialogNo, (dialog, id) -> {

                    });
            builder.create().show();
            return true;
        }

        public void bindItem(Movie movie) {
            this.movie = movie;

            LinearLayout fieldsLayout = itemView.findViewById(R.id.movieContainer);

            TextView titleView = fieldsLayout.findViewById(R.id.title);
            TextView directorView = fieldsLayout.findViewById(R.id.director);
            TextView yearView = fieldsLayout.findViewById(R.id.year);
            TextView seenView = fieldsLayout.findViewById(R.id.seen);

            titleView.setText(movie.getTitle());
            titleView.setTextColor(Color.BLACK);
            titleView.setTypeface(null, Typeface.BOLD);

            directorView.setText(movie.getDirector());
            directorView.setTypeface(null, Typeface.BOLD_ITALIC);

            yearView.setText("(" + movie.getYear() + ")");


            if (movie.isSeen()){
                seenView.setText(movie.getRating() + "/10");
                seenView.setTextColor(Color.BLUE);
            }
            else{
                seenView.setText(R.string.not_seen_res);
                seenView.setTextColor(Color.GRAY);
            }
        }
    }
}

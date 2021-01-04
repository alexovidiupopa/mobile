package ro.alexpopa.watchlist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ro.alexpopa.watchlist.domain.Movie;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "moviesdb.db";
    private static final String TABLE_MOVIES = "movies";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DIRECTOR = "director";
    private static final String KEY_YEAR = "year";
    private static final String KEY_RATING = "rating";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        String sql = "CREATE TABLE " + TABLE_MOVIES + "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TITLE + " TEXT," +
                KEY_DIRECTOR + " TEXT," + KEY_YEAR + " INTEGER," +
                KEY_RATING + " REAL" + ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public void addMovie(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, movie.getTitle());
        values.put(KEY_DIRECTOR, movie.getDirector());
        values.put(KEY_YEAR, movie.getYear());
        values.put(KEY_RATING, movie.getRating());

        // Inserting Row
        db.beginTransaction();
        db.insert(TABLE_MOVIES, null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close(); // Closing database connection
    }

    public void deleteMovie(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        db.delete(TABLE_MOVIES, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public int updateMovie(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, movie.getTitle());
        values.put(KEY_DIRECTOR, movie.getDirector());
        values.put(KEY_YEAR, movie.getYear());
        values.put(KEY_RATING, movie.getRating());

        // updating row
        db.beginTransaction();
        int result = db.update(TABLE_MOVIES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(movie.getId()) });
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return result;
    }

    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_MOVIES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie(cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)), Double.parseDouble(cursor.getString(4)));
                movie.setId(Integer.parseInt(cursor.getString(0)));
                // Adding movie to list
                movies.add(movie);
            } while (cursor.moveToNext());
            cursor.close();
        }

        // return movies list
        return movies;
    }

    public Movie getMovie(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MOVIES, new String[] { KEY_ID,
                        KEY_TITLE, KEY_DIRECTOR, KEY_YEAR, KEY_RATING }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Movie movie = new Movie(cursor.getString(1),
                cursor.getString(2), Integer.parseInt(cursor.getString(3)), Double.parseDouble(cursor.getString(4)));
        movie.setId(Integer.parseInt(cursor.getString(0)));
        cursor.close();
        // return movie
        return movie;
    }
}

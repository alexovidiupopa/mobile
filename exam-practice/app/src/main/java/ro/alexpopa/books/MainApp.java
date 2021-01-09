package ro.alexpopa.books;

import android.app.Application;

import androidx.room.Room;

import ro.alexpopa.books.db.BookDatabase;
import timber.log.Timber;

public class MainApp extends Application {

    public BookDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        db = Room.databaseBuilder(getApplicationContext(),BookDatabase.class, "books-5a.db").build();
    }
}

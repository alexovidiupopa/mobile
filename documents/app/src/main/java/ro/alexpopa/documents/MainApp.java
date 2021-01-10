package ro.alexpopa.documents;

import android.app.Application;

import androidx.room.Room;

import ro.alexpopa.documents.db.DocumentDatabase;
import timber.log.Timber;

public class MainApp extends Application {
    public DocumentDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        db = Room.databaseBuilder(getApplicationContext(), DocumentDatabase.class, "documents.db").build();
    }

}

package ro.alexpopa.games;

import android.app.Application;

import androidx.room.Room;

import ro.alexpopa.games.db.GameDatabase;
import timber.log.Timber;

public class MainApp extends Application {
    public GameDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        db = Room.databaseBuilder(getApplicationContext(), GameDatabase.class, "games.db").build();
    }

}

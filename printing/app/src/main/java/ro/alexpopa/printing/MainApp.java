package ro.alexpopa.printing;

import android.app.Application;

import androidx.room.Room;

import ro.alexpopa.printing.db.ModelDatabase;
import timber.log.Timber;

public class MainApp extends Application {
    public ModelDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        db = Room.databaseBuilder(getApplicationContext(), ModelDatabase.class, "printing.db").build();
    }

}

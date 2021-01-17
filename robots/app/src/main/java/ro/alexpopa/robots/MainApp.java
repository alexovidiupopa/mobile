package ro.alexpopa.robots;

import android.app.Application;

import androidx.room.Room;

import ro.alexpopa.robots.db.RobotDatabase;
import timber.log.Timber;

public class MainApp extends Application {
    public RobotDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        db = Room.databaseBuilder(getApplicationContext(), RobotDatabase.class, "robots.db").build();
    }

}

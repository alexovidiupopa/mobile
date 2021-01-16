package ro.alexpopa.onlineshop;

import android.app.Application;

import androidx.room.Room;

import ro.alexpopa.onlineshop.db.OrderDatabase;
import timber.log.Timber;

public class MainApp extends Application {
    public OrderDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        db = Room.databaseBuilder(getApplicationContext(), OrderDatabase.class, "onlineshop.db").build();
    }

}

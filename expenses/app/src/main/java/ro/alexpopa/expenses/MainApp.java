package ro.alexpopa.expenses;

import android.app.Application;

import androidx.room.Room;

import ro.alexpopa.expenses.db.ExpenseDatabase;
import timber.log.Timber;

public class MainApp extends Application {
    public ExpenseDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        db = Room.databaseBuilder(getApplicationContext(), ExpenseDatabase.class, "expenses.db").build();
    }

}

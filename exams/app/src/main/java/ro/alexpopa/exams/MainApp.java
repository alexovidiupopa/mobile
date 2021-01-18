package ro.alexpopa.exams;

import android.app.Application;

import androidx.room.Room;

import ro.alexpopa.exams.db.ExamDatabase;
import timber.log.Timber;

public class MainApp extends Application {
    public ExamDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        db = Room.databaseBuilder(getApplicationContext(), ExamDatabase.class, "examz21.db").build();
    }

}

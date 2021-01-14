package ro.alexpopa.filescatalog.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ro.alexpopa.filescatalog.model.File;

@Database(entities = {File.class}, version = 1)
public abstract class ModelDatabase extends RoomDatabase {
    public abstract FileDao getModelsDao();
}

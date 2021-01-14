package ro.alexpopa.printing.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ro.alexpopa.printing.model.Model;

@Database(entities = {Model.class}, version = 1)
public abstract class ModelDatabase extends RoomDatabase {
    public abstract ModelDao getModelsDao();
}

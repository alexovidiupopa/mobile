package ro.alexpopa.documents.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ro.alexpopa.documents.model.Document;
import ro.alexpopa.documents.model.Document;

@Database(entities = {Document.class}, version = 1)
public abstract class DocumentDatabase extends RoomDatabase {
    public abstract DocumentDao getDocumentsDao();
}

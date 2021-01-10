package ro.alexpopa.documents.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ro.alexpopa.documents.model.Document;

@Dao
public interface DocumentDao {
    @Insert
    void addDocument(Document g);

    @Insert
    void addDocuments(List<Document> documents);

    @Delete
    void deleteDocument(Document g);

    @Query("delete from documents")
    void deleteDocuments();

    @Update
    void updateDocument(Document document);

    @Query("select * from documents")
    LiveData<List<Document>> getDocuments();
}

package ro.alexpopa.filescatalog.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ro.alexpopa.filescatalog.model.File;

@Dao
public interface FileDao {
    @Insert
    void addModel(File g);

    @Insert
    void addModels(List<File> expens);

    @Delete
    void deleteDocument(File g);

    @Query("delete from files")
    void deleteModels();

    @Update
    void updateDocument(File file);

    @Query("select * from files")
    LiveData<List<File>> getModels();
}

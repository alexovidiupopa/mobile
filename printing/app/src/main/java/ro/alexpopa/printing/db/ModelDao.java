package ro.alexpopa.printing.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ro.alexpopa.printing.model.Model;

@Dao
public interface ModelDao {
    @Insert
    void addModel(Model g);

    @Insert
    void addModels(List<Model> expens);

    @Delete
    void deleteDocument(Model g);

    @Query("delete from printing")
    void deleteModels();

    @Update
    void updateDocument(Model model);

    @Query("select * from printing")
    LiveData<List<Model>> getModels();
}

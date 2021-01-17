package ro.alexpopa.robots.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ro.alexpopa.robots.model.Type;

@Dao
public interface TypeDao {
    @Insert
    void addType(Type g);

    @Insert
    void addTypes(List<Type> orders);

    @Delete
    void deleteType(Type g);

    @Query("delete from types")
    void deleteTypes();

    @Update
    void updateType(Type document);

    @Query("select * from types")
    LiveData<List<Type>> getTypes();

}

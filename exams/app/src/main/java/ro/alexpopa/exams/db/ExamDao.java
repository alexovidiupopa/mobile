package ro.alexpopa.exams.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ro.alexpopa.exams.model.Exam;


@Dao
public interface ExamDao {
    @Insert
    void addObject(Exam g);

    @Insert
    void addObjects(List<Exam> exams);

    @Delete
    void deleteObject(Exam g);

    @Query("delete from exams")
    void deleteAllObjects();

    @Update
    void updateObject(Exam exam);

    @Query("select * from exams")
    LiveData<List<Exam>> getAllObjects();
}

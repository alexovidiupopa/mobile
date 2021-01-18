package ro.alexpopa.exams.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ro.alexpopa.exams.model.Exam;


@Database(entities = {Exam.class}, version = 1)
public abstract class ExamDatabase extends RoomDatabase {
    public abstract ExamDao getDocumentsDao();
}

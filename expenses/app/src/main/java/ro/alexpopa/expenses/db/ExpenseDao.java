package ro.alexpopa.expenses.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ro.alexpopa.expenses.model.Expense;

@Dao
public interface ExpenseDao {
    @Insert
    void addDocument(Expense g);

    @Insert
    void addExpenses(List<Expense> expenses);

    @Delete
    void deleteDocument(Expense g);

    @Query("delete from expenses")
    void deleteExpenses();

    @Update
    void updateDocument(Expense expense);

    @Query("select * from expenses")
    LiveData<List<Expense>> getExpenses();
}

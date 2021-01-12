package ro.alexpopa.expenses.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ro.alexpopa.expenses.model.Expense;

@Database(entities = {Expense.class}, version = 1)
public abstract class ExpenseDatabase extends RoomDatabase {
    public abstract ExpenseDao getExpensesDao();
}

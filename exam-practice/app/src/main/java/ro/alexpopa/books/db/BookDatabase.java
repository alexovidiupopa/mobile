package ro.alexpopa.books.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import ro.alexpopa.books.model.Book;

@Database(entities = {Book.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class BookDatabase extends RoomDatabase {
    public abstract BooksDao getBooksDao();

}

package ro.alexpopa.books.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ro.alexpopa.books.model.Book;

@Dao
public interface BooksDao {

    @Insert
    void addBook(Book book);

    @Insert
    void addBooks(List<Book> books);

    @Delete
    void deleteBook(Book b);

    @Query("delete from books")
    void deleteBooks();

    @Update
    void updateBook(Book book);

    @Query("select * from books")
    LiveData<List<Book>> getBooks();


}

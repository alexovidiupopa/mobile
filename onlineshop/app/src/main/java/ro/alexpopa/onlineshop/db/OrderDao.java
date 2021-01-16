package ro.alexpopa.onlineshop.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ro.alexpopa.onlineshop.model.Order;

@Dao
public interface OrderDao {
    @Insert
    void addOrder(Order g);

    @Insert
    void addOrders(List<Order> orders);

    @Delete
    void deleteOrder(Order g);

    @Query("delete from orders")
    void deleteOrders();

    @Update
    void updateOrder(Order document);

    @Query("select * from orders")
    LiveData<List<Order>> getOrders();

}

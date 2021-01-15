package ro.alexpopa.restaurant.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ro.alexpopa.restaurant.model.Order;

@Database(entities = {Order.class}, version = 1)
public abstract class OrderDatabase extends RoomDatabase {
    public abstract OrderDao getOrderDao();
}


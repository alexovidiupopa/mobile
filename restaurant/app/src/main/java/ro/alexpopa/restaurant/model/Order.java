package ro.alexpopa.restaurant.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "orders")
public class Order {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String table;
    private String details;
    private String status;
    private int time;
    private String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Order(int id, String table, String details, String status, int time, String type) {
        this.id = id;
        this.table = table;
        this.details = details;
        this.status = status;
        this.time = time;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", table='" + table + '\'' +
                ", details='" + details + '\'' +
                ", status='" + status + '\'' +
                ", time=" + time +
                ", type='" + type + '\'' +
                '}';
    }
}

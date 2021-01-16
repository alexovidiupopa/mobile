package ro.alexpopa.onlineshop.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "orders")
public class Order {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String details;
    private String status;
    private int user;
    private int age;
    private String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Order(int id, String details, String status, int user, int age, String type) {
        this.id = id;
        this.details = details;
        this.status = status;
        this.user = user;
        this.age = age;
        this.type = type;
    }
}

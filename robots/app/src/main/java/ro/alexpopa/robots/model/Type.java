package ro.alexpopa.robots.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "types")
public class Type {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
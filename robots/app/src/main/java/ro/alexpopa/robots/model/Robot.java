package ro.alexpopa.robots.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "robots")
public class Robot {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String specs;
    private int height;
    private int age;
    private String type;

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

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Robot(int id, String name, String specs, int height, int age, String type) {
        this.id = id;
        this.name = name;
        this.specs = specs;
        this.height = height;
        this.age = age;
        this.type = type;
    }
}

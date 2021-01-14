package ro.alexpopa.filescatalog.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "files")
public class File {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String status;
    private int size;
    private String location;
    private int usage;


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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public File(int id, String name, String status, int size, String location, int usage) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.size = size;
        this.location = location;
        this.usage = usage;
    }

    public int getUsage() {
        return usage;
    }

    public void setUsage(int usage) {
        this.usage = usage;
    }
}

package ro.alexpopa.documents.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "documents")
public class Document {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String status;
    private String owner;
    private int size;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getUsage() {
        return usage;
    }

    public void setUsage(int usage) {
        this.usage = usage;
    }

    public Document(int id, String name, String status, String owner, int size, int usage) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.owner = owner;
        this.size = size;
        this.usage = usage;
    }
}

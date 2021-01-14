package ro.alexpopa.printing.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "printing")
public class Model {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String model;
    private String status;
    private int client;
    private int time;
    private int cost;

    public Model(int id, String model, String status, int client, int time, int cost) {
        this.id = id;
        this.model = model;
        this.status = status;
        this.client = client;
        this.time = time;
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getClient() {
        return client;
    }

    public void setClient(int client) {
        this.client = client;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}

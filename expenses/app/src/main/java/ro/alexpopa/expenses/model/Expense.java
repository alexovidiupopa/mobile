package ro.alexpopa.expenses.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "expenses")
public class Expense {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String status;
    private String student;
    private int eCost;
    private int cost;

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

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public int getECost() {
        return eCost;
    }

    public void setECost(int eCost) {
        this.eCost = eCost;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public Expense(int id, String name, String status, String student, int eCost, int cost) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.student = student;
        this.eCost = eCost;
        this.cost = cost;
    }
}

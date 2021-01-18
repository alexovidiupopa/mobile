package ro.alexpopa.exams.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "exams")
public class Exam {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String group;
    private String details;
    private String status;
    private int students;
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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
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

    public int getStudents() {
        return students;
    }

    public void setStudents(int students) {
        this.students = students;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Exam(int id, String name, String group, String details, String status, int students, String type) {
        this.id = id;
        this.name = name;
        this.group = group;
        this.details = details;
        this.status = status;
        this.students = students;
        this.type = type;
    }
}

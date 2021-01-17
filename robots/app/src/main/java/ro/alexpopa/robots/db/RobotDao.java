package ro.alexpopa.robots.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ro.alexpopa.robots.model.Robot;

@Dao
public interface RobotDao {
    @Insert
    void addRobot(Robot g);

    @Insert
    void addRobots(List<Robot> robots);

    @Delete
    void deleteRobot(Robot g);

    @Query("delete from robots")
    void deleteRobots();

    @Update
    void updateRobot(Robot document);

    @Query("select * from robots")
    LiveData<List<Robot>> getRobots();

}

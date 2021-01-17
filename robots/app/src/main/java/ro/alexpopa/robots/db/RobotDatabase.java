package ro.alexpopa.robots.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ro.alexpopa.robots.model.Robot;
import ro.alexpopa.robots.model.Type;

@Database(entities = {Robot.class, Type.class}, version = 1)
public abstract class RobotDatabase extends RoomDatabase {
    public abstract RobotDao getRobotDao();
    public abstract TypeDao getTypeDao();
}


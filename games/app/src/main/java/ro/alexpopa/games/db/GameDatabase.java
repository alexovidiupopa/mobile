package ro.alexpopa.games.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ro.alexpopa.games.model.Game;

@Database(entities = {Game.class}, version = 1)
public abstract class GameDatabase extends RoomDatabase {
    public abstract GamesDao getGamesDao();
}

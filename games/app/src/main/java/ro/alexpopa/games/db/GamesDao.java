package ro.alexpopa.games.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ro.alexpopa.games.model.Game;

@Dao
public interface GamesDao {
    @Insert
    void addGame(Game g);

    @Insert
    void addGames(List<Game> games);

    @Delete
    void deleteGame(Game g);

    @Query("delete from games")
    void deleteGames();

    @Update
    void updateGame(Game game);

    @Query("select * from games")
    LiveData<List<Game>> getGames();
}

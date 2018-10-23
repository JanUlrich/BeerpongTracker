package kuckuck.de.statisticallydrinking.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface HitDao {
    @Query("SELECT * FROM hit")
    List<Hit> getAll();

    @Query("SELECT * FROM hit WHERE player_name LIKE :playerName")
    Hit findFromPlayer(String playerName);

    @Insert
    void insertAll(Hit... hits);
}

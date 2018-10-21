package kuckuck.de.statisticallydrinking.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Hit {
    @PrimaryKey
    private int timestamp;

    @ColumnInfo(name = "cup_number")
    private int cup;

    @ColumnInfo(name = "player_name")
    private String playerName;

    @ColumnInfo(name = "cup_layout")
    private int cupLayout;

    @ColumnInfo(name = "game_id")
    private int gameId;
}

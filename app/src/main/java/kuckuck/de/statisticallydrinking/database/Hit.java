package kuckuck.de.statisticallydrinking.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Hit {
    public Hit(long timestamp, String playerName, int cup, int cupLayout, long gameId){
        this.timestamp = timestamp;
        this.playerName = playerName;
        this.cup = cup;
        this.cupLayout = cupLayout;
        this.gameId = gameId;
    }
    @PrimaryKey
    private long timestamp;

    @ColumnInfo(name = "cup_number")
    private int cup;

    @ColumnInfo(name = "player_name")
    private String playerName;

    @ColumnInfo(name = "cup_layout")
    private int cupLayout;

    @ColumnInfo(name = "game_id")
    private long gameId;

    public long getTimestamp(){
        return timestamp;
    }

    public long getGameId() {
        return gameId;
    }

    public int getCupLayout() {
        return cupLayout;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getCup() {
        return cup;
    }
}

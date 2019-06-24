package kuckuck.de.statisticallydrinking.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Hit {;

    @Ignore
    public Hit(long timestamp, String playerName, int cup, int cupLayout, String gameId, int team){
        this(timestamp, playerName, cup, cupLayout, gameId, team, false);
    }

    public Hit(long timestamp, String playerName, int cup, int cupLayout, String gameId, int team, boolean isUndo){
        this.timestamp = timestamp;
        this.playerName = playerName;
        this.cup = cup;
        this.cupLayout = cupLayout;
        this.gameId = gameId;
        this.team = team;
        this.isUndo = isUndo;
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
    private String gameId;

    @ColumnInfo(name = "undo_operation")
    private boolean isUndo;

    private final int team;

    public long getTimestamp(){
        return timestamp;
    }

    public String getGameId() {
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

    public int getTeam() {
        return team;
    }

    public boolean getIsUndo() { return isUndo; }
}

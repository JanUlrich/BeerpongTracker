package kuckuck.de.statisticallydrinking.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Game {
    public Game(String timestamp){
        this.timestamp = timestamp;
    }

    @PrimaryKey
    @NonNull
    private String timestamp;

    public String getTimestamp() {
        return timestamp;
    }
}

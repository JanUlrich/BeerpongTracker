package kuckuck.de.statisticallydrinking.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Game {
    @PrimaryKey
    private int timestamp;
}

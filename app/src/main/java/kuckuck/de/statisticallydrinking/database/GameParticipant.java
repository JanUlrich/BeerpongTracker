package kuckuck.de.statisticallydrinking.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class GameParticipant {
    @PrimaryKey
    private String playerName;
}

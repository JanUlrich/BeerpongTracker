package kuckuck.de.statisticallydrinking.model;

import android.arch.persistence.room.ColumnInfo;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Game {
    private final String gameID;
    HashMap<String, HitCount> hits = new HashMap<>();

    public Game(String gameId){
        this.gameID = gameId;
    }

    public String getGameId(){
        return gameID;
    }

    public static String newGameID() {
        return Long.toString(System.currentTimeMillis());
    }

    public List<String> getParticipants(){
        return Arrays.asList(hits.keySet().toArray(new String[0]));
    }

    public HitCount getHitCount(String forPlayer, HitCount defaultHitcount) {
        if(!hits.containsKey(forPlayer))hits.put(forPlayer, defaultHitcount);
        return hits.get(forPlayer);
    }

    public HitCount addHit(int cupNum, String forPlayer, HitCount defaultHitcount) {
        HitCount ret = getHitCount(forPlayer, defaultHitcount);
        ret.addHit(cupNum);
        hits.put(forPlayer, ret);
        return ret;
    }

    public void addPlayer(Player player) {
        hits.put(player.getName(), new HitCount());
    }
}

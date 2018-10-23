package kuckuck.de.statisticallydrinking.model;

import android.arch.persistence.room.ColumnInfo;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import kuckuck.de.statisticallydrinking.database.Hit;

public class Game implements Identifiable{
    private final String gameID;
    HashMap<String, HitCount> hits = new HashMap<>();
    List<Player> team1 = new ArrayList<>();
    List<Player> team2 = new ArrayList<>();
    private String gameName;

    public Game(String gameId){
        this.gameID = gameId;

        DateFormat df = new SimpleDateFormat("HH:mm");
        gameName = ("Game from "+df.format(Long.valueOf(getGameId())) + "h");
    }

    public String getGameId(){
        return gameID;
    }

    public String getGameName(){
        return gameName;
    }

    public static String newGameID() {
        return Long.toString(System.currentTimeMillis());
    }

    public List<Player> getTeam1(){
        //return Arrays.asList(hits.keySet().toArray(new String[0]));
        return team1;
    }

    public List<Player> getTeam2(){
        //return Arrays.asList(hits.keySet().toArray(new String[0]));
        return team2;
    }

    public HitCount getHitCount(String forPlayer) {
        if(!hits.containsKey(forPlayer))hits.put(forPlayer, new HitCount());
        return hits.get(forPlayer);
    }

    public HitCount addHit(int cupNum, String forPlayer) {
        HitCount ret = getHitCount(forPlayer);
        ret.addHit(cupNum);
        //hits.put(forPlayer, ret);
        return ret;
    }

    public void addPlayerTeam1(Player player) {
        if(!team1.contains(player))team1.add(player);
        if(team2.contains(player))
            team2.remove(player);
    }

    public void addPlayerTeam2(Player player) {
        if(!team2.contains(player))team2.add(player);
        if(team1.contains(player))
            team1.remove(player);
    }

    @Override
    public String getId() {
        return getGameId();
    }

    public void setName(String name) {
        this.gameName = name;
    }

}

package kuckuck.de.statisticallydrinking.model;

import java.util.HashMap;

public class Game {
    HashMap<String, HitCount> hits = new HashMap<>();

    public Game(){

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

}

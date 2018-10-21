package kuckuck.de.statisticallydrinking.model;

import java.util.ArrayList;
import java.util.HashMap;

public class HitCount {

    private HashMap<Integer,Integer> hitCounts = new HashMap<>();

    public HitCount(){
    }

    /**
     * @return hitrate between 0 and 1 (0 = no hit, 1 = every shot was a hit)
     */
    public double calculateHitRate() {
        double hits = 0;
        for(Integer i : hitCounts.keySet()){
            if(i != -1)hits += getHits(i);
        }
        double hitRate;
        double misses = getHits(-1);
        if(misses+hits > 0)
            hitRate = hits/(hits+misses);
        else hitRate = 0;
        return hitRate;
    }

    public int getHits(int cupNum) {
        if(!hitCounts.containsKey(cupNum))return 0;
        return hitCounts.get(cupNum);
    }

    public void addHit(int cupNum) {
        hitCounts.put(cupNum, getHits(cupNum) + 1);
    }
}

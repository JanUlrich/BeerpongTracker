package kuckuck.de.statisticallydrinking.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class HitCount {


    private Stack<Integer> hits = new Stack<>();

    public HitCount(){
    }

    /**
     * @return hitrate between 0 and 1 (0 = no hit, 1 = every shot was a hit)
     */
    public double calculateHitRate() {
        double numHits = 0;
        double misses = 0;
        for(Integer hit : hits){
            if(hit != -1)numHits ++;
            else misses++;
        }
        double hitRate;
        if(misses+numHits > 0)
            hitRate = numHits/(numHits+misses);
        else hitRate = 0;
        return hitRate;
    }

    public int getHits(int cupNum) {
        int totalCount = 0;
        for(Integer hit : hits){
            if(hit == cupNum){
                totalCount++;
            }
        }
        return totalCount;
    }

    public void addHit(int cupNum) {
        hits.push(cupNum);
    }

    public Integer removeLastHit() {
        if(hits.size() == 0) return null;
        return hits.pop();
    }

}

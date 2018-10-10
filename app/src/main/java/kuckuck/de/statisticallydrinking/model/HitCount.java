package kuckuck.de.statisticallydrinking.model;

public class HitCount {

    private int[] hitCounts;

    public HitCount(int numCups){
        hitCounts = new int[numCups];
    }

    /**
     * @return hitrate between 0 and 1 (0 = no hit, 1 = every shot was a hit)
     */
    public double calculateHitRate() {
        double hits = 0;
        for(int i=0; i<hitCounts.length-1;i++){
            hits += hitCounts[i];
            }
        double hitRate;
        if(hitCounts[hitCounts.length-1]+hits > 0)
            hitRate = hits/(hits+hitCounts[hitCounts.length-1]);
        else hitRate = 0;
        return hitRate;
    }

    public int getHits(int cupNum) {
        if(cupNum >= hitCounts.length)return 0;
        return hitCounts[cupNum];
    }

    public void addHit(int cupNum) {
        hitCounts[cupNum]++;
    }
}

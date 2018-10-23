package kuckuck.de.statisticallydrinking.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import kuckuck.de.statisticallydrinking.Database;
import kuckuck.de.statisticallydrinking.HitCounter;
import kuckuck.de.statisticallydrinking.R;
import kuckuck.de.statisticallydrinking.database.Hit;
import kuckuck.de.statisticallydrinking.model.HitCount;
import kuckuck.de.statisticallydrinking.model.Player;

public class PlayerListItem extends LinearLayout
{
    private View v;
    private TextView playerName;
    private TextView hitrate;
    private TextView totalHitrate;
    private TextView thrownBalls;
    private final String gameId;

    public PlayerListItem(String gameId, Context context)
    {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        v = inflater.inflate(R.layout.player_list_item, this, true);
        playerName = v.findViewById(R.id.playerName);
        totalHitrate = v.findViewById(R.id.totalHitrate);
        hitrate = v.findViewById(R.id.hitRate);
        thrownBalls = v.findViewById(R.id.shotsFired);
        this.gameId = gameId;
    }

    public PlayerListItem(Context context)
    {
        this(null, context);
    }

    public void setPlayer(Player wp, Context context)
    {
        if(gameId != null){
            Database.getHits(wp.getName(), gameId, context,
                    (hits)->{
                        hitrate.setText("HitRate: "+getHitrate(hits));
                        thrownBalls.setText("Throws: "+hits.size());
                        return null;
                    });
        }
        Database.getHits(wp.getName(), context,
                (hits)->{
                    totalHitrate.setText("Total: "+getHitrate(hits));
                    return null;
                });

        playerName.setText(wp.getName());

    }

    private String getHitrate(List<Hit> hits){
        HitCount hitCount = new HitCount();
        for(Hit hit : hits)
            hitCount.addHit(hit.getCup());
        return HitCounter.toPercent(hitCount.calculateHitRate());
    }
}
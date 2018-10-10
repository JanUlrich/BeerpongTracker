package kuckuck.de.statisticallydrinking;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import kuckuck.de.statisticallydrinking.model.Game;
import kuckuck.de.statisticallydrinking.model.HitCount;

public class HitCounter extends AppCompatActivity {

    private String playerName;
    private int gameNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hit_counter);
        playerName = "Me, Myself";
        gameNum = 0;

        Resources res = getResources();
        TypedArray buttons = res.obtainTypedArray(R.array.cup_buttons);
        for(int i = 0; i<getNumButtons() ;i++){
            final int cupNum = i;
            int buttonID = buttons.getResourceId(i,-1);
            final Button button = findViewById(buttonID);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        HitCount count = addHit(cupNum);
                        button.setText(Integer.toString(count.getHits(cupNum)));
                        setHitRate(count.calculateHitRate());
                    } catch (IOException e) {
                    }
                }
            });
            try {
                HitCount count = getHitCount();
                int cupCount = count.getHits(cupNum);
                button.setText(Integer.toString(cupCount));
                setHitRate(count.calculateHitRate());
            } catch (IOException e) {
            }
        }
    }

    private void setHitRate(double hitRate) throws IOException {
        TextView statsView = findViewById(R.id.stats);
        statsView.setText("Hitrate: "+ round(hitRate*100, 2) + "%");
    }

    private HitCount getHitCount() throws IOException {
        return Storage.getGame(getGameName(), getApplicationContext())
                .getHitCount(playerName, new HitCount(getNumButtons()));
    }

    private String getGameName() {
        return Integer.toString(gameNum);
    }

    private HitCount addHit(int hit) throws IOException {
        Game game = Storage.getGame(getGameName(), getApplicationContext());
        HitCount newValue = game.addHit(hit, playerName, new HitCount(getNumButtons()));
        Storage.saveGame(getGameName(), game, getApplicationContext());
        return newValue;
    }

    private int getNumButtons(){
        return getResources().obtainTypedArray(R.array.cup_buttons).length();
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}

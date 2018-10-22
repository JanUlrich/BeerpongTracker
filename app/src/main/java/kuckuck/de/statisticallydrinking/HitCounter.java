package kuckuck.de.statisticallydrinking;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import kuckuck.de.statisticallydrinking.model.Game;
import kuckuck.de.statisticallydrinking.model.HitCount;
import kuckuck.de.statisticallydrinking.model.Player;

public class HitCounter extends AppCompatActivity {

    private String playerID;
    private String gameID;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.hitmenu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hit_counter);
        //from: https://developer.android.com/training/appbar/
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        String playerName = "Me, Myself";
        playerID = (playerName);
        gameID = "0";

        setTitle(playerName);

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
        try {
            HitCount count = getHitCount();
            int cupCount = count.getHits(-1);
            ((Button)findViewById(R.id.miss)).setText(Integer.toString(cupCount));
            setHitRate(count.calculateHitRate());
        } catch (IOException e) {
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_newGame:
                newGame();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void newGame() {
        Intent intent = new Intent(this, GameSettings.class);
        intent.putExtra(getString(R.string.extra_game), Game.newGameID());
        startActivity(intent);
    }

    public void addMiss(View view) throws IOException {
        HitCount count = addHit(-1);
        ((Button)findViewById(R.id.miss)).setText(Integer.toString(count.getHits(-1)));
    }

    private void setHitRate(double hitRate) throws IOException {
        TextView statsView = findViewById(R.id.stats);
        statsView.setText("Hitrate: "+ round(hitRate*100, 2) + "%");
    }

    private HitCount getHitCount() throws IOException {
        return Storage.getGame(getGameName(), getApplicationContext())
                .getHitCount(playerID, new HitCount());
    }

    private String getGameName() {
        return gameID;
    }

    private HitCount addHit(int hit) throws IOException {
        Game game = Storage.getGame(getGameName(), getApplicationContext());
        HitCount newValue = game.addHit(hit, playerID, new HitCount());
        Storage.saveGame(getGameName(), game, getApplicationContext());
        setHitRate(newValue.calculateHitRate());
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

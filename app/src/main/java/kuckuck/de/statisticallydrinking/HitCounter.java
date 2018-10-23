package kuckuck.de.statisticallydrinking;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.location.Location;
import android.provider.ContactsContract;
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
    private boolean finishOnDone = false;

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

        String playerName = "Me, Myself";//this.getResources().getString(R.string.standard_player_name);
        gameID = "0";
        try {
             if(!Storage.hasGame(gameID, this)){
                 Storage.savePlayer(new Player(playerName), this);
                 Game standardGame = new Game(gameID);
                 standardGame.setName("Standard Game");
                 standardGame.addPlayerTeam1(new Player(playerName));
                 Storage.saveGame(standardGame, this);
             }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = getIntent();
        if(intent != null){
            if(intent.getStringExtra(getString(R.string.selectedPlayer)) != null &&
                            intent.getStringExtra(getString(R.string.selectedPlayer)).length() > 0){
                playerName = intent.getStringExtra(getString(R.string.selectedPlayer));
                gameID = intent.getStringExtra(getString(R.string.extra_game));
            }
        }

        if(!playerName.equals("Me, Myself")) //Resources.getSystem().getString(R.string.standard_player_name))
            finishOnDone = true; //Only when not shooting for myself

        playerID = (playerName);

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
                        done();
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
            case R.id.action_gamelist:
                gameList();
                done();
                return true;

            case R.id.action_newGame:
                try {
                    newGame();
                    done();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void gameList(){
        Intent intent = new Intent(this, GameList.class);
        startActivity(intent);
    }

    private void newGame() throws IOException {
        Intent intent = new Intent(this, GameSettings.class);
        Game game = new Game(Game.newGameID());
        Storage.saveGame(game, getApplicationContext());
        intent.putExtra(getString(R.string.extra_game), game.getId());
        startActivity(intent);
    }

    private void done() {
        if(finishOnDone)finish();
    }

    public void addMiss(View view) throws IOException {
        HitCount count = addHit(-1);
        ((Button)findViewById(R.id.miss)).setText(Integer.toString(count.getHits(-1)));
        done();
    }

    private void setHitRate(double hitRate) throws IOException {
        TextView statsView = findViewById(R.id.stats);
        statsView.setText("Hitrate: "+ round(hitRate*100, 2) + "%");
    }

    private HitCount getHitCount() throws IOException {
        return Storage.getGame(getGameName(), getApplicationContext())
                .getHitCount(playerID);
    }

    private String getGameName() {
        return gameID;
    }

    private HitCount addHit(int hit) throws IOException {
        Game game = Storage.getGame(getGameName(), getApplicationContext());
        HitCount newValue = game.addHit(hit, playerID);
        setHitRate(newValue.calculateHitRate());

        Storage.saveGame(game, getApplicationContext());
        Database.saveHit(this.playerID, hit, Long.valueOf(this.gameID), getApplicationContext());

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

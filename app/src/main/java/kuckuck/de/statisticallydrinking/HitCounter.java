package kuckuck.de.statisticallydrinking;

import android.arch.persistence.room.PrimaryKey;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import kuckuck.de.statisticallydrinking.model.AppState;
import kuckuck.de.statisticallydrinking.model.Game;
import kuckuck.de.statisticallydrinking.model.HitCount;
import kuckuck.de.statisticallydrinking.model.Player;

public class HitCounter extends AppCompatActivity {

    private String playerID;
    private String gameID;
    private boolean finishOnDone = false;
    private int team;
    private AppState appState;
    private Toolbar myToolbar;
    private Game game;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.hitmenu, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.getItem(0).setVisible(!finishOnDone);
        menu.getItem(0).setEnabled(!finishOnDone);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            appState = Storage.getAppState(this);
        } catch (IOException e) {
            e.printStackTrace();
            finish();
        }
        //If standard game has changed delete this action
        if (game.isStandardGame() && !appState.getCurrentGame().equals(gameID)) {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hit_counter);
        //from: https://developer.android.com/training/appbar/
        myToolbar = (Toolbar) findViewById(R.id.hitcounterMenu);
        setSupportActionBar(myToolbar);

        //The standard
        String playerName = this.getResources().getString(R.string.standard_player_name);
        gameID = null;
        //Which gets changed, when Intent got called from GameSettings:
        Intent intent = getIntent();
        if(intent != null &&
                intent.getStringExtra(getString(R.string.selectedPlayer)) != null &&
                intent.getStringExtra(getString(R.string.selectedPlayer)).length() > 0){
            playerName = intent.getStringExtra(getString(R.string.selectedPlayer));
            gameID = intent.getStringExtra(getString(R.string.extra_game));
        }
        try {
            appState = Storage.getAppState(this);
            if(gameID == null){
                if(appState == null){//First startup:
                    Game game = createStandardGame();
                    appState = new AppState(game.getGameId());
                    Storage.saveAppState(appState, this);
                }
                gameID = appState.getCurrentGame();
            }
        } catch (IOException e) {
            e.printStackTrace();
            finish();
        }
        //Initialize fields:
        try {
            game = Storage.getGame(gameID, this);
            if(game.getTeam1().contains(Storage.getPlayer(playerName))){
                team = 1;
            }else{
                team = 2;
            }
            finishOnDone = ! game.isStandardGame();

            myToolbar.setSubtitle(game.getGameName());
        } catch (IOException e) {
            e.printStackTrace();
        }


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
            case R.id.action_refresh_standard_game:
                try {
                    Game game = createStandardGame();
                    appState.setCurrentGame(game.getGameId());
                    Storage.saveAppState(appState, this);
                    newGame(game);
                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.action_gamelist:
                gameList();
                done();
                return true;

            case R.id.action_newGame:
                try {
                    newGame(new Game(Game.newGameID()));
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

    private Game createStandardGame() throws IOException {
        String playerName = this.getResources().getString(R.string.standard_player_name);
        Storage.savePlayer(new Player(playerName), this);
        Game standardGame = new Game(Game.newGameID(), true);
        DateFormat df = new SimpleDateFormat("HH:mm");
        standardGame.setName("Solo Game from "+df.format(System.currentTimeMillis())+"h");
        standardGame.addPlayerTeam1(new Player(playerName));
        Storage.saveGame(standardGame, this);
        return standardGame;
    }

    private void newGame(Game game) throws IOException {
        Intent intent = new Intent(this, GameSettings.class);
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
        statsView.setText("Hitrate: "+toPercent(hitRate));
    }

    public static String toPercent(double hitRate) {
        return round(hitRate*100, 2) + "%";
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
        Database.saveHit(this.playerID, hit, this.gameID, team, getApplicationContext());

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

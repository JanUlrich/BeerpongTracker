package kuckuck.de.statisticallydrinking;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kuckuck.de.statisticallydrinking.model.Game;
import kuckuck.de.statisticallydrinking.model.Player;
import kuckuck.de.statisticallydrinking.view.PlayerArrayAdapter;

public class GameSettings extends AppCompatActivity {
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_settings);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        try{
            Intent intent = getIntent();
            String gameName = intent.getStringExtra(getString(R.string.extra_game));
            game = Storage.getGame(gameName, getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        initializePlayerList();
        setTitle(game.getGameName());
    }

    private void initializePlayerList(){
        PlayerArrayAdapter adapter = new PlayerArrayAdapter(this,
                -1, game.getTeam1());

        ListView listView = findViewById(R.id.team1);
        listView.setAdapter(adapter);
        PlayerArrayAdapter adapter2 = new PlayerArrayAdapter(this,
                -1, game.getTeam2());

        ListView listView2 = findViewById(R.id.team2);
        listView2.setAdapter(adapter2);
    }

    public void addPlayer1(View view) {
        Intent intent = new Intent(this, AddPlayer.class);
        startActivityForResult(intent, getResources().getInteger(R.integer.add_player1_intent_flag));
    }

    public void addPlayer2(View view) {
        Intent intent = new Intent(this, AddPlayer.class);
        startActivityForResult(intent, getResources().getInteger(R.integer.add_player2_intent_flag));
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == getResources().getInteger(R.integer.add_player1_intent_flag) ||
                requestCode == getResources().getInteger(R.integer.add_player2_intent_flag)) {
            if (resultCode == RESULT_OK) {
                String selectedPlayer = data.getStringExtra(getString(R.string.selectedPlayer));
                if(requestCode == getResources().getInteger(R.integer.add_player1_intent_flag) ){
                    game.addPlayerTeam1(Storage.getPlayer(selectedPlayer));
                }else{
                    game.addPlayerTeam2(Storage.getPlayer(selectedPlayer));
                }
                try {
                    Storage.saveGame(game.getGameId(), game, getApplicationContext());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                initializePlayerList();
            }
        }
    }
}

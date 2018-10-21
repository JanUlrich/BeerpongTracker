package kuckuck.de.statisticallydrinking;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
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
        try{
            Intent intent = getIntent();
            String gameName = intent.getStringExtra(getString(R.string.extra_game));
            game = Storage.getGame(gameName, getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        initializePlayerList();
    }

    private void initializePlayerList(){
        List<Player> participants = new ArrayList<>();
        for(String playerID : game.getParticipants()){
            participants.add(Storage.getPlayer(playerID));
        }
        PlayerArrayAdapter adapter = new PlayerArrayAdapter(this,
                -1, participants);

        ListView listView = findViewById(R.id.participants);
        listView.setAdapter(adapter);
    }

    public void addPlayer(View view) {
        Intent intent = new Intent(this, AddPlayer.class);
        startActivityForResult(intent, getResources().getInteger(R.integer.add_player_intent_flag));
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == getResources().getInteger(R.integer.add_player_intent_flag)) {
            if (resultCode == RESULT_OK) {
                String selectedPlayer = data.getStringExtra(getString(R.string.selectedPlayer));
                game.addPlayer(Storage.getPlayer(selectedPlayer));
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

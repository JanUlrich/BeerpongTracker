package kuckuck.de.statisticallydrinking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import kuckuck.de.statisticallydrinking.model.Game;
import kuckuck.de.statisticallydrinking.model.Player;
import kuckuck.de.statisticallydrinking.view.PlayerArrayAdapter;

public class GameSettings extends AppCompatActivity {
    private String gameId;
    private PlayerArrayAdapter adapter1;
    private PlayerArrayAdapter adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_settings);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        try{
            Intent intent = getIntent();
            gameId = intent.getStringExtra(getString(R.string.extra_game));
            Game game = Storage.getGame(gameId, getApplicationContext());
            if(game.isStandardGame()){
                playerShoots(game.getTeam1().get(0));
                finish();
            }
            initializePlayerList(game);
            setTitle(game.getGameName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter1.notifyDataSetChanged();
        adapter2.notifyDataSetChanged();
    }

    private void initializePlayerList(Game game) {
        adapter1 = new PlayerArrayAdapter(gameId, this,
                -1, game.getTeam1());

        ListView listView = findViewById(R.id.team1);
        listView.setAdapter(adapter1);
        adapter2 = new PlayerArrayAdapter(gameId,this,
                -1, game.getTeam2());

        ListView listView2 = findViewById(R.id.team2);
        listView2.setAdapter(adapter2);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Player player = adapter1.getItem(position);
                playerShoots(player);
            }
        });
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Player player = adapter2.getItem(position);
                playerShoots(player);
            }
        });
    }

    private void playerShoots(Player player){
        Intent intent = new Intent(this, HitCounter.class);
        intent.putExtra(getString(R.string.selectedPlayer), player.getName());
        intent.putExtra(getString(R.string.extra_game), gameId);
        startActivity(intent);
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
                try {
                    Game game = Storage.getGame(gameId, getApplicationContext());
                    String selectedPlayer = data.getStringExtra(getString(R.string.selectedPlayer));
                    if(requestCode == getResources().getInteger(R.integer.add_player1_intent_flag) ){
                        game.addPlayerTeam1(Storage.getPlayer(selectedPlayer));
                    }else{
                        game.addPlayerTeam2(Storage.getPlayer(selectedPlayer));
                    }
                    Storage.saveGame(game, getApplicationContext());
                    initializePlayerList(game);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

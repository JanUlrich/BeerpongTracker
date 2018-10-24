package kuckuck.de.statisticallydrinking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;

import kuckuck.de.statisticallydrinking.model.AppState;
import kuckuck.de.statisticallydrinking.model.Game;
import kuckuck.de.statisticallydrinking.model.Player;
import kuckuck.de.statisticallydrinking.view.GameArrayAdapter;
import kuckuck.de.statisticallydrinking.view.PlayerArrayAdapter;

public class GameList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        setTitle("Games");

        try {
            final GameArrayAdapter adapter = new GameArrayAdapter(this,
                    -1, Storage.getGames(getApplicationContext()));

            ListView listView = findViewById(R.id.games);
            listView.setAdapter(adapter);
            final GameList context = this;
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Game game = adapter.getItem(position);
                    if(game.isStandardGame()){ //If it is a standard game
                        try {//... this will get the new standard game:
                            AppState appState = Storage.getAppState(context);
                            appState.setCurrentGame(game.getGameId());
                            Storage.saveAppState(appState, context);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Intent intent = new Intent(context, GameSettings.class);
                    intent.putExtra(getString(R.string.extra_game), game.getGameId());
                    startActivity(intent);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            finish();
        }

    }
}

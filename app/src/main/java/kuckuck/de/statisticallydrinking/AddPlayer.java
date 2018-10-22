package kuckuck.de.statisticallydrinking;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;

import kuckuck.de.statisticallydrinking.model.Player;
import kuckuck.de.statisticallydrinking.view.PlayerArrayAdapter;

public class AddPlayer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);

        final PlayerArrayAdapter adapter;
        try {
            adapter = new PlayerArrayAdapter(this,
                    -1, Storage.getPlayers(getApplicationContext()));

            ListView listView = findViewById(R.id.playerList);
            listView.setAdapter(adapter);
            EditText search = findViewById(R.id.playerSearch);

            // Add Text Change Listener to EditText
            search.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // Call back the Adapter with current character to Filter
                    adapter.getFilter().filter(s.toString());
                    if(adapter.isEmpty() && s.toString().length() > 0){
                        findViewById(R.id.createPlayer).setEnabled(true);
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Player player = adapter.getItem(position);
                    returnPlayer(player);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void newPlayer(View view) throws IOException {
        String name = ((EditText) findViewById(R.id.playerSearch)).getText().toString();
        Player newPlayer = new Player(name);
        Storage.savePlayer(newPlayer, getApplicationContext());
        returnPlayer(newPlayer);
    }

    private void returnPlayer(Player p){
        Intent resultData = new Intent();
        resultData.putExtra(getString(R.string.selectedPlayer), p.getName());
        setResult(Activity.RESULT_OK, resultData);
        finish();
    }
}

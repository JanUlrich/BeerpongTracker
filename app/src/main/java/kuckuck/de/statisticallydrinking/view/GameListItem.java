package kuckuck.de.statisticallydrinking.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;

import kuckuck.de.statisticallydrinking.R;
import kuckuck.de.statisticallydrinking.model.Game;
import kuckuck.de.statisticallydrinking.model.Player;

public class GameListItem extends LinearLayout
{
    private View v;
    private TextView value;
    private TextView team1;
    private TextView team2;

    public GameListItem(Context context)
    {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        v = inflater.inflate(R.layout.game_list_item, this, true);
        value = v.findViewById(R.id.gameName);
        team1 = v.findViewById(R.id.team1);
        team2 = v.findViewById(R.id.team2);
    }

    public void setGame(Game wp)
    {
        value.setText(wp.getGameName());
        team1.setText(listPlayers(wp.getTeam1()));
        team2.setText(listPlayers(wp.getTeam2()));
    }

    private String listPlayers(List<Player> playerList){
        String ret = "";
        Iterator<Player> it = playerList.iterator();
        while (it.hasNext()){
            ret+=it.next().getName();
            if(it.hasNext())ret+=", ";
        }
        return ret;
    }
}
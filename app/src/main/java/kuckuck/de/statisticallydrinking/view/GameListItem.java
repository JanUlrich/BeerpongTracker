package kuckuck.de.statisticallydrinking.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import kuckuck.de.statisticallydrinking.R;
import kuckuck.de.statisticallydrinking.model.Game;
import kuckuck.de.statisticallydrinking.model.Player;

public class GameListItem extends LinearLayout
{
    private View v;
    private TextView value;

    public GameListItem(Context context)
    {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        v = inflater.inflate(R.layout.game_list_item, this, true);
        value = v.findViewById(R.id.gameName);
    }

    public void setGame(Game wp)
    {
        value.setText(wp.getGameName());
    }

}
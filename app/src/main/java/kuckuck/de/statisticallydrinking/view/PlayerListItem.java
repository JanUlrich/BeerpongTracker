package kuckuck.de.statisticallydrinking.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;

import kuckuck.de.statisticallydrinking.R;
import kuckuck.de.statisticallydrinking.model.Player;

public class PlayerListItem extends LinearLayout
{
    private View v;
    private TextView value;

    public PlayerListItem(Context context)
    {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        v = inflater.inflate(R.layout.player_list_item, this, true);
        value = v.findViewById(R.id.playerName);
    }

    public void setPlayer(Player wp)
    {
        value.setText(wp.getName());
    }

}
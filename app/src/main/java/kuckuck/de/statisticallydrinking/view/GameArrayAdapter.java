package kuckuck.de.statisticallydrinking.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import kuckuck.de.statisticallydrinking.model.Game;

public class GameArrayAdapter extends ArrayAdapter<Game>
{
    public GameArrayAdapter(Context context, int resource, List<Game> objects)
    {
    super(context, resource, objects);
    }
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if(convertView == null)
            convertView = new GameListItem(getContext());

            Game g = getItem(position);
            GameListItem packView = (GameListItem) convertView;
            packView.setGame(g);

            return convertView;
        }
}
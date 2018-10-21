package kuckuck.de.statisticallydrinking.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.List;

import kuckuck.de.statisticallydrinking.model.Player;

public class PlayerArrayAdapter extends ArrayAdapter<Player>implements Filterable
{

    public PlayerArrayAdapter(Context context, int resource, List<Player> objects)
    {
        super(context, resource, objects);
    }

    public boolean isEmpty(){
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView == null)
            convertView = new PlayerListItem(getContext());

        Player p = getItem(position);
        PlayerListItem packView = (PlayerListItem) convertView;
        packView.setPlayer(p);

        return convertView;
    }

    //From: https://stackoverflow.com/questions/14118309/how-to-use-search-functionality-in-custom-list-view-in-android
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                return null;
            }
        };
        return filter;
    }

}
package kuckuck.de.statisticallydrinking.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import kuckuck.de.statisticallydrinking.model.Player;

public class PlayerArrayAdapter extends ArrayAdapter<Player>implements Filterable
{

    ArrayList<Player> data;
    ArrayList<Player> originalValues;
    private final String gameId;

    public PlayerArrayAdapter(Context context, int resource, List<Player> objects)
    {
        this(null, context, resource, objects);
    }

    public PlayerArrayAdapter(String gameId, Context context, int resource, List<Player> objects)
    {
        super(context, resource, objects);
        data = new ArrayList<>(objects);
        originalValues = new ArrayList<>(objects);
        this.gameId = gameId;
    }

    public boolean isEmpty(){
        return true;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Player getItem(int position){
        return data.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView == null)
            convertView = new PlayerListItem(gameId, getContext());

        Player p = data.get(position);//getItem(position);
        PlayerListItem packView = (PlayerListItem) convertView;
        packView.setPlayer(p, parent.getContext());

        return convertView;
    }

    //From: https://stackoverflow.com/questions/14118309/how-to-use-search-functionality-in-custom-list-view-in-android
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {
                data = (ArrayList<Player>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<Player> FilteredArrList = new ArrayList<>();


                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = originalValues.size();
                    results.values = originalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < originalValues.size(); i++) {
                        String playerName = originalValues.get(i).getName();
                        if (playerName.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(originalValues.get(i));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }

}
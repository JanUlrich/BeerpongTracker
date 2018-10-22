package kuckuck.de.statisticallydrinking.model;

import android.content.res.Resources;

import kuckuck.de.statisticallydrinking.R;

/*
Hier könnte man speichern, in welchem game der Spieler zuletzt mit Me, Myself gespielt hat
 */
public class AppState implements Identifiable {
    String currentGame;

    public AppState(String currentGame){
        this.currentGame = currentGame;
    }

    public String getCurrentGame(){
        return currentGame;
    }

    @Override
    public String getId() {
        return Resources.getSystem().getString(R.string.app_stat_id);
    }
}

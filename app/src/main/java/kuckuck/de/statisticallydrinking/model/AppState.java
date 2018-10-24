package kuckuck.de.statisticallydrinking.model;

import android.content.res.Resources;

import kuckuck.de.statisticallydrinking.R;

/*
Hier könnte man speichern, in welchem game der Spieler zuletzt mit Me, Myself gespielt hat
 */
public class AppState implements Identifiable {
    String currentGame;
    private int numNewGames = 0;

    public AppState(String currentGame){
        this.currentGame = currentGame;
    }

    public String getCurrentGame(){
        return currentGame;
    }

    @Override
    public String getId() {
        return "appState.data";
    }

    public void setCurrentGame(String gameId) {
        this.currentGame = gameId;
    }


    public int getNumNewGames() {
        return numNewGames;
    }

    public void setNumNewGames(int numNewGames) {
        this.numNewGames = numNewGames;
    }
}

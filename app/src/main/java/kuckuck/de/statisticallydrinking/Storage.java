package kuckuck.de.statisticallydrinking;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import kuckuck.de.statisticallydrinking.model.Game;
import kuckuck.de.statisticallydrinking.model.Player;

public class Storage {
    public static List<Player> getPlayers(){
        return null;
    }

    public static void savePlayer(Player pl){

    }

    public static Game getGame(String gameName, Context context) throws IOException {
        File path = new File(getGameDir(context), gameName);
        if(!path.exists())return new Game();
        Gson gson = new Gson();
        Game ret = gson.fromJson(getFileData(path), Game.class);
        if(ret == null)throw new IOException();
        return ret;
    }

    private static File getGameDir(Context context) {
        File folder = new File(context.getFilesDir() +
                File.separator + context.getString(R.string.game_directory));
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }

    public static void saveGame(String name, Game game, Context context) throws IOException {
        String filename = name;
        Gson gson = new Gson();
        FileOutputStream outputStream = new FileOutputStream(
                getGameDir(context).getAbsolutePath() + File.separator + filename);
        outputStream.write(gson.toJson(game).getBytes());
        outputStream.close();
    }

    private static String getFileData(File file) throws IOException {
        StringBuilder text = new StringBuilder();

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;

        while ((line = br.readLine()) != null) {
            text.append(line);
            text.append('\n');
        }
        br.close();

        return text.toString();
    }
}

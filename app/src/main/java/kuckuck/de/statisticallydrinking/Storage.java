package kuckuck.de.statisticallydrinking;

import android.content.Context;
import android.content.res.Resources;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import kuckuck.de.statisticallydrinking.model.Game;
import kuckuck.de.statisticallydrinking.model.Identifiable;
import kuckuck.de.statisticallydrinking.model.Player;

public class Storage {
    public static void savePlayer(Player pl, Context context) throws IOException {
        saveClass(pl, getPlayerDir(context).getAbsolutePath(), context);
    }

    public static Game getGame(String gameID, Context context) throws IOException {
        return getSavedClass(new File(getGameDir(context), gameID), Game.class);
    }

    private static File getGameDir(Context context) {
        File folder = new File(context.getFilesDir() +
                File.separator + context.getString(R.string.game_directory));
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }

    private static File getPlayerDir(Context context) {
        File folder = new File(context.getFilesDir() +
                File.separator + context.getString(R.string.player_directory));
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }

    public static void saveGame(Game game, Context context) throws IOException {
        saveClass(game, getGameDir(context).getAbsolutePath(), context);
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

    public static Player getPlayer(String playerID) {
        return new Player(playerID);
    }

    public static List<Player> getPlayers(Context ctx) throws IOException {
        return getSavedClasses(ctx, getPlayerDir(ctx), Player.class);
    }

    public static List<Game> getGames(Context ctx) throws IOException {
        return getSavedClasses(ctx, getGameDir(ctx), Game.class);
    }

    private static <T extends Identifiable> void saveClass(T data, String folder, Context context) throws IOException {
        Gson gson = new Gson();
        String jsonData = gson.toJson(data);
        String filename = data.getId();//Integer.toString(jsonData.hashCode());
        FileOutputStream outputStream = new FileOutputStream(
                folder + File.separator + filename);
        outputStream.write(jsonData.getBytes());
        outputStream.close();
    }

    private static <T> List<T> getSavedClasses(Context context, File dir, Class<T> tClass) throws IOException {
        List<T> ret = new ArrayList<>();
        for(File f : Storage.getListFiles(dir)){
            //Waypoint wp = Waypoint.fromJSON(getFileData(f));
            T wp  = getSavedClass(f, tClass);
            ret.add(wp);
        }
        return ret;
    }

    private static <T> T getSavedClass(File f, Class<T> tClass) throws IOException {
        Gson gson = new Gson();
        T ret = gson.fromJson(getFileData(f), tClass);
        if(ret == null)throw new IOException();
        return ret;
    }

    private static List<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                inFiles.addAll(getListFiles(file));
            } else {
                inFiles.add(file);
            }
        }
        return inFiles;
    }

}

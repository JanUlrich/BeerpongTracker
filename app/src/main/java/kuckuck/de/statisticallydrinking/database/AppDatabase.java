package kuckuck.de.statisticallydrinking.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import kuckuck.de.statisticallydrinking.R;

@Database(entities = {Game.class, Hit.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    AppDatabase db = Room.databaseBuilder(context,
                            AppDatabase.class, context.getResources().getString(R.string.database_name)).fallbackToDestructiveMigration().build();
                    INSTANCE = db;
                }
            }
        }
        return INSTANCE;
    }

    public abstract HitDao hitDao();
}

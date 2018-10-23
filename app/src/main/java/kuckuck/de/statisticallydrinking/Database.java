package kuckuck.de.statisticallydrinking;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import kuckuck.de.statisticallydrinking.database.AppDatabase;
import kuckuck.de.statisticallydrinking.database.Hit;
import kuckuck.de.statisticallydrinking.database.HitDao;

public class Database {
    public static void saveHit(String forPlayer, int cupNum, long gameId, Context context){
        AppDatabase db = AppDatabase.getDatabase(context);
        new insertAsyncTask(db.hitDao()).execute(new Hit(System.currentTimeMillis(), forPlayer, cupNum, 10, gameId));
    }

    private static class insertAsyncTask extends AsyncTask<Hit, Void, Void> {

        private HitDao mAsyncTaskDao;

        insertAsyncTask(HitDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Hit... params) {
            mAsyncTaskDao.insertAll(params);
            return null;
        }
    }
}

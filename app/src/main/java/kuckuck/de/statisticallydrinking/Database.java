package kuckuck.de.statisticallydrinking;

import android.arch.core.util.Function;
import android.content.Context;
import android.os.AsyncTask;

import java.util.Arrays;
import java.util.List;

import kuckuck.de.statisticallydrinking.database.AppDatabase;
import kuckuck.de.statisticallydrinking.database.Hit;
import kuckuck.de.statisticallydrinking.database.HitDao;

public class Database {
    public static void saveHit(String forPlayer, int cupNum, String gameId, int team, Context context){
        AppDatabase db = AppDatabase.getDatabase(context);
        new insertAsyncTask(db.hitDao()).execute(new Hit(System.currentTimeMillis(), forPlayer, cupNum, 10, gameId, team));
    }

    public static void getHits(String name, Context context, Function<List<Hit>, Void> onResult) {
        AppDatabase db = AppDatabase.getDatabase(context);
        AsyncTask<String, Void, Hit[]> task = new getAsyncTask(db.hitDao(), onResult).execute(name);
    }

    public static void getHits(String name, String gameId, Context context, Function<List<Hit>, Void> onResult) {
        AppDatabase db = AppDatabase.getDatabase(context);
        AsyncTask<String, Void, Hit[]> task = new getAsyncTask(db.hitDao(), onResult).execute(name, gameId);
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
    private static class getAsyncTask extends AsyncTask<String, Void, Hit[]> {

        private final Function<List<Hit>, Void> onResult;
        private HitDao mAsyncTaskDao;

        getAsyncTask(HitDao dao, Function<List<Hit>, Void> onResult) {
            mAsyncTaskDao = dao;
            this.onResult = onResult;
        }

        @Override
        protected void onPostExecute(Hit[] result) {
            onResult.apply(Arrays.asList(result));
        }

        @Override
        protected Hit[] doInBackground(final String... name) {
            if(name.length > 1)return mAsyncTaskDao.find(name[0], name[1]);
            return mAsyncTaskDao.find(name[0]);
        }
    }
}

package barqsoft.footballscores.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.RemoteViews;
import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chrispix on 7/18/15.
 */
public class UpdateWidgetService extends Service {

    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_MATCHTIME = 2;

    private static final String TAG = UpdateWidgetService.class.getSimpleName();
    private final Date currentDate = new Date(System.currentTimeMillis());
    private final SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
                .getApplicationContext());

        int[] allWidgetIds = intent
                .getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        final int N = allWidgetIds.length;

        updateScores();
        for (int i = 0; i < N; i++) {
            int appWidgetId = allWidgetIds[i];

            Intent mainActivityIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mainActivityIntent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.score_widget);
            views.setOnClickPendingIntent(R.id.score_widget_layout, pendingIntent);

            updateScoreView(appWidgetManager, appWidgetId, views);



        }

        stopSelf();

        return super.onStartCommand(intent, flags, startId);
    }

    private void updateScores()
    {
        Intent service_start = new Intent(this, MyFetchService.class);
        this.startService(service_start);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void updateScoreView(AppWidgetManager appWidgetManager, int appWidgetId, RemoteViews views) {
        String[] fragmentDate = new String[1];
        fragmentDate[0] = mformat.format(currentDate);

        Cursor cursor = getContentResolver().query(
                DatabaseContract.scores_table.buildScoreWithDate(),
                null,
                null,
                fragmentDate,
                null
        );

        if (cursor != null) {
            cursor.moveToFirst();
            views.setTextViewText(R.id.home_name, cursor.getString(COL_HOME));
            views.setTextViewText(R.id.away_name, cursor.getString(COL_AWAY));
            views.setTextViewText(R.id.date, cursor.getString(COL_MATCHTIME));
            views.setTextViewText(R.id.score, Utilies.getScores(cursor.getInt(COL_HOME_GOALS), cursor.getInt(COL_AWAY_GOALS)));

            cursor.close();
        }


        // Tell the AppWidgetManager to perform an update on the current app widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

}

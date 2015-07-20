package barqsoft.footballscores;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import barqsoft.footballscores.service.UpdateWidgetService;

/**
 * Created by chrispix on 7/18/15.
 */
public class ScoreWidgetProvider extends AppWidgetProvider {


    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;


        // Build the intent to call the service
        Intent intent = new Intent(context.getApplicationContext(),
                UpdateWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

        // Update the widgets via the service
        context.startService(intent);

    }


}
package com.example.salvadorelizarraras.bakingapp3;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RemoteViews;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidget extends AppWidgetProvider {

    public static ArrayList<String> list;
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, ArrayList<String> dataList,
                                int appWidgetId) {
            list = dataList;
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

        Intent intent = new Intent(context ,WidgetRemoteService.class);

        views.setRemoteAdapter(R.id.myListView,intent);

        Intent intent1 = new Intent(context, MainActivity.class);
        PendingIntent pendin = PendingIntent.getActivity(context,0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);


        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.myListView);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            Log.d(TAG, "onUpdate: ");
            WidgetService.startActionService(context);
        }
    }
    public static void updateWidgets(Context context, AppWidgetManager appWidgetManager, ArrayList<String> list, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            Log.d(TAG, "updatePlantWidgets() widget Id: " + appWidgetId);
            updateAppWidget(context, appWidgetManager, list, appWidgetId);
        }
    }
    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


package com.example.salvadorelizarraras.bakingapp3;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Salvador Elizarraras on 21/03/2018.
 */

public class WidgetService extends IntentService {

    public static final String ACTION = "com.example.salvadoelizarraras.bakingapp3.widget";
    public static final String UPDATE = "com.example.salvadoelizarraras.bakingapp3.update";
    private ArrayList<String> list;
    private final String TAG = WidgetService.class.getSimpleName();

    public WidgetService() {
        super(WidgetService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.d(TAG, "onHandleIntent: ");
        if (intent != null){
            String action = intent.getAction();
            if (action.equals(ACTION)){
                Bundle bundle = intent.getExtras().getBundle("bundle");
                Log.d(TAG, "onHandleIntent: Filling data");
                    list = bundle.getStringArrayList("list");
                for (String ingredient :
                        list) {
                    Log.d(TAG, "Ingredient : "+ingredient);
                }
                    handlerUpdateWidget(list);

            }
        }
    }

    private void handlerUpdateWidget(ArrayList<String> list) {


        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidget.class));
        //Now update all widgets
        RecipeWidget.updateWidgets(this, appWidgetManager, list, appWidgetIds);
    }

    public static void startActionService(Context context) {
        Intent intent = new Intent (context, WidgetService.class);
        intent.setAction(UPDATE);
        context.startService(intent);
    }
}

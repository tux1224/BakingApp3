package com.example.salvadorelizarraras.bakingapp3;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

/**
 * Created by Salvador Elizarraras on 21/03/2018.
 */

public class WidgetRemoteService extends RemoteViewsService {
    public ArrayList<String> list;
    Context context;
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteServiceFactory(this.getApplicationContext());
    }




    class WidgetRemoteServiceFactory implements RemoteViewsService.RemoteViewsFactory{

        public WidgetRemoteServiceFactory(Context applicationContext) {
            context = applicationContext;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            list = RecipeWidget.list;
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return (list == null)? 0 : list.size();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.item_widget);
            remoteViews.setTextViewText(R.id.tv_widget, list.get(i).toString());

            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}

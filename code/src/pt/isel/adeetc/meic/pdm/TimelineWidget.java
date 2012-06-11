package pt.isel.adeetc.meic.pdm;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import pt.isel.adeetc.meic.pdm.common.DateHelper;
import pt.isel.adeetc.meic.pdm.controllers.TwitterServiceClient;
import pt.isel.adeetc.meic.pdm.db.StatusTable;
import pt.isel.adeetc.meic.pdm.db.YambaTweet;
import pt.isel.adeetc.meic.pdm.services.TimelineContentProvider;
import winterwell.jtwitter.Twitter;


import java.util.Date;

public class TimelineWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {

        Cursor c = context.getContentResolver().query(TimelineContentProvider.CONTENT_URI, StatusTable.COLUMN_ALL, null, null, null);
        Twitter.ITweet tweet = null;
        try{

        if(c.moveToLast())
        {


            tweet =  new YambaTweet
                    (
                            c.getLong(StatusTable.COLUMN_ID_INDEX),
                            new Date(c.getLong(StatusTable.COLUMN_DATE_INDEX)),
                            c.getString(StatusTable.COLUMN_MESSAGE_INDEX),
                            c.getString(StatusTable.COLUMN_USERNAME_INDEX)
                    );
        }     
            for(int id:appWidgetIds)
            {
                RemoteViews widget=new RemoteViews(context.getPackageName(),
                        R.layout.timeline_widget_layout);
                

                
                if(tweet == null)
                {
                    widget.setTextViewText(R.id.timeline_widget_tweet_user,"Not exist tweets");
                }
                
                else{

                    widget.setTextViewText(R.id.timeline_widget_tweet_user,tweet.getUser().getName());

                    widget.setTextViewText(R.id.timeline_widget_tweet_status, tweet.getText());

                    widget.setTextViewText(R.id.timeline_widget_tweet_date, DateHelper.stringifyDifference(new Date(System.currentTimeMillis()), tweet.getCreatedAt()));
                }

                widget.setOnClickPendingIntent(R.id.timeline_widget_tweet_linear,
                        PendingIntent.getActivity(
                                context,0,new Intent(
                                context,TimelineActivity.class),
                                PendingIntent.FLAG_UPDATE_CURRENT));

                appWidgetManager.updateAppWidget(id,widget);
               
            }
           
        }finally {
            c.close();
        }
        
       super.onUpdate(context,appWidgetManager,appWidgetIds);
    }


    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context,intent);
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        this.onUpdate(context,widgetManager,
                widgetManager.getAppWidgetIds(new ComponentName(context,TimelineWidget.class)));


    }
}

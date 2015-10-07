package com.corporation.tvm.handin4camera;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.SystemClock;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.corporation.tvm.services.MonitorScreenService;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class battery_widget extends AppWidgetProvider {

    private static final String ACTION_BATTERY_UPDATE = "android.appwidget.battery.action.UPDATE";
    private int batteryLevel = 0;
    private double timeEstimate = 0;
    ArrayList<Integer> consumption;
    //double[] consumption;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        // Sometimes when the phone is booting, onUpdate method gets called before onEnabled()
        int currentLevel = calculateBatteryLevel(context);
        if (batteryChanged(currentLevel)) {
            //      add current consumption for  5 minutes into array
            if(consumption == null)
                consumption = new ArrayList<Integer>();
            consumption.add(batteryLevel - currentLevel);

            batteryLevel = currentLevel;

            // Get average consumption
            int avg = getAvgConsumption();
            // Multiplies 'expected count of 2.5 minute intervals until battery is 0' with '2.5 min' to get the time estimate
            timeEstimate = getCountOfIntervalsTilDepletion(avg, batteryLevel)*2.5;
        }
        updateViews(context);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        turnAlarmOnOff(context, true);
        context.startService(new Intent(context, MonitorScreenService.class));
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);

        turnAlarmOnOff(context, false);
        context.stopService(new Intent(context, MonitorScreenService.class));
    }

    private boolean batteryChanged(int currentLevelLeft) {
        return (batteryLevel != currentLevelLeft);
    }

    public static void turnAlarmOnOff(Context context, boolean turnOn) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ACTION_BATTERY_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        if (turnOn) { // Add extra 1 sec because sometimes ACTION_BATTERY_CHANGED is called after the first alarm
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, 150 * 1000, pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
        }
    }

    private int calculateBatteryLevel(Context context) {
        Intent batteryIntent = context.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
        return level * 100 / scale;
    }

    private void updateViews(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.battery_widget);
        views.setTextViewText(R.id.batteryText, batteryLevel + "%");
        if(timeEstimate == 0)
            views.setTextViewText(R.id.timeEstimate, "?? min left");
        else
            views.setTextViewText(R.id.timeEstimate, timeEstimate + " min left");
        views.setProgressBar(R.id.batteryProgress, 100, batteryLevel, false);

        ComponentName componentName = new ComponentName(context, battery_widget.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(componentName, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equals(ACTION_BATTERY_UPDATE)) {
            int currentLevel = calculateBatteryLevel(context);
            if (batteryChanged(currentLevel)) {
                batteryLevel = currentLevel;
                updateViews(context);
            }
        }
    }

    public int getAvgConsumption() {
        int allConsumptions = 0;
        for(int i=0;i<consumption.size();i++) {
            allConsumptions += consumption.get(i);
        }
        return allConsumptions/consumption.size();
    }

    public int getCountOfIntervalsTilDepletion(int avg, int batteryLevel) {
        double tempBatteryLevel = batteryLevel;
        int count = 0;
        while (tempBatteryLevel > 0)
        {
            tempBatteryLevel = tempBatteryLevel - avg;
            count++;
        }
        return count;
    }
}


package ebk.batusrs.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.util.Calendar;

import ebk.batusrs.MainActivity;

/**
 * Created by E.Batuhan Kaynak on 20.7.2016.
 */
public class SrsMidnightAlarmReceiver extends WakefulBroadcastReceiver {
    AlarmManager alarmManager;
    PendingIntent alarmIntent;

    public void setAlarm(Context context){
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, SrsMidnightAlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        // Set the alarm to start at approximately 12:00 p.m.

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Log.i("hiyo", calendar.getTime().toLocaleString());
        Log.i("hiyo", String.valueOf(calendar.getTimeInMillis()));

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);

        ComponentName receiver = new ComponentName(context, SrsBootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void cancelAlarm(Context context) {
        // If the alarm has been set, cancel it.
        if (alarmManager!= null) {
            alarmManager.cancel(alarmIntent);
        }

        // Disable {@code SampleBootReceiver} so that it doesn't automatically restart the
        // alarm when the device is rebooted.
        ComponentName receiver = new ComponentName(context, SrsBootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "Recieved Alarm", Toast.LENGTH_SHORT).show ();
        Log.i("hiyo", "Recieved Alarm");
        Log.i("hiyo", Boolean.toString(MainActivity.stopMode));
        if (!MainActivity.stopMode){
            Intent service = new Intent(context, SrsMidnightAlarmService.class);
            startWakefulService(context, service);
        }
    }
}
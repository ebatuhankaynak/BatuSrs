package ebk.batusrs.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by E.Batuhan Kaynak on 20.7.2016.
 */

public class SrsBootReceiver extends BroadcastReceiver {
    SrsMidnightAlarmReceiver srsMidnightAlarmReceiver = new SrsMidnightAlarmReceiver();
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.
            srsMidnightAlarmReceiver.setAlarm(context);
        }
    }
}

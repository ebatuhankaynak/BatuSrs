package ebk.batusrs.alarm;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ebk.batusrs.database.BatuSrsDatabaseHelper;

/**
 * Created by E.Batuhan Kaynak on 21.7.2016.
 */
public class SrsMidnightAlarmService extends IntentService {
    private SQLiteDatabase db;
    private Cursor cursor;

    public SrsMidnightAlarmService() {
        super("SrsMidnightAlarmService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SQLiteOpenHelper srsDatabaseHelper = new BatuSrsDatabaseHelper(this);
        db = srsDatabaseHelper.getReadableDatabase();

        updateSrsValues();
    }

    private void updateSrsValues() {
        cursor = db.query("LECTURE", new String[]{"SRS", "NAME", "LEVEL", "_id"}, null, null,
                null, null, null);

        if (cursor.moveToFirst()){
            for (int i = 0; i  < cursor.getCount(); i++){
                Log.i("hiyo", cursor.getString(2));
                ContentValues updatedValues = new ContentValues();
                int srs = cursor.getInt(0);
                if (srs == 0){
                    int level = cursor.getInt(2);
                    int newLevel = 0;
                    if (!(level == 0)){
                        newLevel = level - 1;
                    }
                    srs = getNewSrs(newLevel);
                    updatedValues.put("LEVEL", newLevel);
                }else{
                    srs--;
                }
                updatedValues.put("SRS", srs);

                db.update("LECTURE", updatedValues, "_id = ?", new String[] {Integer.toString((cursor.getInt(3)))});
                cursor.moveToNext();
            }
        }
    }

    private int getNewSrs(int newLevel) {
        int newSrs;
        switch (newLevel){
            case 1: newSrs = 3; break;
            case 2: newSrs = 5; break;
            case 3: newSrs = 12; break;
            case 4: newSrs = 24; break;
            default: newSrs = 0;
        }
        return newSrs;
    }
}

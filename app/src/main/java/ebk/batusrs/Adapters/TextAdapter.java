package ebk.batusrs.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import ebk.batusrs.R;
import ebk.batusrs.database.BatuSrsDatabaseHelper;

/**
 * Created by E.Batuhan Kaynak on 4.2.2017.
 */

public class TextAdapter extends BaseAdapter
{

    private SQLiteDatabase db;
    private Cursor cursor;
    private Context context;
    private String[] schedule;

    public TextAdapter(Context context)
    {
        this.context = context;

        ArrayList<String> scheduleList = new ArrayList<String>();
        scheduleList.add("");
        scheduleList.add("MON");
        scheduleList.add("TUE");
        scheduleList.add("WED");
        scheduleList.add("THU");
        scheduleList.add("FRI");

        SQLiteOpenHelper srsDatabaseHelper = new BatuSrsDatabaseHelper(context);
        db = srsDatabaseHelper.getReadableDatabase();

        cursor = db.query("SCHEDULE", new String[] {"MON", "TUE", "WED", "THU", "FRI", "_id"}, null, null, null, null, null);
        if (cursor.moveToFirst()){
            for(int i = 1; i < 9; i++){
                scheduleList.add("" + i);
                for (int j = 0; j < 5; j++){
                    String value = cursor.getString(j);
                    if (value == null){
                        value = "";
                    }
                    if (value.equals("")){
                        scheduleList.add("");
                    } else {
                        scheduleList.add(value);
                    }
                }
                cursor.moveToNext();
            }
        }



        schedule = new String[scheduleList.size()];
        schedule = scheduleList.toArray(schedule);
    }

    @Override
    public int getCount() {
        return schedule.length;
    }

    @Override
    public Object getItem(int position) {
        return schedule[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView text = new TextView(this.context);

        text.setText(schedule[position]);

        text.setGravity(Gravity.CENTER);

        text.setBackgroundColor(Color.parseColor("#fbdcbb"));

        text.setLayoutParams(new GridView.LayoutParams(165, 250));

        text.setBackgroundResource(R.drawable.grid_items_border);

        return text;

    }


    public String getText(int position) {
        return schedule[position];
    }
}

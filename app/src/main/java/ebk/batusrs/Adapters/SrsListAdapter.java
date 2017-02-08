package ebk.batusrs.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import ebk.batusrs.R;

/**
 * Created by E.Batuhan Kaynak on 5.2.2017.
 */

public class SrsListAdapter extends SimpleCursorAdapter{

    private LayoutInflater inflater;
    private int layout;

    public SrsListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.layout = layout;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView (Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(layout, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);

        TextView listNameTextView = (TextView) view.findViewById(R.id.listNameTextView);
        TextView listLectureNumTextView = (TextView) view.findViewById(R.id.listLectureNumTextView);
        TextView listSrsTextView = (TextView) view.findViewById(R.id.listSrsTextView);

        listNameTextView.setTypeface(listNameTextView.getTypeface(), Typeface.BOLD);
        listLectureNumTextView.setTypeface(listLectureNumTextView.getTypeface(), Typeface.BOLD);
        listSrsTextView.setTypeface(listSrsTextView.getTypeface(), Typeface.BOLD);

        String name = cursor.getString(1);
        String lectureNum = cursor.getString(2);
        String srs = cursor.getString(3);
        int level = cursor.getInt(4);

        if(srs.equals("0")){
            srs = "Today";
        }

        view.setBackgroundColor(getViewColor(level));

        listNameTextView.setText(name);
        listLectureNumTextView.setText(lectureNum);
        listSrsTextView.setText(srs);
    }

    private int getViewColor(int level) {
        int color;
        switch (level){
            case 1: color = Color.rgb(240, 110, 190); break;
            case 2: color = Color.rgb(150, 50, 170); break;
            case 3: color = Color.rgb(70, 100, 225); break;
            case 4: color = Color.rgb(0, 160, 240); break;
            case 5: color = Color.rgb(80, 80, 80); break;
            default: color = Color.WHITE;
        }
        return color;
    }
}

package ebk.batusrs;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import ebk.batusrs.adapters.TextAdapter;
import ebk.batusrs.database.BatuSrsDatabaseHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {

    private SQLiteDatabase db;
    private Cursor cursor;
    private Cursor scheduleCursor;

    private SimpleCursorAdapter scheduleAdapter;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SQLiteOpenHelper srsDatabaseHelper = new BatuSrsDatabaseHelper(getContext());
        db = srsDatabaseHelper.getReadableDatabase();

        scheduleCursor = db.query("LECTURELIST", new String[]{"LECTURE", "_id"}, null, null, null,
                null, null);

        scheduleAdapter = new SimpleCursorAdapter(inflater.getContext(),
                android.R.layout.simple_list_item_1,
                scheduleCursor,
                new String[]{"LECTURE"},
                new int[]{android.R.id.text1},
                0);

        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        GridView gridview = (GridView) view.findViewById(R.id.gridview);
        final TextAdapter textAdapter = new TextAdapter(getContext());
        gridview.setAdapter(textAdapter);

        // TODO: 8.2.2017 LET USER ENTER ALL HIS/HER ACTIVITIES, THEN PLACE THEM INTO THE SCHEDULE.
        // TODO: 8.2.2017 2)MAKE TIMESLOTS CUSTOMIZEABLE BY CLICKING?
        // TODO: 8.2.2017 3)ADD THE OPTION TO ADD ROWS?(ŞİT GİBİMSİ)

        Button manageLecturesButton = (Button) view.findViewById(R.id.manageLecturesButton);
        manageLecturesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Transition.getInstance().switchFragment(getFragmentManager(), new ManageLecturesFragment());
            }
        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {
            if(!(position % 6 == 0 || position < 6)){
                //Toast.makeText(getContext(), "" + position, Toast.LENGTH_SHORT).show();

                final AlertDialog builder = new AlertDialog.Builder(getContext()).create();
                builder.setTitle("LECTURES");
                View viewOfList = LayoutInflater.from(getContext()).inflate(R.layout.schedule_dialog,null);

                final ListView scheduleDialogListView = (ListView) viewOfList.findViewById(R.id.scheduleDialogListView);
                scheduleDialogListView.setAdapter(scheduleAdapter);

                scheduleDialogListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        ContentValues updatedValues = new ContentValues();
                        String day = getDay(position);

                        Cursor c = (Cursor) scheduleDialogListView.getItemAtPosition(i);
                        final String lectureDay = c.getString(0);

                        updatedValues.put(day, lectureDay);
                        int timeSlot = position / 6;
                        db.update("SCHEDULE", updatedValues, "_id = ?", new String[] {String.valueOf(timeSlot)});

                        if (timeSlot % 2 == 1){
                            db.update("SCHEDULE", updatedValues, "_id = ?", new String[] {String.valueOf(timeSlot + 1)});
                        }else{
                            db.update("SCHEDULE", updatedValues, "_id = ?", new String[] {String.valueOf(timeSlot - 1)});
                        }

                        builder.dismiss();
                        Transition.getInstance().switchFragment(getFragmentManager(), new ScheduleFragment());
                    }
                });
                builder.setView(viewOfList);
                builder.show();
            }
            }
        });
    }

    private String getDay(int position) {
        String day;
        int dayInt = position % 6;
        switch (dayInt){
            case 1: day = "MON"; break;
            case 2: day = "TUE"; break;
            case 3: day = "WED"; break;
            case 4: day = "THU"; break;
            case 5: day = "FRI"; break;
            default: day = "MON";
        }
        return day;
    }
}

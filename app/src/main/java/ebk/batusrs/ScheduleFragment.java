package ebk.batusrs;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.util.function.ToDoubleBiFunction;

import ebk.batusrs.adapters.TextAdapter;
import ebk.batusrs.database.BatuSrsDatabaseHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {

    private SQLiteDatabase db;
    private Cursor cursor;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SQLiteOpenHelper srsDatabaseHelper = new BatuSrsDatabaseHelper(getContext());
        db = srsDatabaseHelper.getReadableDatabase();

        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        GridView gridview = (GridView) view.findViewById(R.id.gridview);
        final TextAdapter textAdapter = new TextAdapter(getContext());
        gridview.setAdapter(textAdapter);
        
        // TODO: 8.2.2017 LET USER ENTER ALL HIS/HER ACTIVITIES, THEN PLACE THEM INTO THE SCHEDULE.
        // TODO: 8.2.2017 1)WHERE TO PLACE INPUT BOXES?(ADD A FAB MAYBE?)
        // TODO: 8.2.2017 2)MAKE TIMESLOTS CUSTOMIZEABLE BY CLICKING?
        // TODO: 8.2.2017 3)ADD THE OPTION TO ADD ROWS?


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {

                if(!(position % 6 == 0 || position < 6)){
                    Toast.makeText(getContext(), "" + position, Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Edit Lecture");
                    //builder.setMessage("What is your name:");

                    final EditText input = new EditText(getContext());
                    input.setText(textAdapter.getText(position));
                    builder.setView(input);

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ContentValues updatedValues = new ContentValues();
                            String day = getDay(position);
                            updatedValues.put(day, input.getText().toString());
                            int timeSlot = position / 6;
                            db.update("SCHEDULE", updatedValues, "_id = ?", new String[] {String.valueOf(timeSlot)});

                            if (timeSlot % 2 == 1){
                                db.update("SCHEDULE", updatedValues, "_id = ?", new String[] {String.valueOf(timeSlot + 1)});
                            }else{
                                db.update("SCHEDULE", updatedValues, "_id = ?", new String[] {String.valueOf(timeSlot - 1)});
                            }

                            refreshFragment();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
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

    private void refreshFragment(){
        Transition.getInstance().switchFragment(getFragmentManager(), new ScheduleFragment());
    }
}

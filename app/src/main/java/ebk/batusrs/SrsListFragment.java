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
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import ebk.batusrs.adapters.SrsListAdapter;
import ebk.batusrs.database.BatuSrsDatabaseHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class SrsListFragment extends Fragment {

    private SQLiteDatabase db;
    private Cursor todayCursor;
    private Cursor upcomingCursor;

    private SrsListAdapter todayListAdapter;
    private SrsListAdapter upcomingListAdapter;

    public SrsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SQLiteOpenHelper srsDatabaseHelper = new BatuSrsDatabaseHelper(getContext());
        db = srsDatabaseHelper.getReadableDatabase();

        todayCursor = db.query("LECTURE", new String[]{"_id", "NAME", "LEC_NUM", "SRS", "LEVEL"}, "SRS=?",
                new String[]{"0"}, null, null, "SRS ASC");

        upcomingCursor = db.query("LECTURE", new String[]{"_id", "NAME", "LEC_NUM", "SRS", "LEVEL"}, "SRS!=?",
                new String[]{"0"}, null, null, "SRS ASC");

        todayListAdapter = new SrsListAdapter(inflater.getContext(),
                R.layout.srs_list,
                todayCursor,
                new String[]{"NAME", "LEC_NUM", "SRS", "LEVEL"},
                new int[]{R.id.listNameTextView, R.id.listLectureNumTextView, R.id.listSrsTextView},
                0);
        upcomingListAdapter = new SrsListAdapter(inflater.getContext(),
                R.layout.srs_list,
                upcomingCursor,
                new String[]{"NAME", "LEC_NUM", "SRS", "LEVEL"},
                new int[]{R.id.listNameTextView, R.id.listLectureNumTextView, R.id.listSrsTextView},
                0);
        return inflater.inflate(R.layout.fragment_srs_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView todayList = (ListView) view.findViewById(R.id.todayList);
        ListView upcomingList = (ListView) view.findViewById(R.id.upcomingList);

        // TODO: 8.2.2017 ACCESS DB WHEN USER SAYS "YES" ???
        // TODO: 8.2.2017 CHANGE QUERY TO BETTER SUIT NEW DESIGN 
        todayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                long id = l;
                todayCursor = db.query("LECTURE", new String[] {"SRS", "LEVEL", "_id"}, "_id = ?", new String[] {Integer.toString((int) id)},
                        null, null, null);

                if (todayCursor.moveToFirst()){
                    if (todayCursor.getInt(0) == 0){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Completed?");
                        final int dbId = (int) id;
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                int newLevel = todayCursor.getInt(1) + 1;
                                int newSrs = getNewSrs(newLevel);

                                ContentValues updatedValues = new ContentValues();
                                updatedValues.put("LEVEL", newLevel);
                                updatedValues.put("SRS", newSrs);

                                db.update("LECTURE", updatedValues, "_id = ?", new String[] {Integer.toString((dbId))});

                                refreshFragment();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                        builder.show();
                    }
                }
            }
        });

        upcomingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        todayList.setAdapter(todayListAdapter);
        upcomingList.setAdapter(upcomingListAdapter);

        //setListAdapter(listAdapter);
    }
    
    private int getNewSrs(int newLevel) {
        int newSrs;
        switch (newLevel){
            case 1: newSrs = 2; break;
            case 2: newSrs = 3; break;
            case 3: newSrs = 7; break;
            case 4: newSrs = 14; break;
            case 5: newSrs = 28; break;
            default: newSrs = 0;
        }
        return newSrs;
    }

    private void refreshFragment(){
        Fragment fragment = new SrsListFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }
}

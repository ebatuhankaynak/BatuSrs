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
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import ebk.batusrs.database.BatuSrsDatabaseHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManageLecturesFragment extends Fragment {

    private SQLiteDatabase db;
    private Cursor cursor;

    private SimpleCursorAdapter lecturesListAdapter;

    public ManageLecturesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SQLiteOpenHelper srsDatabaseHelper = new BatuSrsDatabaseHelper(getContext());
        db = srsDatabaseHelper.getWritableDatabase();

        cursor = db.query("LECTURELIST", new String[]{"LECTURE", "_id"}, null, null, null,
                null, null);

        lecturesListAdapter = new SimpleCursorAdapter(inflater.getContext(),
                android.R.layout.simple_list_item_1,
                cursor,
                new String[]{"LECTURE"},
                new int[]{android.R.id.text1},
                0);
        return inflater.inflate(R.layout.fragment_manage_lectures, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        final EditText manageAddLectureEditText = (EditText) view.findViewById(R.id.manageAddLectureEditText);
        final Button manageAddActivitiesButton = (Button) view.findViewById(R.id.manageAddLectureButton);
        manageAddActivitiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lectureName = manageAddLectureEditText.getText().toString();
                if (!lectureName.equals("")){
                    ContentValues lectureValues = new ContentValues();
                    lectureValues.put("LECTURE", lectureName);

                    db.insert("LECTURELIST", null, lectureValues);

                    manageAddLectureEditText.setText("");
                    Transition.getInstance().switchFragment(getFragmentManager(), new ManageLecturesFragment());
                }


            }
        });

        final ListView lecturesListView = (ListView) view.findViewById(R.id.lecturesListView);
        // TODO: 10.2.2017 DELETE LECTURES FROM LIST(USER)
        lecturesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, final long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Edit Lecture");

                final EditText input = new EditText(getContext());
                Cursor c = (Cursor) lecturesListView.getItemAtPosition(i);
                input.setText(c.getString(0));
                builder.setView(input);

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ContentValues updatedValues = new ContentValues();

                        updatedValues.put("LECTURE", input.getText().toString());

                        db.update("LECTURELIST", updatedValues, "_id = ?", new String[] {String.valueOf(l)});

                        Transition.getInstance().switchFragment(getFragmentManager(), new ManageLecturesFragment());
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
        });
        lecturesListView.setAdapter(lecturesListAdapter);
    }
}

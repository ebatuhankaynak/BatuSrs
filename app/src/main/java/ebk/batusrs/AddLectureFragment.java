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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.ToDoubleBiFunction;

import ebk.batusrs.R;
import ebk.batusrs.database.BatuSrsDatabaseHelper;

public class AddLectureFragment extends Fragment {

    private SQLiteDatabase db;
    private Cursor cursor;
    private View view;

    private Spinner sItems;
    private EditText lectureNumEditText;
    private EditText lectureNotesEditText;

    public AddLectureFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart(){
        super.onStart();
        sItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                lectureNumEditText.setText(getLastLectureNum());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //lectureNumEditText.setText(getLastLectureNum());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_lecture, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState){
        this.view = view;
        // you need to have a list of data that you want the spinner to display
        List<String> spinnerArray =  new ArrayList<String>();

        SQLiteOpenHelper srsDatabaseHelper = new BatuSrsDatabaseHelper(getContext());
        db = srsDatabaseHelper.getWritableDatabase();

        cursor = db.query("SCHEDULE", new String[] {"MON", "TUE", "WED", "THU", "FRI", "_id"}, null, null, null,
                null, null);

        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++){
                for (int j = 0; j < cursor.getColumnCount() - 1; j++){
                    String value = cursor.getString(j);
                    if (value != null){
                        if (!spinnerArray.contains(value)){
                            spinnerArray.add(value);
                        }
                    }
                }
                cursor.moveToNext();
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this.getContext(), android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sItems = (Spinner) view.findViewById(R.id.lectures_spinner);
        sItems.setAdapter(adapter);

        lectureNumEditText = (EditText) view.findViewById(R.id.lectureNumEditText);

        Button addLectureButton = (Button) view.findViewById(R.id.addLectureButton);
        addLectureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddLectureClick();
            }
        });
    }

    private String getLastLectureNum() {
        String lastLectureNum = "";

        String selected = sItems.getSelectedItem().toString();
        final String lectureName = selected;

        Cursor lastCursor = db.query("LECTURE", new String[] {"LEC_NUM", "_id"}, "NAME=?", new String[]{lectureName}, null,
                null, "LEC_NUM DESC");

        if (lastCursor.moveToFirst()){
            lastLectureNum = String.valueOf(Integer.parseInt(lastCursor.getString(0)) + 1);
        }else {
            lastLectureNum = "1";
        }
        return lastLectureNum;
    }

    public void onAddLectureClick() {
        String selected = sItems.getSelectedItem().toString();
        final String lectureName = selected;

        lectureNotesEditText = (EditText) view.findViewById(R.id.lectureNotesEditText);
        String lectureNote = lectureNotesEditText.getText().toString();

        lectureNumEditText = (EditText) view.findViewById(R.id.lectureNumEditText);
        String lectureNum = lectureNumEditText.getText().toString();

        Cursor checkCursor = db.query("LECTURE", new String[] {"LEC_NUM", "_id"}, "NAME=? AND LEC_NUM=?",
                new String[]{lectureName, lectureNum}, null, null, "LEC_NUM DESC");

        if (checkCursor.moveToFirst()){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("This Lecture Already Exists!");
            builder.setPositiveButton("Oops ^_^", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {
                    return;
                }
            });
            builder.show();
        }else{
            insertLecture(lectureName, lectureNum, lectureNote);
            Transition.getInstance().switchFragment(getFragmentManager(), new SrsListFragment());
        }
    }

    private void insertLecture(String lectureName, String lectureNum, String lectureNote){
        ContentValues lectureValues = new ContentValues();
        lectureValues.put("NAME", lectureName);
        lectureValues.put("LEC_NUM", lectureNum);
        lectureValues.put("LEVEL", 0);
        lectureValues.put("SRS", 0);
        lectureValues.put("NOTE", lectureNote);

        db.insert("LECTURE", null, lectureValues);
    }
}

package ebk.batusrs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import ebk.batusrs.alarm.SrsMidnightAlarmReceiver;
import ebk.batusrs.database.AndroidDatabaseManager;
import ebk.batusrs.database.BatuSrsDatabaseHelper;
import ebk.batusrs.intro.IntroActivity;

public class MainActivity extends AppCompatActivity {
    //9 + 6
    private float x1,x2;
    static final int MIN_DISTANCE = 50;

    private SQLiteDatabase db;
    private Cursor cursor;
    private SrsMidnightAlarmReceiver srsMidnightAlarmReceiver = new SrsMidnightAlarmReceiver();

    public static boolean stopMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get SharedPreferences from getSharedPreferences("name_file", MODE_PRIVATE)
        SharedPreferences shared = getSharedPreferences("settings",MODE_PRIVATE);
        //Using getXXX- with XX is type date you wrote to file "name_file"
        stopMode = shared.getBoolean("stopMode", false);

        //Log.i("hiyo", Boolean.toString(stopMode));

        SQLiteOpenHelper srsDatabaseHelper = new BatuSrsDatabaseHelper(this);
        db = srsDatabaseHelper.getWritableDatabase();

        //  Declare a new thread to do a preference check
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (false) {
                    //  Launch app intro
                    Intent i = new Intent(MainActivity.this, IntroActivity.class);
                    startActivity(i);

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });

        // Start the thread
        t.start();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cursor = db.query("SCHEDULE", new String[] {"MON", "TUE", "WED", "THU", "FRI", "_id"}, null, null, null,
                        null, null);

                boolean foundLecture = false;
                if (cursor.moveToFirst()) {
                    for (int i = 0; i < cursor.getCount(); i++){
                        for (int j = 0; j < cursor.getColumnCount() - 1; j++){
                            String value = cursor.getString(j);
                            if (value != null){
                                foundLecture = true;
                                break;
                            }
                        }
                        cursor.moveToNext();
                    }
                }

                if (foundLecture){
                    Transition.getInstance().switchFragment(getSupportFragmentManager(), new AddLectureFragment());
                    /*
                    Fragment fragment = new AddLectureFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.addToBackStack(null);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();*/
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("You seem to have an empty schedule, please update your schedule to continue");
                    // TODO: 5.2.2017 FIX TEXT FIT
                    builder.setPositiveButton("Oops ^_^", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            return;
                        }
                    });
                    builder.show();
                }
            }
        });

        srsMidnightAlarmReceiver.setAlarm(this);

        Transition.getInstance().switchFragment(getSupportFragmentManager(), new SrsListFragment());

        //db.delete("LECTURE", null, null);
        //db.delete("SCHEDULE", null, null);
        /*
        db.execSQL("DROP TABLE IF EXISTS LECTURE");
        db.execSQL("DROP TABLE IF EXISTS SCHEDULE");
        db.execSQL("DROP TABLE IF EXISTS LECTURELIST");
        db.setVersion(0);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = new SrsListFragment();

        if (id == R.id.action_home){
            fragment = new SrsListFragment();
        }else if (id == R.id.action_schedule) {
            fragment = new ScheduleFragment();
        }else if (id == R.id.action_progress){
            fragment = new ProgressFragment();
        }else if (id == R.id.action_info){
            fragment = new InfoFragment();
        }else if (id == R.id.action_settings){
            fragment = new SettingsFragment();
        }
        Transition.getInstance().switchFragment(getSupportFragmentManager(), fragment);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;
                if (Math.abs(deltaX) > MIN_DISTANCE) {
                    // Left to Right swipe action
                    if (x2 > x1) {
                        Toast.makeText(this, "Left to Right swipe [Next]", Toast.LENGTH_SHORT).show ();
                    }else {
                        Toast.makeText(this, "Right to Left swipe [Previous]", Toast.LENGTH_SHORT).show ();
                        Intent dbmanager = new Intent(this ,AndroidDatabaseManager.class);
                        startActivity(dbmanager);
                    }
                }else {
                    // consider as something else - a screen tap for example
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}

package ebk.batusrs.database;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by E.Batuhan Kaynak on 4.7.2016.
 */
public class BatuSrsDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Srs"; // the name of our database
    private static final int DB_VERSION = 1; // the version of the database

    public static final String TABLE_NAME = "ACTIVITY";
    public static final String ID = "_id";
    public static final String NAME = "NAME";
    public static final String TYPE = "Type";
    public static final String START_TIME = "START_TIME";
    public static final String END_TIME = "END_TIME";
    public static final String DURATION = "DURATION";
    public static final String ONGOING = "ONGOING";
    public static final String DATE = "DATE";

    public BatuSrsDatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("in update", "in update");
        if (oldVersion < 1) {
            Log.i("in update", "in create");
            db.execSQL("CREATE TABLE LECTURE (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "NAME TEXT, "
                    + "LEC_NUM TEXT, "
                    + "LEVEL INTEGER, " //+ "LAST_LEC_NUM TEXT, "
                    + "SRS INTEGER);"); //"DATE TEXT);");
            db.execSQL("CREATE TABLE SCHEDULE (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "MON TEXT, "
                    + "TUE TEXT, "
                    + "WED TEXT, "
                    + "THU TEXT, "
                    + "FRI TEXT);");
            for(int i = 0; i < 8; i++){
                db.execSQL("INSERT INTO SCHEDULE DEFAULT VALUES");
            }
        }
        if (oldVersion < 2) {

        }
    }

    //Helper method for AndroidDatabaseManager
    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }
}

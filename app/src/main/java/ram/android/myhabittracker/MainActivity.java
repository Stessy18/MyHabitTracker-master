package ram.android.myhabittracker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ram.android.myhabittracker.data.HabitContract;
import ram.android.myhabittracker.data.HabitTrackerDBHelper;

import static ram.android.myhabittracker.data.HabitContract.HabitEntry.COLUMN_DURATION;
import static ram.android.myhabittracker.data.HabitContract.HabitEntry.COLUMN_HABIT_NAME;

public class MainActivity extends AppCompatActivity {

    HabitTrackerDBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new HabitTrackerDBHelper(this);

        Button gotoButton = (Button) findViewById(R.id.button);


        gotoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText editText1 = (EditText) findViewById(R.id.Text);
                String exhab = editText1.getText().toString();
                EditText editText2 = (EditText) findViewById(R.id.Text2);
                int exdur = Integer.parseInt(editText2.getText().toString());
                insertHabit(exhab, exdur);
                readHabits(exhab);
                deleteEntries();


            }
        });
        // put your onclick listener here with insert
        insertHabit("Gaming", 120);
        insertHabit("Gaming", 200);
        insertHabit("Yoga", 60);
        insertHabit("Gaming", 45);
        readHabits("Gaming");
        deleteEntries();
        insertHabit("Cycling", 45);
        insertHabit("Walking", 60);
    }

    //Insert method for making an entry to the habit tracker database

    public void insertHabit(String name, int duration) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HABIT_NAME, name);
        values.put(COLUMN_DURATION, duration);

        db.insert(HabitContract.HabitEntry.TABLE_NAME, "null", values);
    }

    //read method that reads habit_name and duration of entries with habit name as gaming

    public void readHabits(String habitName) {
        db = dbHelper.getReadableDatabase();
        String whereClause = COLUMN_HABIT_NAME + " = ?";
        String[] selectionArgs = {habitName};
        String result = "";
        StringBuilder sb = new StringBuilder();
        String[] projection = {
                COLUMN_HABIT_NAME,
                COLUMN_DURATION};
        Cursor myCursor = db.query(
                HabitContract.HabitEntry.TABLE_NAME,
                projection,
                whereClause,
                selectionArgs,
                null,
                null,
                null);
        myCursor.moveToFirst();
        if (myCursor != null) {
            do {
                for (int i = 0; i < myCursor.getColumnCount(); i++) {
                    result = sb.append(" " + myCursor.getString(i)).toString();
                }
            } while (myCursor.moveToNext());
            Log.v("Result of query ", result);
        }
        myCursor.close();
    }

    //deletes all the entries from the table
    public void deleteEntries() {
        db.delete(HabitContract.HabitEntry.TABLE_NAME, null, null);
    }
}

package android.virtualpostit;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PostItOpenHelper extends SQLiteOpenHelper {

	private static final String SQL_DATE_FORMAT = "YYYY-MM-DD HH:MM:SS.SSS";
	private static final SimpleDateFormat SQL_DATE_FORMATTER = new SimpleDateFormat(SQL_DATE_FORMAT);
    protected static final int DATABASE_VERSION = 3;
    protected static final String POSTIT_DB_NAME = "PostIt";
    protected static final String NOTE_TABLE = "Note"; 
    protected static final String NOTE_TABLE_ID = "Id";
    protected static final String NOTE_TABLE_NOTE = "Note";
    protected static final String NOTE_TABLE_TS = "Timestamp";
    protected final String CREATE_NOTE_TABLE =
            "create table " + NOTE_TABLE + " (" + NOTE_TABLE_ID + 
            " integer primary key autoincrement, " + NOTE_TABLE_NOTE +
            " text not null, " + NOTE_TABLE_TS + " text not null);";
	SQLiteDatabase db = this.getWritableDatabase();
	ContentValues cv = new ContentValues();

    PostItOpenHelper(Context context) {
        super(context, POSTIT_DB_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating Table
        db.execSQL(CREATE_NOTE_TABLE);
    }
    	  

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	public void insertNote(String name, Date timestamp) {
		cv.put(NOTE_TABLE_NOTE, name);
		cv.put(NOTE_TABLE_TS, SQL_DATE_FORMATTER.format(timestamp));
		db.insert(NOTE_TABLE, null, cv);

		db.close();
	}
	
	public int UpdateNote(Note note)
	  {
	   cv.put(NOTE_TABLE_NOTE, note.getName());
	   cv.put(NOTE_TABLE_TS, note.getAge());
	   return db.update(NOTE_TABLE, cv, colID+"=?", 
	    new String []{String.valueOf(note.getID())});   
	  }
	
	
}
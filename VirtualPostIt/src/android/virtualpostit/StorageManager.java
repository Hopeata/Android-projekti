package android.virtualpostit;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StorageManager extends SQLiteOpenHelper {

	private static final String SQL_DATE_FORMAT = "YYYY-MM-DD HH:MM:SS.SSS";
	private static final SimpleDateFormat SQL_DATE_FORMATTER = new SimpleDateFormat(SQL_DATE_FORMAT);
    private static final int DATABASE_VERSION = 3;
    private static final String POSTIT_DB_NAME = "PostIt";
    private static final String NOTE_TABLE = "Note"; 
    private static final String NOTE_TABLE_ID = "Id";
    private static final String NOTE_TABLE_NOTE = "Note";
    private static final String NOTE_TABLE_TS = "Timestamp";
    private final String CREATE_NOTE_TABLE =
            "create table " + NOTE_TABLE + " (" + NOTE_TABLE_ID + 
            " integer primary key autoincrement, " + NOTE_TABLE_NOTE +
            " text not null, " + NOTE_TABLE_TS + " text not null);";
	SQLiteDatabase db = this.getWritableDatabase();
	ContentValues cv = new ContentValues();

    StorageManager(Context context) {
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
	
	public void insertNote(String note, Date timestamp) {
		cv.put(NOTE_TABLE_NOTE, note);
		cv.put(NOTE_TABLE_TS, SQL_DATE_FORMATTER.format(timestamp));
		db.insert(NOTE_TABLE, null, cv);
		db.close();
	}
	
	public int UpdateNote(Note note)
	  {
	   cv.put(NOTE_TABLE_NOTE, note.getNote());
	   cv.put(NOTE_TABLE_TS, SQL_DATE_FORMATTER.format(note.getTimestamp()));
	   return db.update(NOTE_TABLE, cv, NOTE_TABLE_ID + " = ?", 
	    new String []{String.valueOf(note.getId())});   
	  }
	
	public void DeleteNote(Note note)
	  {
	   SQLiteDatabase db=this.getWritableDatabase();
	   db.delete(NOTE_TABLE, NOTE_TABLE_ID + " = ?", new String [] {String.valueOf(note.getId())});
	   db.close();
	  }
	
	
	Cursor getAllNotes()
	  {
	   Cursor cur=db.rawQuery("SELECT " + NOTE_TABLE_NOTE +" from "+ NOTE_TABLE,new String [] {});
	   return cur;
	  }
}
package android.virtualpostit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StorageManager extends SQLiteOpenHelper {

	private static final String SQL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	private static final SimpleDateFormat SQL_DATE_FORMATTER = new SimpleDateFormat(
			SQL_DATE_FORMAT);
	private static final int DATABASE_VERSION = 3;
	private static final String POSTIT_DB_NAME = "PostIt.s3db";
	private static final String NOTE_TABLE = "Note";
	private static final String NOTE_TABLE_ID = "Id";
	private static final String NOTE_TABLE_NOTE = "Note";
	private static final String NOTE_TABLE_TS = "Timestamp";
	private final String CREATE_NOTE_TABLE = "create table " + NOTE_TABLE
			+ " (" + NOTE_TABLE_ID + " integer primary key autoincrement, "
			+ NOTE_TABLE_NOTE + " text not null, " + NOTE_TABLE_TS
			+ " text not null);";
	ContentValues cv = new ContentValues();

	StorageManager(Context context) {
		super(context, POSTIT_DB_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Creating Table
		
		db.execSQL(CREATE_NOTE_TABLE);
		db.close();
	}

	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public void insertNote(Note note) {
		SQLiteDatabase db = this.getWritableDatabase();
		cv.put(NOTE_TABLE_NOTE, note.getText());
		cv.put(NOTE_TABLE_TS, SQL_DATE_FORMATTER.format(note.getTimestamp()));
		db.insert(NOTE_TABLE, null, cv);
		db.close();
	}

	public int UpdateNote(Note note) {
		SQLiteDatabase db = this.getWritableDatabase();
		cv.put(NOTE_TABLE_NOTE, note.getText());
		cv.put(NOTE_TABLE_TS, SQL_DATE_FORMATTER.format(note.getTimestamp()));
		return db.update(NOTE_TABLE, cv, NOTE_TABLE_ID + " = ?",
				new String[] { String.valueOf(note.getId()) });
	}

	public void DeleteNote(Note note) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(NOTE_TABLE, NOTE_TABLE_ID + " = ?",
				new String[] { String.valueOf(note.getId()) });
		db.close();
	}

	public List<Note> getAllNotes() {
		SQLiteDatabase db = this.getWritableDatabase();
		List<Note> allNotes = new ArrayList<Note>();
		Cursor cur = db.rawQuery("SELECT " + NOTE_TABLE_ID + ", " + NOTE_TABLE_NOTE +
				", " + NOTE_TABLE_TS + " from " + NOTE_TABLE, null);

		if (cur != null && cur.moveToFirst()) {
			try {
				do {
					allNotes.add(populateNote(cur));
				} while (cur.moveToNext());
			} catch (Exception e) {
				// Should not end up here
				throw new RuntimeException(e);
			}
			cur.close();
		}	
		return allNotes;

	}

	private Note populateNote(Cursor cursor) throws ParseException {
		int idIndex = cursor.getColumnIndexOrThrow(NOTE_TABLE_ID);
		int noteIndex = cursor.getColumnIndexOrThrow(NOTE_TABLE_NOTE);
		int tsIndex = cursor.getColumnIndexOrThrow(NOTE_TABLE_TS);
		int id = cursor.getInt(idIndex);
		String note = cursor.getString(noteIndex);
		Date ts = SQL_DATE_FORMATTER.parse(cursor.getString(tsIndex));
		return new Note(id, note, ts);

	}
}
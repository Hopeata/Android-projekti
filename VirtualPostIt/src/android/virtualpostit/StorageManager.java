package android.virtualpostit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Valeria
 * T‰m‰ luokka hoitaa vuorovaikutuksen tietokannan kanssa
 */
public class StorageManager extends SQLiteOpenHelper {

	private static final String SQL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	private static final SimpleDateFormat SQL_DATE_FORMATTER = new SimpleDateFormat(
			SQL_DATE_FORMAT);
	private static final int DATABASE_VERSION = 3;
	private static final String POSTIT_DB_NAME = "PostIt.s3db";
	private static final String NOTE_TABLE = "Note";
	private static final String NOTE_TABLE_ID = "Id";
	private static final String NOTE_TABLE_CONTENT = "Note";
	private static final String NOTE_TABLE_TS = "Timestamp";
	private static final String NOTE_TABLE_ADDRESS = "Address";
	private final String CREATE_NOTE_TABLE = "create table " + NOTE_TABLE
			+ " (" + NOTE_TABLE_ID + " integer primary key autoincrement, "
			+ NOTE_TABLE_CONTENT + " text not null, " + NOTE_TABLE_TS
			+ " text not null, " + NOTE_TABLE_ADDRESS + " text);";
	ContentValues cv = new ContentValues();

	StorageManager(Context context) {
		super(context, POSTIT_DB_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_NOTE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	/**
	 * @param note
	 * T‰ss‰ metodissa tallennetaan uuden Noten tiedot kantaan
	 */
	public void insertNote(Note note) {
		SQLiteDatabase db = this.getWritableDatabase();
			cv.put(NOTE_TABLE_CONTENT, note.getContent());
			cv.put(NOTE_TABLE_ADDRESS, note.getAddress());
			cv.put(NOTE_TABLE_TS,
					SQL_DATE_FORMATTER.format(note.getTimestamp()));
			db.insert(NOTE_TABLE, null, cv);
		db.close();
	}

	/**
	 * @param note
	 * @return p‰ivitetty tietokanta
	 * T‰ss‰ metodissa p‰ivitet‰‰n yksitt‰isen Noten tiedot kantaan
	 */
	public int updateNote(Note note) {
		SQLiteDatabase db = this.getWritableDatabase();
		cv.put(NOTE_TABLE_CONTENT, note.getContent());
		cv.put(NOTE_TABLE_ADDRESS, note.getAddress());
		cv.put(NOTE_TABLE_TS, SQL_DATE_FORMATTER.format(new Date()));
		return db.update(NOTE_TABLE, cv, NOTE_TABLE_ID + " = ?",
				new String[] { String.valueOf(note.getId()) });
	}

	/**
	 * @param note
	 * Poistetaan yksitt‰isen Noten tiedot kannasta
	 */
	public void deleteNote(Note note) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(NOTE_TABLE, NOTE_TABLE_ID + " = ?",
				new String[] { String.valueOf(note.getId()) });
		db.close();
	}

	public Note getNote(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		Note note = null;
		Cursor cur = db.rawQuery("SELECT " + NOTE_TABLE_ID + ", "
				+ NOTE_TABLE_CONTENT + ", " + NOTE_TABLE_TS + ", "
				+ NOTE_TABLE_ADDRESS + " from " + NOTE_TABLE + " where "
				+ NOTE_TABLE_ID + "=" + id + "", null);
		if (cur != null && cur.moveToFirst()) {
			try {
				note = populateNote(cur);
			} catch (Exception e) {
				// Should not end up here
				throw new RuntimeException(e);
			}
			cur.close();
		}
		return note;
	}

	public List<Note> getAllNotes() {
		SQLiteDatabase db = this.getWritableDatabase();
		List<Note> allNotes = new ArrayList<Note>();
		Cursor cur = db.rawQuery("SELECT " + NOTE_TABLE_ID + ", "
				+ NOTE_TABLE_CONTENT + ", " + NOTE_TABLE_TS + ", "
				+ NOTE_TABLE_ADDRESS + " from " + NOTE_TABLE, null);

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
		Collections.sort(allNotes);
		return allNotes;

	}

	private Note populateNote(Cursor cursor) throws ParseException {
		int idIndex = cursor.getColumnIndexOrThrow(NOTE_TABLE_ID);
		int noteIndex = cursor.getColumnIndexOrThrow(NOTE_TABLE_CONTENT);
		int tsIndex = cursor.getColumnIndexOrThrow(NOTE_TABLE_TS);
		int addressIndex = cursor.getColumnIndexOrThrow(NOTE_TABLE_ADDRESS);
		int id = cursor.getInt(idIndex);
		String note = cursor.getString(noteIndex);
		Date ts = SQL_DATE_FORMATTER.parse(cursor.getString(tsIndex));
		String address = cursor.getString(addressIndex);
		return new Note(id, note, ts, address);
	}
}
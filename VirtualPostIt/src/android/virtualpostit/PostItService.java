package android.virtualpostit;

import java.util.Date;
import java.util.List;

import android.content.Context;

/**
 * @author Valeria
 * Tämä luokka toimii linkkinä StorageManagerin ja käyttöliittymän välillä.
 */
public class PostItService {

	private StorageManager sm;

	public PostItService(Context context) {
		sm = new StorageManager(context);
	}

	public Note getNote(int id) {
		return sm.getNote(id);
	}

	public List<Note> getAllNotes() {
		return sm.getAllNotes();
	}

	public void makeNewNote(String text, String address) {
		Note note = new Note(-1, text, new Date(), address);
		sm.insertNote(note);
	}

	public void deleteNote(Note note) {
		sm.deleteNote(note);
	}
	
	public void updateNote(Note note) {
		sm.updateNote(note);
	}

}

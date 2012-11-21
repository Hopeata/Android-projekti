package android.virtualpostit;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

public class NoteArrayAdapter extends ArrayAdapter<String> {

	private final List<Note> notes;
	
	public NoteArrayAdapter(Context context, List<Note> notes) {
		super(context, R.layout.list_row);
		for (Note note : notes) {
			add(note.getText().substring(0, 2) + "...");
		}
		this.notes = notes;
	}
	
	public Note getNote(int position) {
		return notes.get(position);
	}


}
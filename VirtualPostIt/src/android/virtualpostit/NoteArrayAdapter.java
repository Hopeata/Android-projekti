package android.virtualpostit;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ArrayAdapter;

public class NoteArrayAdapter extends ArrayAdapter<String> {

	private final List<Note> notes;
	private static final SimpleDateFormat SDF = new SimpleDateFormat("dd.MM."); 
	private static final int PREVIEW_CONTENT_LENGTH = 27;
	
	public NoteArrayAdapter(Context context, List<Note> notes) {
		super(context, R.layout.list_row);
		notes.add(0, new Note(-1, "New Note", null, null));
		for (Note note : notes) {
			String previewText = note.getTimestamp() != null ? SDF.format(note.getTimestamp()) + " " : "";
			if (note.getContent().length() > PREVIEW_CONTENT_LENGTH) {
				previewText += note.getContent().substring(0, PREVIEW_CONTENT_LENGTH - 3) + "...";				
			} else {
				previewText += note.getContent();
			}
			add(previewText);
		}
		
		this.notes = notes;
	}
	
	public Note getNote(int position) {
		return notes.get(position);
	}


}
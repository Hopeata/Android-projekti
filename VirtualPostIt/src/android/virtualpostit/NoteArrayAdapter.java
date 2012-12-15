package android.virtualpostit;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Valeria
 * T�m� apuluokka m��rittelee, milt� viestin sis�lt� n�ytt�� listan�kym�ss�
 */
public class NoteArrayAdapter extends ArrayAdapter<String> {

	private final List<Note> notes;
	private static final SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.");
	private static final int PREVIEW_CONTENT_LENGTH = 30;

	public NoteArrayAdapter(Context context, List<Note> notes) {
		super(context, R.layout.list_row);
		notes.add(0, new Note(-1, "New Note", null, null));
		this.notes = notes;
	}
	
	@Override
	public int getCount() {
		return notes.size();
	}

	/**
	 * @param note
	 * @return Listan�kym�ss� n�kyv� teksti
	 * T�ss� metodissa luodaan se tekstinp�tk� muistutuksesta, joka n�kyy listassa
	 */
	private String getPreviewText(Note note) {
		String previewText = note.getTimestamp() != null ? SDF.format(note
				.getTimestamp()) + " " : "";
		if (note.getContent().length() > PREVIEW_CONTENT_LENGTH) {
			previewText += note.getContent().substring(0,
					PREVIEW_CONTENT_LENGTH - 3)
					+ "...";
		} else {
			previewText += note.getContent();
		}
		return previewText;
	}

	public Note getNote(int position) {
		return notes.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 * T�m�n metodin avulla saadaan my�s nuppineula n�kyviin listaan
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.list_row, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.rowTextView);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.pushpin);
		Note note = getNote(position);
		textView.setText(getPreviewText(note));
		if (note.getAddress() != null && !note.getAddress().equals("")) {
			imageView.setImageResource(R.drawable.pushpin);
		}
		return rowView;
	}

}
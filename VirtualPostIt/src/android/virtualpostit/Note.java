package android.virtualpostit;

import java.util.Date;

public class Note {

	String note = "";
	Date timestamp;
	int id = 0;

	public Note(int id, String note, Date timestamp) {
		this.note = note;
		this.timestamp = timestamp;
	}


	public int getId() {
		return id;
	}

	public String getText() {
		return note;
	}

	public Date getTimestamp() {
		return timestamp;
	}

}

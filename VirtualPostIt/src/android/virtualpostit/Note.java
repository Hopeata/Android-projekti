package android.virtualpostit;

import java.util.Date;

public class Note implements Comparable<Note> {
	
	private int id;
	private String content = "";
	private Date timestamp;

	public Note(int id, String content, Date timestamp) {
		this.id = id;
		this.content = content;
		this.timestamp = timestamp;
	}


	public int getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public Date getTimestamp() {
		return timestamp;
	}
	
	public void setContent(String content) {
		this.content = content;
	}


	public int compareTo(Note another) {
		return timestamp.compareTo(another.timestamp) * -1;
	}

}

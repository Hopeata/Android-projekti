package android.virtualpostit;

import java.util.Date;

/**
 * @author Valeria
 * Tämä luokka on yksittäinen muistilappu ja sen sisältämät tiedot
 */
public class Note implements Comparable<Note> {
	
	private int id;
	private String content = "";
	private Date timestamp;
	private String address = "";

	public Note(int id, String content, Date timestamp, String address) {
		this.id = id;
		this.content = content;
		this.timestamp = timestamp;
		this.address = address;
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
	
	public String getAddress() {
		return address;
	}
	
	public void setContent(String content) {
		this.content = content;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int compareTo(Note another) {
		return timestamp.compareTo(another.timestamp) * -1;
	}

}

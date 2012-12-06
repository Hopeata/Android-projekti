package android.virtualpostit;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.google.android.maps.GeoPoint;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class NoteViewActivity extends Activity {

	public static final String EditID = "android.virtualpostit.NoteViewActivity.EditID";
	public static final String ADDRESS = "android.virtualpostit.NoteViewActivity.Address";
	public static final String CONTENT = "android.virtualpostit.NoteViewActivity.Content";
	private static final SimpleDateFormat SDF = new SimpleDateFormat(
			"dd.MM.yyyy HH:mm");

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_view);

		Intent intent = getIntent();
		int id = intent.getIntExtra(PostIt.ID, -1);
		final Note note = PostIt.POST_IT_SERVICE.getNote(id);

		TextView textView = (TextView) findViewById(R.id.viewNote);
		TextView timestampView = (TextView) findViewById(R.id.lblTimestamp);
		TextView addressView = (TextView) findViewById(R.id.lblAddress);

		if (note != null) {
			textView.setText(note.getContent());
			timestampView.setText(SDF.format(note.getTimestamp()));
			final String address = note.getAddress();
			if (address != null) {
				SpannableString content = new SpannableString(address);
				content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
				addressView.setText(content);
				addressView.setTextColor(Color.BLUE);
				addressView.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						Intent intent = new Intent(NoteViewActivity.this,
								GMapActivity.class);
						intent.putExtra(ADDRESS, address);
						intent.putExtra(CONTENT, note.getContent());
						intent.putExtra(GMapActivity.ACTION_TYPE, GMapActivity.GET_LOCATION_ACTION);
						startActivity(intent); 
					}
				});
			}
		}

		Button edit = (Button) findViewById(R.id.btnEdit);
		Button delete = (Button) findViewById(R.id.btnDelete);
		Button cancel = (Button) findViewById(R.id.btnCancel);

		edit.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(NoteViewActivity.this,
						NoteEditActivity.class);
				intent.putExtra(EditID, note.getId());
				startActivity(intent);
				finish();
			}
		});

		delete.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				PostIt.POST_IT_SERVICE.deleteNote(note);
				finish();
			}
		});

		cancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});

	}
	
}
package android.virtualpostit;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class NoteEditActivity extends Activity {

	public static String EditID = "android.virtualpostit.NoteEditActivity.EditID";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_edit);

		Intent intent = getIntent();
		int id = intent.getIntExtra(PostIt.ID, -1);
		final Note note = PostIt.POST_IT_SERVICE.getNote(id);

		TextView textView = (TextView) findViewById(R.id.viewNote);
		textView.setTextSize(40);
		if (note != null) {
			textView.setText(note.getContent());
		}

		Button edit = (Button) findViewById(R.id.btnEdit);
		Button delete = (Button) findViewById(R.id.btnDelete);
		Button cancel = (Button) findViewById(R.id.btnCancel);

		edit.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(NoteEditActivity.this,
						NoteActivity.class);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_note_edit, menu);
		return true;
	}

	/*
	 * @Override public boolean onOptionsItemSelected(MenuItem item) { switch
	 * (item.getItemId()) { case android.R.id.home:
	 * NavUtils.navigateUpFromSameTask(this); return true; } return
	 * super.onOptionsItemSelected(item); }
	 */
}

package android.virtualpostit;

import android.os.Bundle;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NoteEditActivity extends Activity {

	public static final String ADDRESS = "android.virtualpostit.NoteEditActivity.ADDRESS";
	public static final int SELECT_LOCATION_REQUEST_CODE = 1;
	private EditText editAddress;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_note_edit);

		Intent intent = getIntent();
		final int id = intent.getIntExtra(NoteViewActivity.EDIT_ID, -1);
		final Note note = PostIt.POST_IT_SERVICE.getNote(id);
		Button save = (Button) findViewById(R.id.btnSave);
		Button coordinates = (Button) findViewById(R.id.btnAddress);
		Button cancel = (Button) findViewById(R.id.btnCancel);
		final EditText editNote = (EditText) findViewById(R.id.txtNote);
		editAddress = (EditText) findViewById(R.id.txtAddress);

		if (id != -1) {
			editNote.setText(note.getContent());
			editAddress.setText(note.getAddress());
		}
		
		save.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				if (id == -1) {
					String noteText = editNote.getText().toString();
					if (!noteText.equals("")) {
					PostIt.POST_IT_SERVICE.makeNewNote(noteText, editAddress.getText().toString());
					finish();
					} else {
						Toast toast = Toast.makeText(getApplicationContext(), "Can't save empty note",
								Toast.LENGTH_LONG);
						toast.show();
					}
				} else {
					note.setContent(editNote.getText().toString());
					note.setAddress(editAddress.getText().toString());
					PostIt.POST_IT_SERVICE.updateNote(note);
					finish();
				}

			}
		});

		coordinates.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(NoteEditActivity.this,
						GMapActivity.class);
				intent.putExtra(GMapActivity.ACTION_TYPE,
						GMapActivity.SELECT_LOCATION_ACTION);
				startActivityForResult(intent, SELECT_LOCATION_REQUEST_CODE);
			}
		});

		cancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SELECT_LOCATION_REQUEST_CODE && data != null) {

			String result = data.getStringExtra(ADDRESS);
			if (result != null) {
				editAddress.setText(result);
			}

		}
	}

}

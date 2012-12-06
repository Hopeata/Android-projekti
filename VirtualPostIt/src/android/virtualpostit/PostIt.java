package android.virtualpostit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.Arrays;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PostIt extends ListActivity {

	private ListView mainListView;
	private NoteArrayAdapter listAdapter;
	public static PostItService POST_IT_SERVICE;
	public static String ID = "android.virtualpostit.PostIt.ID";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.title_activity_post_it);
		POST_IT_SERVICE = new PostItService(getApplicationContext());

	}

	@Override
	protected void onResume() {
		List<Note> allNotes = POST_IT_SERVICE.getAllNotes();

		listAdapter = new NoteArrayAdapter(this, allNotes);
		setListAdapter(listAdapter);
		super.onResume();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent;
		int noteID = listAdapter.getNote(position).getId();
		if (noteID == -1) {
			intent = new Intent(PostIt.this, NoteEditActivity.class);
		} else {
			intent = new Intent(PostIt.this, NoteViewActivity.class);

		}
		intent.putExtra(ID, noteID);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_post_it, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_map_view:
			Intent intent = new Intent(PostIt.this,
					GMapActivity.class);
			intent.putExtra(GMapActivity.ACTION_TYPE,
					GMapActivity.NOTES_LOCATION_ACTION);
			startActivity(intent);
			return true;
		case R.id.menu_quit:
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}

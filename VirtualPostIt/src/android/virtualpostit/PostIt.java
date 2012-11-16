package android.virtualpostit;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class PostIt extends Activity {

	private Button cancel;
	private PostItService pi;
	private static final String TAG = "PostIt";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_it);
		pi = new PostItService(getApplicationContext());

		cancel = (Button) findViewById(R.id.btnCancel);
		cancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				//Log.v(TAG, "Count 1: " + pi.getAllNotes().size());
				pi.makeNewNote("hei");
				
				Log.v(TAG, "Count 2	: " + pi.getAllNotes().size());
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_post_it, menu);
		return true;
	}

}

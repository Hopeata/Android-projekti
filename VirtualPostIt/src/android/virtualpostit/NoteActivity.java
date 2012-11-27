package android.virtualpostit;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NoteActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        
        setContentView(R.layout.activity_note);	
        
		Intent intent = getIntent();
		final int id = intent.getIntExtra(NoteEditActivity.EditID, -1);
        final Note note = PostIt.POST_IT_SERVICE.getNote(id);
		Button save = (Button) findViewById(R.id.btnSave);
		Button cancel = (Button) findViewById(R.id.btnCancel);

		final EditText editNote = (EditText) findViewById(R.id.txtNote);
		
		if (id != -1) {
			editNote.setText(note.getContent());						
		}
		
		save.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				
				if (id == -1) {
					PostIt.POST_IT_SERVICE.makeNewNote(editNote.getText().toString());
					Intent intent = new Intent(NoteActivity.this, PostIt.class);
					startActivity(intent);
					finish();
				}

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
        getMenuInflater().inflate(R.menu.activity_note, menu);
        return true;
    }
}

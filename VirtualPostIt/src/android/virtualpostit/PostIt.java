package android.virtualpostit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PostIt extends Activity {

	  private ListView mainListView ;  
	  private NoteArrayAdapter listAdapter ; 
	  public static PostItService POST_IT_SERVICE; 
	    
	  /** Called when the activity is first created. */  
	  @Override  
	  public void onCreate(Bundle savedInstanceState) {  
	    super.onCreate(savedInstanceState);  
	    
	    POST_IT_SERVICE = new PostItService(getApplicationContext());
	    
	    setContentView(R.layout.activity_post_it);  
	      
	    // Find the ListView resource.   
	    mainListView = (ListView) findViewById(R.id.mainListView );  
	  
	    
	    List<Note> allNotes = POST_IT_SERVICE.getAllNotes();

	    listAdapter = new NoteArrayAdapter(this, allNotes);
	    
	    mainListView.setAdapter( listAdapter );        

	    	     
		mainListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				 Toast.makeText(getApplicationContext(),
						listAdapter.getNote(position).getText(), Toast.LENGTH_SHORT).show();
		//		Intent intent = new Intent(PostIt.this, NoteActivity.class);
		//		startActivity(intent);
			}
		});
	      
	  }  
	  
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_post_it, menu);
		return true;
	}

}

package android.virtualpostit;

import java.util.ArrayList;

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

public class PostIt extends ListActivity {

	  private ListView mainListView ;  
	  private ArrayAdapter<String> listAdapter ;  
	    
	  /** Called when the activity is first created. */  
	  @Override  
	  public void onCreate(Bundle savedInstanceState) {  
	    super.onCreate(savedInstanceState);  
	    
	    setContentView(R.layout.activity_list);  
	      
	    // Find the ListView resource.   
	    mainListView = (ListView) findViewById(R.id.mainListView );  
	  
	    // Create and populate a List of planet names.  
	    String[] planets = new String[] { "Mercury", "Venus", "Earth", "Mars",  
	                                      "Jupiter", "Saturn", "Uranus", "Neptune"};    
	    ArrayList<String> planetList = new ArrayList<String>();  
	    planetList.addAll( Arrays.asList(planets) );  
	      
	    // Create ArrayAdapter using the planet list.  
	    listAdapter = new ArrayAdapter<String>(this, R.layout.list_row, planetList); 
	    mainListView.setAdapter( listAdapter );        

	    	     
		mainListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			    // When clicked, show a toast with the TextView text
				Intent intent = new Intent(PostIt.this, NoteActivity.class);
				startActivity(intent);
			}
		});
	      
	  }  
	  
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_post_it, menu);
		return true;
	}

}

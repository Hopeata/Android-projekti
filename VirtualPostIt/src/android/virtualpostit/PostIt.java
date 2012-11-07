package android.virtualpostit;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class PostIt extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_it);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_post_it, menu);
        return true;
    }
}

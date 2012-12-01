package android.virtualpostit;

import java.io.IOException;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Menu;

public class GMapActivity extends MapActivity {

	private static Geocoder coder;
	private MapView mapView;
	private GeoPoint p = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gmap);

		mapView = (MapView) findViewById(R.id.map_view);
		mapView.setBuiltInZoomControls(true);

		coder = new Geocoder(this);
		Intent intent = getIntent();
		String address = intent.getStringExtra(NoteEditActivity.ADDRESS);

		p = getLocationFromAddress(address);
		MapController mc = mapView.getController();

		mc.animateTo(p);
		mc.setZoom(17);
		
        //---Add a location marker---
        MapOverlay mapOverlay = new MapOverlay();
        List<Overlay> listOfOverlays = mapView.getOverlays();
        listOfOverlays.clear();
        listOfOverlays.add(mapOverlay);
        
		mapView.invalidate();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_gmap, menu);
		return true;
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	private static GeoPoint getLocationFromAddress(String address) {
		List<Address> addresses;

		try {
			addresses = coder.getFromLocationName(address, 5);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		if (addresses == null) {
			return null;
		}

		Address location = addresses.get(0);
		location.getLatitude();
		location.getLongitude();

		GeoPoint p1 = new GeoPoint((int) (location.getLatitude() * 1E6),
				(int) (location.getLongitude() * 1E6));

		return p1;

	}

	class MapOverlay extends Overlay {
		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
				long when) {
			super.draw(canvas, mapView, shadow);

			// ---translate the GeoPoint to screen pixels---
			Point screenPts = new Point();
			mapView.getProjection().toPixels(p, screenPts);

			// ---add the marker---
			Bitmap bmp = BitmapFactory.decodeResource(getResources(),
					R.drawable.pushpin);
			canvas.drawBitmap(bmp, screenPts.x, screenPts.y - 32, null);
			return true;
		}
	}

}

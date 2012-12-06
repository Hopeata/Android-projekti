package android.virtualpostit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.widget.Toast;

public class GMapActivity extends MapActivity {

	public static final String ACTION_TYPE = "android.virtualpostit.GMapActivity.actionType";
	public static final String GET_LOCATION_ACTION = "get location";
	public static final String SELECT_LOCATION_ACTION = "select location";
	public static final String NOTES_LOCATION_ACTION = "notes location";
	private static Geocoder coder;
	private MapView mapView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gmap);

		mapView = (MapView) findViewById(R.id.map_view);
		mapView.setBuiltInZoomControls(true);

		Intent intent = getIntent();
		String action = intent.getStringExtra(ACTION_TYPE);

		final List<Overlay> listOfOverlays = mapView.getOverlays();
		listOfOverlays.clear();

		coder = new Geocoder(this);
		final MapController mc = mapView.getController();
		mc.setZoom(17);

		if (action.equals(GET_LOCATION_ACTION)) {

			String address = intent.getStringExtra(NoteViewActivity.ADDRESS);
			// String note = intent.getStringExtra(NoteViewActivity.CONTENT);

			GeoPoint p = getLocationFromAddress(address);
			mc.animateTo(p);

			// ---Add a location marker---
			MapOverlay mapOverlay = new MapOverlay();
			mapOverlay.p = p;
			listOfOverlays.add(mapOverlay);

			mapView.invalidate();

		} else if (action.equals(SELECT_LOCATION_ACTION)) {
			MyLocationOverlay myLocationOverlay = new MyLocationOverlay(this,
					mapView) {
				public synchronized void onLocationChanged(Location location) {
					super.onLocationChanged(location);
					int lat = (int) (location.getLatitude() * 1E6);
					int lng = (int) (location.getLongitude() * 1E6);
					GeoPoint point = new GeoPoint(lat, lng);					
					mc.animateTo(point);
				};
			};
			myLocationOverlay.enableMyLocation();
			listOfOverlays.add(myLocationOverlay);
			
			
			SelectionMapOverlay selectionMapOverlay = new SelectionMapOverlay();
			selectionMapOverlay.setGestureDetector(new GestureDetector(
					new MapTouchDetector()));
			listOfOverlays.add(selectionMapOverlay);

		} else if (action.equals(NOTES_LOCATION_ACTION)) {
			Drawable drawable = getResources().getDrawable(R.drawable.pushpin);
			NotePopUpItemizedOverlay itemizedOverlay = new NotePopUpItemizedOverlay(drawable, mapView);

			
			
			GeoPoint point = new GeoPoint((int)(51.5174723*1E6),(int)(-0.0899537*1E6));
			OverlayItem overlayItem = new OverlayItem(point, "Tomorrow Never Dies (1997)", 
					"(M gives Bond his mission in Daimler car)");
			itemizedOverlay.addOverlay(overlayItem);
			
			GeoPoint point2 = new GeoPoint((int)(51.515259*1E6),(int)(-0.086623*1E6));
			OverlayItem overlayItem2 = new OverlayItem(point2, "GoldenEye (1995)", 
					"(Interiors Russian defence ministry council chambers in St Petersburg)");		
			itemizedOverlay.addOverlay(overlayItem2);
			
			listOfOverlays.add(itemizedOverlay);
			
			mc.animateTo(point2);
		}

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

		private GeoPoint p = null;

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

	class SelectionMapOverlay extends Overlay {

		private GestureDetector gestureDetector;

		@Override
		public boolean onTouchEvent(MotionEvent event, MapView mapView) {
			// when the user lifts its finger
			if (gestureDetector.onTouchEvent(event)) {
				return true;
			}

			return false;
		}

		public GestureDetector getGestureDetector() {
			return gestureDetector;
		}

		public void setGestureDetector(GestureDetector gestureDetector) {
			this.gestureDetector = gestureDetector;
		}
	}

	class MapTouchDetector extends SimpleOnGestureListener {

		@Override
		public boolean onSingleTapConfirmed(MotionEvent event) {

			GeoPoint p = mapView.getProjection().fromPixels((int) event.getX(),
					(int) event.getY());

			// ---reverse geocoding---

			List<Address> addresses;
			try {
				addresses = coder.getFromLocation(p.getLatitudeE6() / 1E6,
						p.getLongitudeE6() / 1E6, 1);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			String add = "";
			if (addresses.size() > 0) {
				int lastIndex = addresses.get(0).getMaxAddressLineIndex();
				for (int i = 0; i < lastIndex; i++) {
					add += addresses.get(0).getAddressLine(i);
					if (i < lastIndex - 1) {
						add += ", ";
					}
				}
			}
			Intent returnIntent = new Intent(GMapActivity.this,
					NoteEditActivity.class);
			returnIntent.putExtra(NoteEditActivity.ADDRESS, add);
			setResult(NoteEditActivity.SELECT_LOCATION_REQUEST_CODE,
					returnIntent);
			finish();
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			return super.onFling(e1, e2, velocityX, velocityY);
		}

		@Override
		public boolean onDown(MotionEvent e) {
			return false;
		}
	}

}

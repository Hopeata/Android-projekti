package android.virtualpostit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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
	private List<Toast> toasts;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gmap);

		mapView = (MapView) findViewById(R.id.map_view);
		mapView.setBuiltInZoomControls(true);

		Intent intent = getIntent();
		String action = intent.getStringExtra(ACTION_TYPE);
		
		toasts = new ArrayList<Toast>();

		final List<Overlay> listOfOverlays = mapView.getOverlays();
		listOfOverlays.clear();

		coder = new Geocoder(this);
		final MapController mc = mapView.getController();
		mc.setZoom(17);

		if (action.equals(GET_LOCATION_ACTION)) {

			int noteId = intent.getIntExtra(NoteViewActivity.NOTE_ID, -1);
			Note note = PostIt.POST_IT_SERVICE.getNote(noteId);
			NotePopUpItemizedOverlay notePopUpOverlay = getNotePopUpOverlay(mc,
					Arrays.asList(note));
			if (notePopUpOverlay != null) {
				listOfOverlays.add(notePopUpOverlay);
			}
			GeoPoint p = getLocationFromAddress(note.getAddress());
			if (p != null) {
				mc.animateTo(p);
			}

		} else if (action.equals(SELECT_LOCATION_ACTION)) {
			listOfOverlays.add(getMyLocationOverlay(mc));

			SelectionMapOverlay selectionMapOverlay = new SelectionMapOverlay();
			selectionMapOverlay.setGestureDetector(new GestureDetector(
					new MapTouchDetector()));
			listOfOverlays.add(selectionMapOverlay);

		} else if (action.equals(NOTES_LOCATION_ACTION)) {
			listOfOverlays.add(getMyLocationOverlay(mc));

			List<Note> notes = PostIt.POST_IT_SERVICE.getAllNotes();
			NotePopUpItemizedOverlay notePopUpOverlay = getNotePopUpOverlay(mc,
					notes);
			if (notePopUpOverlay != null) {
				listOfOverlays.add(notePopUpOverlay);
			}
		}
		
		showToasts();

	}

	private NotePopUpItemizedOverlay getNotePopUpOverlay(
			final MapController mc, List<Note> notes) {

		Drawable drawable = getResources().getDrawable(R.drawable.pushpin);
		NotePopUpItemizedOverlay notePopUpOverlay = new NotePopUpItemizedOverlay(
				drawable, mapView);

		boolean notesAdded = false;

		for (Note note : notes) {
			if (note.getAddress() != null && !note.getAddress().equals("")) {
				GeoPoint p = getLocationFromAddress(note.getAddress());
				if (p != null) {
					OverlayItem overlayItem = new OverlayItem(p,
							note.getContent(), NoteViewActivity.SDF.format(note
									.getTimestamp()));
					notePopUpOverlay.addOverlay(overlayItem);
					notesAdded = true;
				} else {
					addInvalidLocationToast(note.getAddress());
				}
			}
		}
		if (!notesAdded) {
			return null;
		}
		return notePopUpOverlay;
	}

	private void addInvalidLocationToast(String address) {
		Toast toast = Toast.makeText(getApplicationContext(),
				"Sorry, can't find " + address, Toast.LENGTH_LONG);
		toasts.add(toast);
	}

	private void showToasts() {
		for (Toast toast : toasts) {
			toast.show();
		}
	}

	private MyLocationOverlay getMyLocationOverlay(final MapController mc) {
		MyLocationOverlay myLocationOverlay = new MyLocationOverlay(this,
				mapView) {
			private boolean firstFix = true;

			public synchronized void onLocationChanged(Location location) {
				super.onLocationChanged(location);
				if (firstFix) {
					mc.animateTo(this.getMyLocation());
					firstFix = false;
				}
			};
		};
		myLocationOverlay.enableMyLocation();
		return myLocationOverlay;
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

		if (addresses == null || addresses.isEmpty()) {
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

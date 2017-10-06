package alek.examen;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.PermissionChecker;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

import alek.examen.model.Description;

public class MainActivity extends AppCompatActivity {

    private MapView mapView;
    private static final double[] LAT_LONG = { 51.180848, 4.60841 };
    private static final String GEOPOINT_LAT = "latitude";
    private static final String GEOPOINT_LON = "longitude";
    private MySQLiteHelper helper;
    private ArrayList<OverlayItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView = (MapView)findViewById(R.id.mapview);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(16);
        mapView.getController().setCenter(new GeoPoint( LAT_LONG[0], LAT_LONG[1]));
        helper = new MySQLiteHelper(this);
        items = new ArrayList<OverlayItem>();

        for (Description d: helper.findDescriptions()) {
            addMarker(d.getLatitude(), d.getLongitude());
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int actionType = ev.getAction();
        switch(actionType) {
            case MotionEvent.ACTION_UP: {
                Projection proj = this.mapView.getProjection();
                GeoPoint loc = (GeoPoint)proj.fromPixels((int)ev.getX(), (int)ev.getY());

                //findZone(loc);
                moveToNextScreen(loc);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void moveToNextScreen(GeoPoint loc) {
        Intent nextScreenIntent = new Intent(this, AddDescriptionActivity.class);
        nextScreenIntent.putExtra(GEOPOINT_LAT, loc.getLatitude());
        nextScreenIntent.putExtra(GEOPOINT_LON, loc.getLongitude());
        startActivity(nextScreenIntent);
    }

    private void addMarker(double latitude, double longitude) {
        OverlayItem myLocationOverlayItem = new OverlayItem("Here", "Current Position", new GeoPoint(latitude, longitude));
        Drawable myCurrentLocationMarker = ResourcesCompat.getDrawable(getResources(), R.drawable.marker, null);
        myLocationOverlayItem.setMarker(myCurrentLocationMarker);

        items.add(myLocationOverlayItem);
        DefaultResourceProxyImpl resourceProxy = new DefaultResourceProxyImpl(getApplicationContext());

        ItemizedIconOverlay<OverlayItem> currentLocationOverlay = new ItemizedIconOverlay<OverlayItem>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        return true;
                    }
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return true;
                    }
                }, resourceProxy);
        this.mapView.getOverlays().add(currentLocationOverlay);
        this.mapView.invalidate();
    }
}

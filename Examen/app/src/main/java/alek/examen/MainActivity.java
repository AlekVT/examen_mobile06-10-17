package alek.examen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;

public class MainActivity extends AppCompatActivity {

    private MapView mapView;
    private static final double[] LAT_LONG = { 51.180848, 4.60841 };

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
        Toast.makeText(this, loc.toDoubleString(), Toast.LENGTH_LONG).show();
        //Intent nextScreenIntent = new Intent(this, );
        /*if (pictureTaken) {
            Intent nextScreenIntent = new Intent(this, EnterTextActivity.class);
            nextScreenIntent.putExtra(IMAGE_URI_KEY, selectedPhotoPath);
            nextScreenIntent.putExtra(BITMAP_WIDTH, takePictureImageView.getWidth());
            nextScreenIntent.putExtra(BITMAP_HEIGHT, takePictureImageView.getHeight());

            startActivity(nextScreenIntent);
        } else {
            Toaster.show(this, R.string.select_a_picture);
        }*/
    }
}

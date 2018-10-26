package io.codefundo.rmd.dmapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.cachemanager.CacheManager;
import org.osmdroid.tileprovider.modules.SqliteArchiveTileWriter;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MapFragment extends Fragment {

    private static final String TAG = MapFragment.class.getName();
    private MapView mapView;
    private RotationGestureOverlay mRotationGestureOverlay;
    private SqliteArchiveTileWriter sqliteArchiveTileWriter;
    private CacheManager cacheManager;

    public MapFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Context ctx = getContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        mapView = rootView.findViewById(R.id.maps_mv);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setScrollableAreaLimitDouble(new BoundingBox(-85, 180, 85, -180));
        mapView.setMinZoomLevel(3.0);
        mapView.setBuiltInZoomControls(false);
        mapView.setMultiTouchControls(true);

        mRotationGestureOverlay = new RotationGestureOverlay(mapView);
        mRotationGestureOverlay.setEnabled(true);
        mapView.setMultiTouchControls(true);
        //mapView.setUseDataConnection(false);
        mapView.getOverlays().add(mRotationGestureOverlay);

        MyLocationNewOverlay myLocationNewOverlay = new MyLocationNewOverlay(mapView);
        //myLocationNewOverlay.enableMyLocation();
        myLocationNewOverlay.enableFollowLocation();
        mapView.getOverlays().add(myLocationNewOverlay);
        mapView.getController().zoomIn();


        TelephonyManager telephonyManager =
                (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        GsmCellLocation cellLocation;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ctx.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                LocationManager locationManager =
                        (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
                LocationListener locationListener = new MyLocationListener();
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
                final Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                String longitude = "Longitude: " + loc.getLongitude();
                Log.v(TAG, longitude);
                String latitude = "Latitude: " + loc.getLatitude();
                Log.v(TAG, latitude);

                mapView.getController().zoomTo(14.0);
                mapView.zoomToBoundingBox(new BoundingBox(
                        loc.getLatitude()+1,
                        loc.getLongitude()+1,
                        loc.getLatitude()+0,
                        loc.getLongitude()+0),true);
                mapView.invalidate();
            }
        } else {
            LocationManager locationManager =
                    (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new MyLocationListener();
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
            final Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            String longitude = "Longitude: " + loc.getLongitude();
            Log.v(TAG, longitude);
            mapView.getController().animateTo((int) loc.getLatitude(), (int) loc.getLongitude());
            String latitude = "Latitude: " + loc.getLatitude();
            Log.v(TAG, latitude);

            mapView.getController().zoomTo(3.0);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mapView.getController()
                            .animateTo((int) loc.getLatitude(), (int) loc.getLongitude());
                }
            }, 1000);
        }

        if(isNetworkAvailable())
            downloader();
        return rootView;
    }

    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void downloader() {
        try {
            String outputName = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    File.separator + "osmdroid" +
                    File.separator + "maps";
            sqliteArchiveTileWriter = new SqliteArchiveTileWriter(outputName);
            cacheManager = new CacheManager(mapView, sqliteArchiveTileWriter);
            double n = 12.926497;
            double e = 74.829270;
            double s = 12.925163;
            double w = 74.828640;
            List<GeoPoint> locations = new ArrayList<>();
            locations.add(new GeoPoint(12.897730, 74.842054));
            locations.add(new GeoPoint(12.899974, 74.846170));
            locations.add(new GeoPoint(12.967230, 77.501327));
            BoundingBox bb = new BoundingBox(n, e, s, w);
            Log.d(TAG,"max " + mapView.getMaxZoomLevel());
            int zoommin = 22;
            int zoommax = 22;
            Log.v(TAG, "Zoom min " + zoommin);
            Log.v(TAG, "Zoom max " + zoommax);
            int tilecount = cacheManager.possibleTilesInArea(bb, zoommin, zoommax);
            Log.v(TAG, "Map download tile count : " + tilecount);
            //Download trigger
            cacheManager.downloadAreaAsyncNoUI(getActivity(),
                    bb,
                    zoommin,
                    zoommax,
                    new CacheManager.CacheManagerCallback() {
                        @Override
                        public void onTaskComplete() {
                            Snackbar.make(getView(), "Download complete", Snackbar.LENGTH_SHORT)
                                    .show();
                            if (sqliteArchiveTileWriter != null)
                                sqliteArchiveTileWriter.onDetach();
                        }

                        @Override
                        public void onTaskFailed(int errors) {
                            Snackbar.make(getView(),
                                    "Download complete with " + errors + " errors",
                                    Snackbar.LENGTH_SHORT)
                                    .show();
                            if (sqliteArchiveTileWriter != null)
                                sqliteArchiveTileWriter.onDetach();
                        }

                        @Override
                        public void updateProgress(int progress, int currentZoomLevel, int zoomMin,
                                                   int zoomMax) {
                            //NOOP since we are using the build in UI
                        }

                        @Override
                        public void downloadStarted() {
                            //NOOP since we are using the build in UI
                        }

                        @Override
                        public void setPossibleTilesInArea(int total) {
                            //NOOP since we are using the build in UI
                        }
                    });

        } catch (Exception e) {
            Log.e(TAG, "Map download errorr : " + e.getMessage());
        }
    }

    class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            String longitude = "Longitude: " + loc.getLongitude();
            Log.v(TAG, longitude);
            String latitude = "Latitude: " + loc.getLatitude();
            Log.v(TAG, latitude);
            mapView.getController().animateTo((int) loc.getLatitude(), (int) loc.getLongitude());
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }
}

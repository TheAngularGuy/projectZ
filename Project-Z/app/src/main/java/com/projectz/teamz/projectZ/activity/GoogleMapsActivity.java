package com.projectz.teamz.projectZ.activity;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.projectz.teamz.projectZ.R;
import com.projectz.teamz.projectZ.classUtils.Coordinate;
import com.projectz.teamz.projectZ.classUtils.FileParser;
import com.projectz.teamz.projectZ.classUtils.Utils;
import com.projectz.teamz.projectZ.sensorsListener.GPSTracker;

import java.util.ArrayList;
import java.util.List;


public class GoogleMapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener{

    private GoogleMap       mMap;
    private int             MY_PERMISSION_ACCESS_FINE_LOCATION_REQUEST;
    private double          longitude_ = -1;
    private double          latitude_  = -1;
    private LocationManager locationManager;
    private List<Coordinate> listCoordinate = new ArrayList<>();
    private List<Coordinate> listCoordinateID = new ArrayList<>();
    private List<Marker> listMarker = new ArrayList<>();
    private GPSTracker gps;
    private Button btnRadar;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goole_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigationview3);
        bottomNavigationView.setSelectedItemId(R.id.map);
        bottomNavigationView.setOnNavigationItemSelectedListener(new
         BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.radar:
                        //Toast.makeText(GoogleMapsActivity.this, "Radar", Toast.LENGTH_SHORT).show();
                        Intent radarIntent = new Intent(GoogleMapsActivity.this, RadarActivity.class);
                        radarIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(radarIntent);
                        break;
                    case R.id.map:
                        //Toast.makeText(GoogleMapsActivity.this, "Map", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.settings:
                        //Toast.makeText(GoogleMapsActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                        Intent settingsIntent = new Intent(GoogleMapsActivity.this, SettingActivity.class);
                        settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(settingsIntent);
                        break;
                }
                return true;
            }
        });

        /**
         * Check GPS permission and request if not activated
         * Request location update from current user location
         */
        gps = new GPSTracker(this, 1);
        if(gps.canGetLocation())
        {
            latitude_ = gps.getLatitude();
            longitude_ = gps.getLongitude();
            double orientation = gps.getOrientation();
        }
        btnRadar = (Button) findViewById(R.id.testButtonOnApiGoogleMap);
        btnRadar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent activityChangeIntent =
                        new Intent(GoogleMapsActivity.this, RadarActivity.class);
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                GoogleMapsActivity.this.startActivity(activityChangeIntent);
            }
        });
    }

    private void readAndParseFile(List<Coordinate> list, int opt) {
        String str;
        if (opt == 1)
            str = Utils.readFromFile("ballZ.txt", this);
        else
            str = Utils.readFromFile("ballZid.txt", this);
        if (str != null) {
            FileParser fp = new FileParser(str);

            for (int i = 1; i <= (fp.getHt().size()); i++)
                list.add(new Coordinate(fp.getValue(Integer.toString(i))));
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        /**Sets the map to be "normal"*/
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        /**Set a preference for minimum and maximum zoom*/
        mMap.setMinZoomPreference(17.0f);
        mMap.setMaxZoomPreference(20.0f);
        /** Add ball marker*/
        //mMarker = mMap.addMarker(new MarkerOptions().position(BALL).title("Ball"));
        //mMarker.setTag(0);

        int i = 0;
        for (Coordinate position : listCoordinate)
        {
            i++;
            listMarker.add( mMap.addMarker(new MarkerOptions()
                    .position( new LatLng(position.getX(), position.getY()) )
                    .title("DragonBall "+ (int) (listCoordinateID.get(i-1).getX()) )
                    .snippet("Mysterious DragonBall you better go catch it!")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_dragon_ball))));

        }
        for (Marker marker : listMarker)
        {
            marker.setTag(0);
        }
        mMap.moveCamera( CameraUpdateFactory.newLatLng(new LatLng(latitude_, longitude_)));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude_, longitude_))
                .title("Me")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_current_position))
                .draggable(true));
        /** Set a listener for marker click*/
        mMap.setOnMarkerClickListener(this);
    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        final String  marker_position;
        final String marker_title;

        marker_title = marker.getTitle();
        marker_position = String.valueOf(marker.getPosition().latitude) +", " +
                String.valueOf(marker.getPosition().longitude);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            public void onInfoWindowClick(Marker marker) {
                Intent activityChangeIntent =
                        new Intent(GoogleMapsActivity.this, AugmentedRealityActivity.class);
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
                activityChangeIntent.putExtra("userClickedOnThisBall", marker_position);
                activityChangeIntent.putExtra("userClickedOnThisBallID", marker_title.charAt(11) +"");
                startActivity(activityChangeIntent);

            }
        });

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        readAndParseFile(listCoordinate, 1);
        readAndParseFile(listCoordinateID, 0);
    }
}
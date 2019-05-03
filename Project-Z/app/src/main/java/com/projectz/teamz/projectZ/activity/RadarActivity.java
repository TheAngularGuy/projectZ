package com.projectz.teamz.projectZ.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.projectz.teamz.projectZ.R;
import com.projectz.teamz.projectZ.classUtils.Coordinate;
import com.projectz.teamz.projectZ.classUtils.FileParser;
import com.projectz.teamz.projectZ.classUtils.Utils;
import com.projectz.teamz.projectZ.enums.CardinalPoints;
import com.projectz.teamz.projectZ.sensorsListener.GPSTracker;
import com.projectz.teamz.projectZ.sensorsListener.MagneticField;
import java.util.ArrayList;
import java.util.List;

public class RadarActivity extends AppCompatActivity implements View.OnTouchListener {

    private static TextView tvDegree;
    private static TextView my_coordinates;
    private static ImageView ballRadar;
    private float currentDegree = 0f;
    private TextView ballCatchByUserTextView;
    private ImageView ballCatchByUserImageView;
    private MagneticField mgt;
    private GPSTracker gps;
    private Coordinate coordinate;
    private List<Coordinate> listCoordinate = new ArrayList<>();
    private BottomNavigationView bottomNavigationView;
    private Coordinate userClick = new Coordinate();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String dataIntent = bundle.getString("mainToRadar");
            if (dataIntent != null) {
                if (!dataIntent.isEmpty()) {
                    this.overridePendingTransition(R.anim.slide_in_down_custom, 0);
                }
            }
        }
        tvDegree = (TextView) findViewById(R.id.tv_degree);
        my_coordinates = (TextView) findViewById(R.id.my_coordinates);
        //gridRadar = (ImageView) findViewById(R.id.gridRadar);
        ballRadar = (ImageView) findViewById(R.id.ballRadar);
        ballCatchByUserTextView = (TextView) findViewById(R.id.ballCatchByUser);
        ballCatchByUserImageView = (ImageView) findViewById(R.id.ballCollected);
        readAndParseFile();
        mgt = new MagneticField(this, 1);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigationview);
        bottomNavigationView.setSelectedItemId(R.id.radar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new
         BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.radar:
                        //Toast.makeText(RadarActivity.this, "Radar", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.map:
                        //Toast.makeText(RadarActivity.this, "Map", Toast.LENGTH_SHORT).show();
                        Intent mapIntent = new Intent(RadarActivity.this, GoogleMapsActivity.class);
                        mapIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(mapIntent);
                        break;
                    case R.id.settings:
                        //Toast.makeText(RadarActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                        Intent settingsIntent = new Intent(RadarActivity.this, SettingActivity.class);
                        settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(settingsIntent);
                        break;
                }
                return true;
            }
        });
        RelativeLayout radarLayout = (RelativeLayout) findViewById(R.id.activity_radar);
        radarLayout.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        final int amplitude = MainActivity.MOUVEMENT_AMPLITUDE;
        boolean isSwipe = false;
        switch (motionEvent.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                userClick.setX( motionEvent.getX() );
                userClick.setY( motionEvent.getY() );
                break;
            case MotionEvent.ACTION_UP:
                float nextX = motionEvent.getX();
                float nextY = motionEvent.getY();
                //Calculate the swipe
                if (Math.abs(nextX-userClick.getX()) > Math.abs(nextY-userClick.getY()))
                {
                    //left or right
                    if (nextX - userClick.getX() > amplitude) {
                        //right
                        isSwipe = true;
                        Intent mapIntent = new Intent(RadarActivity.this, GoogleMapsActivity.class);
                        mapIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(mapIntent);
                    } else if (nextX - userClick.getX() < -amplitude){
                        //left
                        isSwipe = true;
                        Intent settingsIntent = new Intent(RadarActivity.this, SettingActivity.class);
                        settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(settingsIntent);
                    }
                } else {
                    if (nextY - userClick.getY() < -amplitude) {
                        //up
                        isSwipe = true;
                        Log.i("touchListener", "up diff="+ (nextY - userClick.getY()) );
                    } else if (nextY - userClick.getY() > amplitude){
                        //down
                        isSwipe = true;
                    }
                }
                break;
        }
        if (!isSwipe)
            actualizeGPS();
        return true;
    }

    private void actualizeGPS() {
        gps = new GPSTracker(this);
        if(gps.canGetLocation())
        {
            //Toast.makeText(this, "Position refreshed", Toast.LENGTH_SHORT).show();
            coordinate = new Coordinate(gps.getLatitude(), gps.getLongitude());
            //my_coordinates.setText(coordinate.getX() + ", " + coordinate.getY());
            if (listCoordinate != null) {
                Coordinate closestBall =
                        TestRadarActivity.measureClosestBall(coordinate, listCoordinate);
                if (closestBall.getX() == 0)
                    return;
                CardinalPoints cardinalPoints =
                        TestRadarActivity.measureCardinalPoint(coordinate.getX(), coordinate.getY(),
                                closestBall.getX(), closestBall.getY());
                my_coordinates.setText("The closest ball is\n"
                        + TestRadarActivity.measureDistance(closestBall.getX(), closestBall.getY(),
                        coordinate.getX(), coordinate.getY())
                        + "m to your "
                        + cardinalPoints.toString());
            }
            gps.stopUsingGPS();
            mgt.setCoordinates(coordinate, listCoordinate);
        }
    }

    private void readAndParseFile() {
        String str = Utils.readFromFile("ballZ.txt", this);
        if (str != null) {
            FileParser fp = new FileParser(str);
            for (int i = 1; i <= (fp.getHt().size()); i++)
                listCoordinate.add(new Coordinate(fp.getValue(Integer.toString(i))));
        }
        else
            listCoordinate = null;
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
        actualizeGPS();
        mgt.registerSensor();
        currentDegree = mgt.getCurrentDegree();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                displayCollected();
            }
        }, 500);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                actualizeGPS();
                handler.postDelayed(this, 10000);
            }
        }, 10000);
    }

    public void displayCollected() {
        String strFileCatch = Utils.readFromFile("ballZCatchByUser.txt", this);
        if (strFileCatch != null) {
            //ballCatchByUserTextView.setText(strFileCatch);

            Resources r = getResources();
            if(strFileCatch.length() >= 7)
            {
                mgt.unregisterSensor();
                tvDegree.setText("\nCongratulation!\nYou have collected all the balls."+
                        "You can uninstall the beta for now :)");
                my_coordinates.setText("");
                ballCatchByUserImageView.setImageDrawable(r.getDrawable(R.drawable.dragon_end));
            }
            else {
                Drawable[] layers = new Drawable[strFileCatch.length()];
                for (int i = 0; i < strFileCatch.length(); i++) {
                    switch (strFileCatch.charAt(i)) {
                        case '1':
                            layers[i] = r.getDrawable(R.drawable.collected1);
                            break;
                        case '2':
                            layers[i] = r.getDrawable(R.drawable.collected2);
                            break;
                        case '3':
                            layers[i] = r.getDrawable(R.drawable.collected3);
                            break;
                        case '4':
                            layers[i] = r.getDrawable(R.drawable.collected4);
                            break;
                        case '5':
                            layers[i] = r.getDrawable(R.drawable.collected5);
                            break;
                        case '6':
                            layers[i] = r.getDrawable(R.drawable.collected6);
                            break;
                        case '7':
                            layers[i] = r.getDrawable(R.drawable.collected7);
                            break;
                    }
                }
                LayerDrawable layerDrawable = new LayerDrawable(layers);
                ballCatchByUserImageView.setImageDrawable(layerDrawable);
                Animation slideIn = AnimationUtils.loadAnimation(RadarActivity.this,
                        R.anim.slide_in_up_custom);
                ballCatchByUserImageView.startAnimation(slideIn);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        mgt.unregisterSensor();
    }

    public static void setTextView(String str)
    {
        tvDegree.setText(str);
    }

    public static void ballRotation(RotateAnimation ra)
    {
        ballRadar.startAnimation(ra);
        //gridRadar.startAnimation(ra);
    }

    public static void setImageBallGone()
    {
        ballRadar.setVisibility(View.GONE);
        my_coordinates.setText("");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}

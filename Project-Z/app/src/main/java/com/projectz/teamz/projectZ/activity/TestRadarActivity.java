package com.projectz.teamz.projectZ.activity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.projectz.teamz.projectZ.R;
import com.projectz.teamz.projectZ.classUtils.Coordinate;
import com.projectz.teamz.projectZ.classUtils.FileParser;
import com.projectz.teamz.projectZ.classUtils.Utils;
import com.projectz.teamz.projectZ.enums.CardinalPoints;
import com.projectz.teamz.projectZ.sensorsListener.GPSTracker;
import com.projectz.teamz.projectZ.sensorsListener.MagneticField;

import java.util.ArrayList;
import java.util.List;

/**
 * Test du radar
 */
public class TestRadarActivity extends AppCompatActivity {
    
    private static TextView textView1;
    private static TextView textView2;
    private TextView textView3, textView4;
    private GPSTracker gps;
    private MagneticField mgt;
    private String str;
    List<Coordinate> ListCoordinate = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test_radar);
        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);

        gps = new GPSTracker(TestRadarActivity.this, 0);
        if(gps.canGetLocation())
        {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            double orientation = gps.getOrientation();
            str = "Your Location is - \nLat: " + latitude +
                    "\nLong: " + longitude +
                    "\nOrientation: " + orientation;
        }
        else
        {
            // Can't get location.
            // GPS or network is not enabled.
            // Ask user to enable GPS/network in settings.
            gps.showSettingsAlert();
        }
        mgt = new MagneticField(this, 0);
        readAndParseFile();
        distanceBetweenBall();
    }

    @Override
    protected void onStart() {
        super.onStart();
        textView1.setText(str);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mgt.registerSensor();
        gps.getLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mgt.unregisterSensor();
        gps.stopUsingGPS();
    }

    private void readAndParseFile() {
        String str = Utils.readFromFile("ballZ.txt", this);
        if (str != null) {
            FileParser fp = new FileParser(str);
            String ballString = "";

            for (int i = 1; i <= (fp.getHt().size()-1); i++)
                ListCoordinate.add(new Coordinate(fp.getValue(Integer.toString(i))));
            for (Coordinate position : ListCoordinate) {
                ballString += "\n Ball's position= Latitude: "
                        + position.getX() + " Longitude: " + position.getY();
            }
            textView3.setText(ballString);
        }
        else
            textView3.setText("No file");
    }

    private void distanceBetweenBall() {
        double latP = gps.getLatitude();
        double lonP = gps.getLongitude();
        int i = 1;
        String disp = "";
        for (Coordinate position : ListCoordinate)
        {
            disp += "Distance player/ball"+i+"= "+
                    measureDistance( latP, lonP, position.getX(), position.getY() )+" \t "+
                    " -Direction is: " +
                    measureCardinalPoint( latP, lonP, position.getX(), position.getY() )+"\n";
            i++;
        }
        Coordinate closest = measureClosestBall(new Coordinate(latP, lonP), ListCoordinate);
        disp += "The closest ball is : lat= "+ closest.getX() +" lon= "+ closest.getY()+
                " distance= "+ measureDistance(latP, lonP, closest.getX(), closest.getY());
        textView4.setText(disp);
    }

    public static int measureDistance(double lat1, double lon1, double lat2, double lon2)
    {  // generally used geo measurement function
        double R = 6378.137; // Radius of earth in KM
        double dLat = lat2 * Math.PI / 180 - lat1 * Math.PI / 180;
        double dLon = lon2 * Math.PI / 180 - lon1 * Math.PI / 180;
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;
        return (int) (d * 1000); // meters
    }

    public static CardinalPoints measureCardinalPoint(double lt1, double ln1, double lt2, double ln2)
    {
        double latDif = lt1 - lt2;
        double longDif = ln1 - ln2;

        if (Math.abs(latDif) > Math.abs(longDif))
        {            //North ou south
            if (latDif > 0)
                return CardinalPoints.south;
            else
                return CardinalPoints.north;
        }
        else
        {            //East or west
            if (longDif > 0)
                return CardinalPoints.west;
            else
                return CardinalPoints.east;

        }
    }

    public static CardinalPoints degreeToCardinal(double degree)
    {
        if (degree > 0 && degree < 45)
            return CardinalPoints.north;
        if (degree > 45 && degree < 90+45)
            return CardinalPoints.east;
        if (degree > 90+45 && degree < 225)
            return CardinalPoints.south;
        if (degree > 225 && degree < 225+90)
            return CardinalPoints.west;
        if (degree > 315 && degree < 360)
            return CardinalPoints.north;
        return CardinalPoints.north;
    }

    public static Coordinate measureClosestBall(Coordinate position, List<Coordinate> coordinates)
    {
        if (position == null)
            return null;
        if (coordinates == null)
            return null;
        if (coordinates.size() == 0)
            return new Coordinate("0,0");

        double lat = position.getX();
        double lon = position.getY();
        double distance = measureDistance(lat, lon,
                coordinates.get(0).getX(), coordinates.get(0).getY());
        int i = 0;
        int indexForMinDistanceBall = 0;
        for (Coordinate coordinate : coordinates)
        {
            if (distance > measureDistance(lat, lon, coordinate.getX(), coordinate.getY()))
            {
                distance = measureDistance(lat, lon, coordinate.getX(), coordinate.getY());
                indexForMinDistanceBall = i;
            }
            i++;
        }
        return coordinates.get(indexForMinDistanceBall);
    }

    public static void setText1(String str)
    {
        textView1.setText(str);
    }
    public static void setText2(String str)
    {
        textView2.setText(str);
    }

}

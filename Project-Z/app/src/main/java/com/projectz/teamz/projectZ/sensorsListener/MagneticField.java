package com.projectz.teamz.projectZ.sensorsListener;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.projectz.teamz.projectZ.activity.MainActivity;
import com.projectz.teamz.projectZ.activity.RadarActivity;
import com.projectz.teamz.projectZ.activity.TestRadarActivity;
import com.projectz.teamz.projectZ.classUtils.Coordinate;
import com.projectz.teamz.projectZ.enums.CardinalPoints;

import java.util.List;

import static android.content.Context.SENSOR_SERVICE;
import static android.content.Context.TELECOM_SERVICE;

/**
 * champ magnetique
 * Created by musta on 09/05/2017.
 */

public class MagneticField implements SensorEventListener {

    private float currentDegree = 0f;
    private Context context;
    private SensorManager mSensorManager;
    private int option = -1;
    //--------------------------------------
    private List<Coordinate> coordinateList;
    private Coordinate currentCoordinate;

    public MagneticField(Context context) {
        this.context = context;

        mSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
    }

    public MagneticField(Context context, int opt) {
        this.context = context;

        option = opt;
        mSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
    }

    public void setCoordinates(Coordinate currentCoordinate, List<Coordinate> coordinateList) {
        this.coordinateList = coordinateList;
        this.currentCoordinate = currentCoordinate;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Coordinate minCoordinate;
        CardinalPoints cardinalPoint;
        float addDegree = 0;
        float degree = Math.round(event.values[0]);
        currentDegree = degree;

        if (option == 0)
            TestRadarActivity.setText2("Heading: " + Float.toString(degree) + " degrees");
        else if (option == 1) {
            minCoordinate = TestRadarActivity.measureClosestBall(currentCoordinate, coordinateList);
            if (minCoordinate != null) {
                if (minCoordinate.getX() == 0 && minCoordinate.getY() == 0)
                {
                    //RadarActivity.setTextView("You have collected all the balls."+
                    // "You can uninstall the beta for now :)");
                    RadarActivity.setImageBallGone();
                }
                else {
                    cardinalPoint = TestRadarActivity.measureCardinalPoint(
                            currentCoordinate.getX(), currentCoordinate.getY(),
                            minCoordinate.getX(), minCoordinate.getY()
                    );
                    switch (cardinalPoint) {
                        case north:
                            addDegree = 0;
                            break;
                        case east:
                            addDegree = 90;
                            break;
                        case south:
                            addDegree = 180;
                            break;
                        case west:
                            addDegree = 270;
                            break;
                    }
                    RadarActivity.setTextView(Float.toString(degree) + " °\n"+
                            "You are looking "+TestRadarActivity.degreeToCardinal(degree));
                    RotateAnimation ra = new RotateAnimation(-currentDegree +
                            addDegree, -degree + addDegree,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    ra.setDuration(210);
                    ra.setFillAfter(true);
                    RadarActivity.ballRotation(ra);
                }
            }
            else
                RadarActivity.setTextView("Connection to internet  needed!");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {    }

    public void registerSensor()
    {
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    public float getCurrentDegree() {
        return currentDegree;
    }

    public void unregisterSensor()
    {
        mSensorManager.unregisterListener(this);
    }
}

package com.projectz.teamz.projectZ.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.projectz.teamz.projectZ.R;
import com.projectz.teamz.projectZ.apiDatabase.InteractDatabase;
import com.projectz.teamz.projectZ.classUtils.Coordinate;
import com.projectz.teamz.projectZ.classUtils.Utils;
import com.projectz.teamz.projectZ.sensorsListener.GPSTracker;

import eu.kudan.kudan.ARActivity;
import eu.kudan.kudan.ARArbiTrack;
import eu.kudan.kudan.ARGyroPlaceManager;
import eu.kudan.kudan.ARImageNode;
import eu.kudan.kudan.ARLightMaterial;
import eu.kudan.kudan.ARMeshNode;
import eu.kudan.kudan.ARModelImporter;
import eu.kudan.kudan.ARModelNode;
import eu.kudan.kudan.ARTexture2D;

/**
 * AR
 * Created by ForceKevin on 10/05/2017.
 */

public class AugmentedRealityActivity extends ARActivity{

    private ARModelNode modelNode;
    private ARBITRACK_STATE arbitrack_state;
    private GPSTracker gps;
    private InteractDatabase interactDatabase;
    private String ballID;

    enum ARBITRACK_STATE {
        ARBI_PLACEMENT,
        ARBI_TRACKING
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arcamera_view);
        gps = new GPSTracker(AugmentedRealityActivity.this);
        if(gps.canGetLocation())
        {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            double orientation = gps.getOrientation();
        }
        else
        {
            gps.showSettingsAlert();
        }
    }

    public void setup() {

        addModelNode();
        setupArbiTrack();
    }

    private void addModelNode() {

        // Import model
        ARModelImporter modelImporter = new ARModelImporter();
        modelImporter.loadFromAsset("balltest.jet");
        modelNode = (ARModelNode) modelImporter.getNode();

        // Load model texture
        ARTexture2D texture2D = new ARTexture2D();
        texture2D.loadFromAsset("dragonb.png");


        // Apply model texture to model texture material
        ARLightMaterial material = new ARLightMaterial();
        material.setTexture(texture2D);
        material.setAmbient(0.8f, 0.8f, 0.8f);


        // Apply texture material to models mesh nodes
        for (ARMeshNode meshNode : modelImporter.getMeshNodes()) {
            meshNode.setMaterial(material);
        }


        modelNode.scaleByUniform(100f);

    }

    public void setupArbiTrack() {

        // Create an image node to be used as a target node
        ARImageNode targetImageNode = new ARImageNode("target.png");

        // Scale and rotate the image to the correct transformation.
        targetImageNode.scaleByUniform(0.25f);
        targetImageNode.rotateByDegrees(90, 1, 0, 0);

        // Initialise gyro placement. Gyro placement positions content on a virtual floor plane where the device is aiming.
        ARGyroPlaceManager gyroPlaceManager = ARGyroPlaceManager.getInstance();
        gyroPlaceManager.initialise();

        // Add target node to gyro place manager
        gyroPlaceManager.getWorld().addChild(targetImageNode);

        // Initialise the arbiTracker
        ARArbiTrack arbiTrack = ARArbiTrack.getInstance();
        arbiTrack.initialise();

        // Set the arbiTracker target node to the node moved by the user.
        arbiTrack.setTargetNode(targetImageNode);
        arbiTrack.getTargetNode().setVisible(false);

        // Add model node to world
        arbiTrack.getWorld().addChild(modelNode);
    }

    public void takeBall(View view)
    {
        interactDatabase.deleteBallZ(ballID);
        interactDatabase.retrieveBallZ();
        Intent activityChangeIntent =
                new Intent(AugmentedRealityActivity.this, RadarActivity.class);
        AugmentedRealityActivity.this.startActivity(activityChangeIntent);
        onDestroy();
    }

    public void lockPosition(View view) {

        Button b = (Button) findViewById(R.id.lockButton);
        ARArbiTrack arbiTrack = ARArbiTrack.getInstance();

        String x = "15656.6525";
        String y = "84651531.54156";

        String Currentx = "15656.6525";
        String Currenty = "84651531.54156";


        if (x.equals(Currentx)) {
            // If in placement mode start arbi track, hide target node and alter label
            if (arbitrack_state.equals(ARBITRACK_STATE.ARBI_TRACKING)) {

                arbiTrack.stop();

                // Display target node
                arbiTrack.getTargetNode().setVisible(false);

                //Change enum and label to reflect Arbi Track state
                arbitrack_state = ARBITRACK_STATE.ARBI_PLACEMENT;
                b.setText("Start Tracking");
                b.setVisibility(View.VISIBLE);

            } else {
                //Start Arbi Track
                arbiTrack.start();

                //Hide target node
                arbiTrack.getTargetNode().setVisible(false);

                //Change enum and label to reflect Arbi Track state
                arbitrack_state = ARBITRACK_STATE.ARBI_TRACKING;
                b.setVisibility(View.VISIBLE);
                b.setText("Start Tracking");
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        arbitrack_state = ARBITRACK_STATE.ARBI_PLACEMENT;
        lockPosition(getCurrentFocus());

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        gps = new GPSTracker(AugmentedRealityActivity.this);
        interactDatabase = new InteractDatabase(this);
        setContentView(R.layout.activity_arcamera_view);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String data = bundle.getString("userClickedOnThisBall");
            final String dataID = getIntent().getExtras().getString("userClickedOnThisBallID");
            ballID = dataID;
            if (data != null && dataID != null) {
                if (!data.isEmpty()) {
                    Coordinate coordinateBall = new Coordinate(data);
                    Toast.makeText(getApplicationContext(), "ball N" + dataID + " = lat " + coordinateBall.getX() +
                            " long " + coordinateBall.getY(), Toast.LENGTH_LONG).show();
                }
            }
        }
        /*
        final Button b = (Button) findViewById(R.id.lockButton);
        b.setText("Take DragonBall");
        b.setVisibility(View.VISIBLE);

        arbitrack_state = ARBITRACK_STATE.ARBI_PLACEMENT;
        arbiTrack = ARArbiTrack.getInstance();
        if (arbitrack_state.equals(ARBITRACK_STATE.ARBI_PLACEMENT)) {
            arbiTrack.stop();
            arbiTrack.getTargetNode().setVisible(false);
            arbitrack_state = ARBITRACK_STATE.ARBI_TRACKING;
        }

        if(gps.canGetLocation())
        {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            double orientation = gps.getOrientation();
            Toast.makeText(getApplicationContext(), "lat " + latitude + " long " + longitude +
                    " or " + orientation, Toast.LENGTH_LONG).show();
        }
        else
        {
            gps.showSettingsAlert();
        }

        b.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v)
            {
                if (arbitrack_state.equals(ARBITRACK_STATE.ARBI_TRACKING)) {
                    arbiTrack.start();
                    arbiTrack.getTargetNode().setVisible(false);
                    arbitrack_state = ARBITRACK_STATE.ARBI_PLACEMENT;
                    b.setVisibility(View.VISIBLE);
                    //interactDatabase.deleteBallZ(dataID);
                    Intent activityChangeIntent =
                            new Intent(AugmentedRealityActivity.this, RadarActivity.class);
                    AugmentedRealityActivity.this.startActivity(activityChangeIntent);
                }
            }});
        }
        */
    }
}

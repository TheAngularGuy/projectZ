package com.projectz.teamz.projectZ.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.projectz.teamz.projectZ.R;
import com.projectz.teamz.projectZ.apiDatabase.InteractDatabase;
import com.projectz.teamz.projectZ.classUtils.FileParser;
import com.projectz.teamz.projectZ.classUtils.Utils;
import com.projectz.teamz.projectZ.services.ServiceBackgroundSound;

import eu.kudan.kudan.ARAPIKey;

/**
 * Main activity
 * Created by musta on 05/05/2017.
 */

public class MainActivity extends AppCompatActivity {

    private Button btn1, btn2, btn3, btn4, btn5, btn6;
    private ImageView img1;
    protected Context context = this;
    public final static int MOUVEMENT_AMPLITUDE = 200;
    public final static boolean MODE_TEST = false;
    public final static String URL_API = "192.168.1.103";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        img1 = (ImageView) findViewById(R.id.imageView1);
        permissionsRequest();
        InteractDatabase interactDatabase = new InteractDatabase(this);
        interactDatabase.retrieveBallZ();
        //interactDatabase.deleteBallZ("3");
        checkMusicToStart();
        ARAPIKey key = ARAPIKey.getInstance();
        key.setAPIKey("s1VBn+6PQqkEAXee0L/pDbQkxANGxYx+JAnlo4O/NkzxDlAjTvdnyw"+
                "54KeFI4uYusBscv/jvc93+4HKrvQURqeYJt1Kl694a9y/rwHxEgvLnJ3MN/y"+
                "H/E9/2r6/gQ9hbmczI9ErESbOPWYppBako6o06jki2cdnL4g4Ntm68Sab2Gp"+
                "dEhV1LFxmDzv5nK+h1rarzDJqTbs4RP0z7u6aEdg7unbIo8QAHmemFH7TtRY"+
                "3aEHJ+D0KdSdoiMt163VLv4mpthl7Bk2q5WKnXZ2eEN7DlKtZdWob8G7jlYK"+
                "W6VQYydk+dNzqzHzPbp46PiSxG8XSlBJHwH+hsxBrQSHfXx7KPw7/DfWfoal"+
                "8mkf9K3pvjbu3FZLrsOghyVVK3MI/tx3WprMqwUtsaj18yVjaTpOqeg4unXz"+
                "FsnmXV0grRdPFNsWuNovj/OVELWFPF6Ueqdj4sicYxiHWFj9flfK90XRk7wb"+
                "kzIoOwiBxhEh1IgNOExYQj/awXb8iCpHWTEi4gd8ExvQXSefSLJwjUo2XLZ0"+
                "0UnLSELyRSKyxQZdIpBPusQYWi65HxTK5bndvxVo0YEZT33xi7HcrSgjiBsC"+
                "ioS/a61UzQKG8P/apUuj5G3jF5VzIvnOmSbKwXWId9AJaAUf0j/+Dl8pjG0+"+
                "TWpzCNSTa9/UfZQ49K6Oc4KnkBxsM=");

        InitButtonListener();
       // final ActionBar actionBar = getSupportActionBar();
        //assert actionBar != null;
        //actionBar.hide();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation slideOut = AnimationUtils.loadAnimation(context,
                        R.anim.slide_out_down_custom);
                slideOut.setAnimationListener(new Animation.AnimationListener(){
                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        if (!MODE_TEST)
                        {
                            setButtonsVisibilityGone();
                            Intent activityChangeIntent = new
                                    Intent(MainActivity.this, RadarActivity.class);
                            activityChangeIntent.putExtra("mainToRadar","doAnimation");
                            MainActivity.this.startActivity(activityChangeIntent);
                        }
                        img1.setVisibility(View.GONE);
                    }
                    @Override
                    public void onAnimationRepeat(Animation arg0) { }
                    @Override
                    public void onAnimationStart(Animation arg0) {
                        if (MODE_TEST) {//actionBar.show();
                        }
                    }
                });
                img1.startAnimation(slideOut);
            }
        }, 3300);
    }

    @Override
    protected void onStart() {
        super.onStart();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    protected void onStop() {
        super.onStop();

        //Intent svc = new Intent(MainActivity.this, ServiceBackgroundSound.class);
        //stopService(svc);
    }

    private void InitButtonListener() {
        btn1 = (Button) findViewById(R.id.button);
        btn2 = (Button) findViewById(R.id.button2);
        btn3 = (Button) findViewById(R.id.button3);
        btn4 = (Button) findViewById(R.id.button4);
        btn5 = (Button) findViewById(R.id.button5);
        btn6 = (Button) findViewById(R.id.button6);

        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent activityChangeIntent =
                        new Intent(MainActivity.this, RadarActivity.class);
                MainActivity.this.startActivity(activityChangeIntent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent activityChangeIntent =
                        new Intent(MainActivity.this, TestRadarActivity.class);
                MainActivity.this.startActivity(activityChangeIntent);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent activityChangeIntent =
                        new Intent(MainActivity.this, GoogleMapsActivity.class);
                MainActivity.this.startActivity(activityChangeIntent);
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent activityChangeIntent =
                        new Intent(MainActivity.this, AugmentedRealityActivity.class);
                activityChangeIntent.putExtra("userClickedOnThisBall","39.949801,116.343820");
                MainActivity.this.startActivity(activityChangeIntent);
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent activityChangeIntent =
                        new Intent(MainActivity.this, SettingActivity.class);
                MainActivity.this.startActivity(activityChangeIntent);
            }
        });
    }

    private void setButtonsVisibilityGone()
    {
        btn1.setVisibility(View.GONE);
        btn2.setVisibility(View.GONE);
        btn3.setVisibility(View.GONE);
        btn4.setVisibility(View.GONE);
        btn5.setVisibility(View.GONE);
        btn6.setVisibility(View.GONE);
    }

    private void checkMusicToStart() {
        String str = Utils.readFromFile("musicZ.txt", this);
        if (str != null)
        {
            FileParser fp = new FileParser(str);
            int bool = Integer.parseInt(fp.getValue("music"));
            Log.d("checkMusic", "bool="+bool);

            if (bool == 1)
            {
                Intent svc = new Intent(MainActivity.this, ServiceBackgroundSound.class);
                startService(svc);
            }
        }
    }
    /**
     * Requests app permissions
     */
    public void permissionsRequest() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
            != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.CAMERA}, 111);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case 111: {
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)
                { /* do nothing */ }
                else {
                    permissionsNotSelected();
                }
            }
        }
    }

    private void permissionsNotSelected() {
        AlertDialog.Builder builder = new AlertDialog.Builder (this);
        builder.setTitle("Permissions Requred");
        builder.setMessage("Please enable the requested permissions" +
                "in the app settings in order to use this game");
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                System.exit(1);
            }
        });
        AlertDialog noInternet = builder.create();
        noInternet.show();
    }

}

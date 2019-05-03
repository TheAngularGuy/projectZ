package com.projectz.teamz.projectZ.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.projectz.teamz.projectZ.R;
import com.projectz.teamz.projectZ.apiDatabase.InteractDatabase;
import com.projectz.teamz.projectZ.classUtils.Coordinate;
import com.projectz.teamz.projectZ.classUtils.FileParser;
import com.projectz.teamz.projectZ.classUtils.Utils;
import com.projectz.teamz.projectZ.services.ServiceBackgroundSound;

public class SettingActivity extends AppCompatActivity implements View.OnTouchListener {

    private BottomNavigationView bottomNavigationView;
    private ToggleButton Music;
    private Coordinate userClick = new Coordinate();
    private InteractDatabase interactDatabase;
    private EditText editTextApi;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        final int amplitude = MainActivity.MOUVEMENT_AMPLITUDE;
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
                        Intent radarIntent = new Intent(SettingActivity.this, RadarActivity.class);
                        radarIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(radarIntent);
                    } else if (nextX - userClick.getX() < -amplitude){
                        //left
                    }
                } else {
                    if (nextY - userClick.getY() < -amplitude) {
                        //up
                        Log.i("touchListener", "up diff="+ (nextY - userClick.getY()) );
                    } else if (nextY - userClick.getY() > amplitude){
                        //down
                    }
                }
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        String str = Utils.readFromFile("URL_API_Z.txt", this);
        editTextApi.setText(MainActivity.URL_API);
        if (str != null)
        {
            if (!str.isEmpty())
                editTextApi.setText(str);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigationview2);
        bottomNavigationView.setSelectedItemId(R.id.settings);
        Music = (ToggleButton) findViewById(R.id.tbMusic);
        editTextApi = (EditText) findViewById(R.id.editText);
        interactDatabase = new InteractDatabase(this);

        String str = Utils.readFromFile("musicZ.txt", this);
        if (str != null) {
            FileParser fp = new FileParser(str);
            int bool = Integer.parseInt(fp.getValue("music"));
            if (bool > 0)
                Music.setChecked(true);
            else
                Music.setChecked(false);
        }
        else
        {
            Music.setChecked(false);
        }
        Music.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    // The toggle is enabled so True
                    Intent svc = new Intent(SettingActivity.this, ServiceBackgroundSound.class);
                    startService(svc);
                    //write to file on
                    Utils.writeToFile("music=1", "musicZ.txt", SettingActivity.this);
                } else
                {
                    // The toggle is disabled so False
                    Intent svc = new Intent(SettingActivity.this, ServiceBackgroundSound.class);
                    stopService(svc);
                    //write to file off
                    Utils.writeToFile("music=0", "musicZ.txt", SettingActivity.this);

                }
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new
         BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.radar:
                        //Toast.makeText(SettingActivity.this, "Radar", Toast.LENGTH_SHORT).show();
                        Intent radarIntent = new Intent(SettingActivity.this, RadarActivity.class);
                        radarIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(radarIntent);
                        break;
                    case R.id.map:
                        //Toast.makeText(SettingActivity.this, "Map", Toast.LENGTH_SHORT).show();
                        Intent mapIntent = new Intent(SettingActivity.this, GoogleMapsActivity.class);
                        mapIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(mapIntent);
                        break;
                    case R.id.settings:
                        //Toast.makeText(SettingActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        RelativeLayout radarLayout = (RelativeLayout) findViewById(R.id.activity_setting);
        radarLayout.setOnTouchListener(this);
    }

    public void resetAll(View v)
    {
        Utils.writeToFile("", "ballZCatchByUser.txt", SettingActivity.this);
        Utils.writeToFile("", "ballZ.txt", SettingActivity.this);
        interactDatabase.resetBallZ();
        interactDatabase.retrieveBallZ();
    }

    public void syncAPI(View v)
    {
        String str = null;
        if (editTextApi.getText() != null)
            str = editTextApi.getText().toString();
        if (str != null)
            Utils.writeToFile(str, "URL_API_Z.txt", SettingActivity.this);
        interactDatabase.testBallZ();
    }
}

package com.projectz.teamz.projectZ.apiDatabase;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.projectz.teamz.projectZ.activity.MainActivity;
import com.projectz.teamz.projectZ.classUtils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class InteractDatabase {
    private  String URL_API = MainActivity.URL_API;
    protected Context context;

    public InteractDatabase(Context context) {
        this.context = context;

        checkURLFile();
    }

    private void checkURLFile() {
        String str = Utils.readFromFile("URL_API_Z.txt", context);
        if (str != null)
        {
            if (!str.isEmpty())
                URL_API = str;
        }
    }

    private class RetrieveFeedTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... urls) {
            // Do some validation here
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new
                            BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return (stringBuilder.toString());
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                //Log.e("ERROR", e.getMessage(), e);
                Log.e("ERROR", "API TIME OUT");
                return null;
            }
        }
        @Override
        protected void onPostExecute(String response) {
            if (response != null)
                Log.i("INFO", response);
            else
                Log.i("INFO", "Not connected");
            try {
                String coordBallz = "";
                String idBallz = "";
                if (response != null)
                {
                    String cut = "[;]";
                    String[] jsonStrings = response.split(cut);
                    Log.i("INFO", "json lines: "+jsonStrings.length +"");
                    JSONObject jsonTypeRequest = new JSONObject(jsonStrings[0]);
                    if (jsonTypeRequest.getString("requestType").equals("GET"))
                    {
                        for (int i = 1; i < (jsonStrings.length - 1); i++) {
                            JSONObject json = new JSONObject(jsonStrings[i]);
                            coordBallz += "" + i + "="
                                    + json.getString("1") + "," + json.getString("2") + "&";
                        }
                        Log.i("INFO ballZ co fetched ", coordBallz);
                        for (int i = 1; i < (jsonStrings.length - 1); i++) {
                            JSONObject json = new JSONObject(jsonStrings[i]);
                            idBallz += "" + i + "="+ json.getString("0")+ ",0&";
                        }
                        Log.i("INFO ballZ id fetched ", idBallz);
                        Utils.writeToFile(idBallz, "ballZid.txt", context);
                        Utils.writeToFile(coordBallz, "ballZ.txt", context);
                    }
                    else if(jsonTypeRequest.getString("requestType").equals("DEL"))
                    {
                        Log.i("INFO ballZ removed ", jsonTypeRequest.getString("id"));
                        String previousFile = Utils.readFromFile("ballZCatchByUser.txt", context);
                        if (previousFile == null)
                            previousFile = "";
                        Utils.writeToFile(previousFile + jsonTypeRequest.getString("id"),
                                "ballZCatchByUser.txt", context);
                    }
                    else if (jsonTypeRequest.getString("requestType").equals("TEST"))
                    {
                        Toast.makeText(context,"Connection : OK", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e)
            {
                // Appropriate error handling code
                Log.e("MainActivity read", "ERROR JSON" );
            }
        }
    }



    public void retrieveBallZ() {
        checkURLFile();
        Log.i("retrieveBallz", "API get balls");
        RetrieveFeedTask async = new RetrieveFeedTask();
        String url = "http://"+ URL_API +"/projectz/get.php?key=*ProjectZ*";
        async.execute(url);
    }

    public void deleteBallZ(String id) {
        checkURLFile();
        Log.i("deleteBallZ", "API delete ball");
        RetrieveFeedTask async = new RetrieveFeedTask();
        String url = "http://"+ URL_API +"/projectz/delete.php?id=" + id + "&key=*ProjectZ*";
        async.execute(url);
    }

    public void resetBallZ() {
        checkURLFile();
        Log.i("retrieveBallz", "API reset balls");
        RetrieveFeedTask async = new RetrieveFeedTask();
        String url = "http://"+ URL_API +"/projectz/reset.php?key=*ProjectZ*";
        async.execute(url);
    }

    public void testBallZ() {
        checkURLFile();
        Log.i("testBallz", "API test connection");
        RetrieveFeedTask async = new RetrieveFeedTask();
        String url = "http://"+ URL_API +"/projectz/index.php?key=*ProjectZ*";
        async.execute(url);
    }
}

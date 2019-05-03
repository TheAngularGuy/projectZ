package com.projectz.teamz.projectZ.classUtils;


import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

/**
 * Fonctions utiles
 * Created by musta on 19/04/2017.
 */
public class Utils {
    /**
     * Write data in config.txt that is in the directory of the app
     * @param data data to put in the file
     * @param context the context of the activity
     */
    public static void writeToFile(String data,Context context) {
        writeToFile(data, "config.txt", context);
    }

    /**
     * Open and read the data from config.txt that is in the directory of the app
     * @param context the context of the activity
     * @return
     */
    public static String readFromFile(Context context) {
        return readFromFile("config.txt", context);
    }

    /**
     * Write data in a file that is in the directory of the app
     * @param data data to put in the file
     * @param fileName name of the file
     * @param context the context of the activity
     */
    public static void writeToFile(String data, String fileName,Context context) {
        try {
            OutputStreamWriter outputStreamWriter =
                    new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "|File write failed: " + e.toString());
        }
    }

    /**
     * Open and read the data from a file that is in the directory of the app
     * @param fileName name of the file
     * @param context the context of the application
     * @return
     */
    public static String readFromFile(String fileName, Context context)
    {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(fileName);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "|File not found: " + e.toString());
            return null;
        } catch (IOException e) {
            Log.e("login activity", "|Can not read file: " + e.toString());
            return null;
        }
        return ret;
    }

    /**
     * Make a beep sound
     */
    public static void beepSound()
    {
        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,150);
    }

    /**
     * create a FILE along with all the directories leading to it
     * @param base base directory FILE
     * @param paths name of the directories to create finishing with the file name
     * @return the FILE created by concatenating the paths
     */
    public static File pathToFile(File base, String... paths)
    {
        Log.d("pathToFile", "expending starting from " + base.getPath());
        for (String path : paths) {
            base = new File(base, path);
            Log.d("pathToFile", "expended to " + base.getPath());
        }
        if (base.getParentFile() != null)
            base.getParentFile().mkdirs();
        return base;
    }

    /**
     * delete folders and their content recursively
     * @param target file or folder to remove
     * @throws SecurityException if file cannot be acceded
     */
    public static void recursiveDelete(File target) throws SecurityException
    {
        if (target.exists() == false)
            return;
        if (target.isDirectory() == true) {
            for (File file : target.listFiles())
                recursiveDelete(file);
        }
        target.delete();
    }

    /**
     * Encode a file to a base64 format
     * @param Path path of the file
     * @return return the encoded data
     */
    @Nullable
    public static String File2Base64(File Path) {
        try {
            RandomAccessFile f = new RandomAccessFile(Path, "rw");
            byte[] bytes = new byte[(int)f.length()];
            f.readFully(bytes);
            f.close();
            return Base64.encodeToString(bytes, Base64.NO_WRAP);
        } catch (IOException e) {
            Log.e("File2Base64", e.getLocalizedMessage(), e);
            return null;
        }
    }
}
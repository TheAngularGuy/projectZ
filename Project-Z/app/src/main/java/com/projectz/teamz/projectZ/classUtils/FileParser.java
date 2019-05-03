package com.projectz.teamz.projectZ.classUtils;

import android.util.Log;

import java.util.Hashtable;

/**
 * parser
 * Created by musta on 09/05/2017.
 */

public class FileParser {
    private String contents;
    private Hashtable ht = new Hashtable();

    /**
     * Constructor
     * @param contents data of the file
     */
    public FileParser(String contents)
    {
        this.contents = contents;
        this.putToHt();
    }

    public Hashtable getHt() {
        return ht;
    }

    /**
     * Return the content of the file
     * @return a string containing the data of the file
     */
    public String getContents() { return contents; }

    /**
     * Return the data contained in a variable
     * Example of Qr 'pass=MMM&id=PPP' if T=the key is 'pass' it will return 'MMM'
     * @param Key name of a variable in the file
     * @return return a string containing the data
     */
    public String getValue(String Key) { return (String)ht.get( Key ); }

    /**
     * Parse the file
     * The delimiter is '\n'
     * Example : 'Variable=data&Variable=data'
     */
    private void putToHt()
    {
        Log.v("putToHt", "Content" + this.contents);
        if (contents.isEmpty())
            return;
        String delims = "[&]";
        String[] tokens = this.contents.split(delims);

        for( int i = 0; i < tokens.length; i++)
        {
            String k = tokens[i].substring( 0 , tokens[i].indexOf('=') );
            String v = tokens[i].substring(tokens[i].indexOf('=') + 1 );
            ht.put(k, v);
        }
    }
}
package com.example.lab2;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class UrlXmlReader extends AsyncTask<Object, Void, String> {
    MainActivity activity;
    private final String TAG = "UrlXmlReader";
    String mContent = "";
    @Override
    protected String doInBackground(Object... params) {
        activity = (MainActivity)params[0];
        StringBuilder contentBuilder = new StringBuilder();
        try {
            URL url = new URL((String)params[1]);
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(url.openStream()));

            String line;
            while ((line = in.readLine()) != null) {
                contentBuilder.append(line);
                Log.d(TAG, "doInBackground: " + line);
            }
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mContent = contentBuilder.toString();
        return contentBuilder.toString();
    }

    @Override
    protected void onPostExecute(String string) {
        Log.d(TAG, "onPostExecute: ");
        activity.displayContent(string);
    }
}

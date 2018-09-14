/*package com.etsisi.dev.etsisicrowdsensing;

import android.os.AsyncTask;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class LongRunningGetIO extends AsyncTask<Void, Void, String> {


    @Override
    protected String doInBackground(Void... voids) {

      // Create URL
        URL githubEndpoint = null;
        try {
            githubEndpoint = new URL("https://api.github.com/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Create connection
        try {
            HttpsURLConnection myConnection = (HttpsURLConnection) githubEndpoint.openConnection();
            // App identification
            myConnection.setRequestProperty("User-Agent", "my-rest-app-v0.1");

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    protected void onPostExecute(String results) {
        if (myConnection.getResponseCode() == 200) {
            // Success
            // Further processing here
        } else {
            // Error handling code goes here
        }


        Button b = (Button) findViewById(R.id.button);


        b.setClickable(true);
    }
}
*/
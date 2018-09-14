package com.etsisi.dev.etsisicrowdsensing.menu.options;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.etsisi.dev.etsisicrowdsensing.R;

public class TransportActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TransportActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport);

        // Set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //TextView textView = findViewById(R.id.textView);

        findViewById(R.id.httpGETBtn).setOnClickListener(this);
        Log.d(TAG, "On click listener was set");
    }

    @Override
    public void onClick(View v) {
        final TextView mTextView = (TextView) findViewById(R.id.httpTextView);
        // ...

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://www.google.com";

        Log.d(TAG, "Request is prepared");
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,"Response was returned");
                        // Display the first 500 characters of the response string.
                        mTextView.setText("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"HTTP call didn't work: " + error.toString());
                error.printStackTrace();
                mTextView.setText("That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);



        //b.setClickable(false);
        //new LongRunningGetIO().execute();
    }
}

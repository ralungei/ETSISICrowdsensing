package com.etsisi.dev.etsisicrowdsensing.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.Toolbar;


import com.etsisi.dev.etsisicrowdsensing.R;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

public class NewsActivity extends ListActivity {

    private static Twitter twitter;

    private static final String TAG = "NewsActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        // Set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);
        getActionBar().setTitle("");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);




        //final ListView listView = (ListView) findViewById(R.id.list_view);


        Twitter.initialize(this);


        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName("ETSISIupm")
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(userTimeline)
                .build();

        setListAdapter(adapter);
    }

}

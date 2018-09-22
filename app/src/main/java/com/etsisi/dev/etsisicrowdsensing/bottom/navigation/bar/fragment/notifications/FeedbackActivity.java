package com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.notifications;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.etsisi.dev.etsisicrowdsensing.R;

import java.util.ArrayList;

public class FeedbackActivity extends AppCompatActivity {

    private ArrayList<String> mTopics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        loadTopics();
        feedbackBlocksRecyclerViewConfig();



    }

    private void loadTopics(){
        mTopics = new ArrayList<String>();
        mTopics.add("Tu estado");
        mTopics.add("Temario");
        mTopics.add("Profesor");
        mTopics.add("Ambiente");
    }

    private void feedbackBlocksRecyclerViewConfig(){
        RecyclerView feedbackBlocksRecyclerView = (RecyclerView) findViewById(R.id.feedback_blocks_recyclerView);

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 2);
        feedbackBlocksRecyclerView.setLayoutManager(mGridLayoutManager);

        //  This item decoration is used to draw a line under each item in Recyclerview.
        //notificationsRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
        //        DividerItemDecoration.VERTICAL));

        FeedbackBlocksAdapter feedbackBlocksAdapter = new FeedbackBlocksAdapter(mTopics);
        feedbackBlocksRecyclerView.setAdapter(feedbackBlocksAdapter);
    }
}

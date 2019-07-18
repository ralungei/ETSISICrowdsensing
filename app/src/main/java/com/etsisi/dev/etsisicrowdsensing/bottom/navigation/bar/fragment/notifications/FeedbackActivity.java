package com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.notifications;

import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.etsisi.dev.etsisicrowdsensing.R;
import com.etsisi.dev.etsisicrowdsensing.model.FeedbackForm;
import com.etsisi.dev.etsisicrowdsensing.model.FeedbackResult;
import com.etsisi.dev.etsisicrowdsensing.model.Voting;
import com.etsisi.dev.etsisicrowdsensing.web.api.network.RetrofitInstance;
import com.etsisi.dev.etsisicrowdsensing.web.api.network.RetrofitUPMInstance;

import org.w3c.dom.Text;

import java.sql.Array;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackActivity extends AppCompatActivity implements FeedbackBlocksAdapter.FeedbackStateClickListener, View.OnClickListener {

    private static final String TAG = "FeedbackActivity";
    private List<String> mTopics;

    private ArrayList<Voting> votingArray;

    static final int PICK_REASON_REQUEST = 0;
    static final String EXTRA_CATEGORY = "feedback_category";
    static final String EXTRA_REASON = "reason";

    private Button sendBtn;

    private FeedbackForm feedbackForm;

    private RetrofitInstance restService;

    private RetrofitUPMInstance restUPMService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // WebAPI Rest Interface Service
        restService = new RetrofitInstance();
        restUPMService = new RetrofitUPMInstance();

        // Load categories
        Resources res = getResources();
        String[] categories = res.getStringArray(R.array.feedback_categories_array);
        mTopics = Arrays.asList(categories);

        // Load voting array with categories
        votingArray = new ArrayList<>();
        for(String topic: mTopics){
            Voting voting = new Voting(topic);
            votingArray.add(voting);
        }

        sendBtn = findViewById(R.id.send_button);
        Button cancelBtn = findViewById(R.id.cancel_button);

        sendBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);


        // Form info
        feedbackForm = getIntent().getParcelableExtra(NotificationsFragment.FEEDBACKFORM_EXTRA);

        // TODO Get info views and fill with feedback form info
        TextView subjectView = findViewById(R.id.subjectText);
        TextView timeView = findViewById(R.id.timeTextView);
        TextView placeView = findViewById(R.id.placeText);

        // TODO Read date
        // DATE AS 00/00/0000
        // ADD HOUR
        // ADD HOUR + DURATION
        Date date = feedbackForm.getDate();
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);

        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(date); // sets calendar time/date
        cal.add(Calendar.HOUR_OF_DAY, feedbackForm.getDuration());
        String formattedTime = new SimpleDateFormat("HH:mm").format(cal.getTime());
        formattedDate += "-" + formattedTime;

        timeView.setText(formattedDate);

        subjectView.setText(feedbackForm.getSubject().getTitle());
        placeView.setText(String.valueOf(feedbackForm.getRoom()));


        feedbackBlocksRecyclerViewConfig();
    }



    private void feedbackBlocksRecyclerViewConfig(){
        RecyclerView feedbackBlocksRecyclerView = (RecyclerView) findViewById(R.id.feedback_blocks_recyclerView);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false);

        feedbackBlocksRecyclerView.setLayoutManager(mLinearLayoutManager);

        FeedbackBlocksAdapter feedbackBlocksAdapter = new FeedbackBlocksAdapter(mTopics, this);
        feedbackBlocksRecyclerView.setAdapter(feedbackBlocksAdapter);
    }

    @Override
    public void onFeedbackStateItemClick(String topic, int level) {

        //Toast.makeText(this, "Topic " + topic + " with smile level " + level + " clicked  ",Toast.LENGTH_SHORT).show();



        if(level < 4) {
            Intent i = new Intent(this, FeedbackReasonsActivity.class);
            i.putExtra(EXTRA_CATEGORY, topic);
            startActivityForResult(i, PICK_REASON_REQUEST);
        }

        for(Voting voting: votingArray){
            if(voting.getCategory().equals(topic)){
                voting.setScore(level);
            }
        }

        if(formIsFilled())
            sendBtn.setEnabled(true);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_REASON_REQUEST) {
            if (resultCode == RESULT_OK) {
                String reason = data.getStringExtra(EXTRA_REASON);
                String topic = data.getStringExtra(EXTRA_CATEGORY);
                for(Voting voting: votingArray){
                    if(voting.getCategory().equals(topic)){
                        voting.setReason(reason);
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel_button:
                Intent i = getIntent();
                setResult(RESULT_OK, i);
                finish();
                break;
            case R.id.send_button:
                //  Create Feedback Result
                int subjectId = feedbackForm.getSubject().getId();
                int duration = feedbackForm.getDuration();
                int room = feedbackForm.getRoom();
                Date date = feedbackForm.getDate();


                FeedbackResult result = new FeedbackResult(subjectId, duration, room, date, votingArray);
                sendFeedbackWebAPI(result);


                Log.d(TAG, "Feedback form data is " + feedbackForm.getSubject().getId() + " duration " + feedbackForm.getDuration() + " room " + feedbackForm.getRoom());
                for(Voting voting: votingArray){
                    Log.d(TAG, "Topic " + voting.getCategory() + " with score " + voting.getScore() + " porque " + voting.getReason());
                }

                /*
                Intent intent = getIntent();
                setResult(RESULT_OK, intent);
                finish();
                */

                break;
        }
    }

    public boolean formIsFilled(){
        for(Voting voting: votingArray){
            if(voting.getScore() == -1)
                return false;
        }
        return true;
    }

    public void sendFeedbackWebAPI(FeedbackResult result){
        Call<Void> call = restService.getFeedbacksService().postFeedback(result);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(TAG, "Response received");
                if(response.isSuccessful()){
                    Log.d(TAG, "Is successful");
                    Intent intent = getIntent();
                    setResult(RESULT_OK, intent);
                    finish();
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, t.getMessage().toString());
            }
        });


        Call<Void> callUPM = restUPMService.getFeedbacksService().postFeedback(result);
        callUPM.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(TAG, "Response UPM received " + response.message());
                if(response.isSuccessful()){
                    Log.d(TAG, "Is successful");
                    Intent intent = getIntent();
                    setResult(RESULT_OK, intent);
                    finish();
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, t.getMessage().toString());
            }
        });

    }
}

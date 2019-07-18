package com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.notifications;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.etsisi.dev.etsisicrowdsensing.R;
import com.etsisi.dev.etsisicrowdsensing.model.Event;
import com.etsisi.dev.etsisicrowdsensing.utils.ItemDecorationColumns;

import java.util.Arrays;
import java.util.List;

public class FeedbackReasonsActivity extends AppCompatActivity implements FeedbackReasonsAdapter.ReasonItemClickListener {

    private static final String TAG = "FeedbackReasonsActivity";

    private List<String> reasonsArray;

    private FeedbackReasonsAdapter feedbackReasonsAdapter;

    private String originalCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_reasons);

        // Set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        // Cancel button
        Button cancel = (Button) findViewById(R.id.cancel_button);
        cancel.setOnClickListener(view -> onBackPressed());


        // Get feedback categories
        Resources res = getResources();
        String[] categories = res.getStringArray(R.array.feedback_categories_array);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        originalCategory = intent.getStringExtra(FeedbackActivity.EXTRA_CATEGORY);


        // Set toolbar title
        TextView categoryView = (TextView) findViewById(R.id.categoryView);
        categoryView.setText(originalCategory);

        String category = originalCategory.toLowerCase();
        category = category.replaceAll("\\s", "");
        category = category.replaceAll("á", "a");
        category = category.replaceAll("é", "e");
        category = category.replaceAll("í", "i");
        category = category.replaceAll("ó", "o");
        category = category.replaceAll("ú", "u");


        category += "_array";
        //int reasonsId = getResources().getIdentifier(category, "string", getPackageName());
        int reasonsId = getResources().getIdentifier(category, "array", getPackageName());
        String[] reasons = getResources().getStringArray(reasonsId);
        reasonsArray = Arrays.asList(reasons);

        Log.d(TAG, "Category formatted is " + category);
        Log.d(TAG, "Reasons id is " + reasonsId);


        // TODO Initialize array
        // Capture the layout's elements
        RecyclerView recyclerView = findViewById(R.id.recyclerView);


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new ItemDecorationColumns(2,2));

        // specify an adapter (see also next example)
        feedbackReasonsAdapter = new FeedbackReasonsAdapter(this,this, reasonsArray);
        recyclerView.setAdapter(feedbackReasonsAdapter);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onReasonItemClick(int pos) {
       // Toast.makeText(this, reasonsArray.get(pos),Toast.LENGTH_SHORT).show();
        // TODO Set the result in an intent
        Intent i = getIntent();

        //i.putExtra(FeedbackActivity.EXTRA_CATEGORY, originalCategory);
        i.putExtra(FeedbackActivity.EXTRA_REASON, reasonsArray.get(pos));

        setResult(RESULT_OK, i);
        finish();

    }
}

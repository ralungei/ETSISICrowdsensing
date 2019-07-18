package com.etsisi.dev.etsisicrowdsensing.web.api.network;

import com.etsisi.dev.etsisicrowdsensing.model.FeedbackResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FeedbacksService {

    //i.e. http://localhost/api/institute/Students
    //Add student record and post content in HTTP request BODY
    @POST("feedbacks")
    Call<Void> postFeedback(@Body FeedbackResult feedback);
}

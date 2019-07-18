package com.etsisi.dev.etsisicrowdsensing.web.api.network;

import com.etsisi.dev.etsisicrowdsensing.model.Incidence;
import com.etsisi.dev.etsisicrowdsensing.model.Subject;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SubjectsService {

    //Retrofit turns our institute WEB API into a Java interface.
    //So these are the list available in our WEB API and the methods look straight forward

    //i.e. http://localhost/api/asignaturas
    @GET("asignaturas")
    Call<ArrayList<Subject>> getAsignaturas();


}
package com.etsisi.dev.etsisicrowdsensing.web.api.network;

import com.etsisi.dev.etsisicrowdsensing.model.Incidence;
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

public interface IncidencesService {

    //Retrofit turns our institute WEB API into a Java interface.
    //So these are the list available in our WEB API and the methods look straight forward

    //i.e. http://localhost/api/incidencias
    @GET("incidencias/{userId}")
    Call<ArrayList<Incidence>> getIncidencias(@Path("userId") String userId);

    //i.e. http://localhost/api/incidencias/1
    //Get incidence record base on ID
    @GET("incidencias/{id}")
    Call<Incidence> getIncidenciaById(@Path("id") String id);

    //i.e. http://localhost/api/incidencias/1
    //Delete incidence record base on ID
    @DELETE("incidencias/{id}")
    Call<Void> deleteIncidenciaById(@Path("id") String id);

    //i.e. http://localhost/api/incidencias/1
    //PUT student record and post content in HTTP request BODY
    //@PUT("/incidencias/{id}")
    Call<Void> updateIncidenciaById(@Path("id") String id, @Body Incidence incidence);

    //i.e. http://localhost/api/incidencias
    //Add student record and post content in HTTP request BODY
    @POST("incidencias")
    Call<Void> postIncidencia(@Body Incidence incidence);

}
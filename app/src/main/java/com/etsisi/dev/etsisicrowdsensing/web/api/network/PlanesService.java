package com.etsisi.dev.etsisicrowdsensing.web.api.network;


import com.etsisi.dev.etsisicrowdsensing.model.Plan;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PlanesService {
    //Retrofit turns our institute WEB API into a Java interface.
    //So these are the list available in our WEB API and the methods look straight forward

    //i.e. http://localhost/api/asignaturas
    @GET("planes")
    Call<ArrayList<Plan>> getPlanes();

}

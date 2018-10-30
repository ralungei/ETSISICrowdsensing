package com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus.incidents;

import android.widget.ImageView;

import com.etsisi.dev.etsisicrowdsensing.model.Incidence;

public interface IncidenceItemClickListener {
    void onIncidenceItemClick(int pos, Incidence incidence, ImageView sharedImageView);
}

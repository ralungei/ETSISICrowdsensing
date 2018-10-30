package com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.notifications;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etsisi.dev.etsisicrowdsensing.R;
import com.etsisi.dev.etsisicrowdsensing.model.FeedbackForm;
import com.etsisi.dev.etsisicrowdsensing.model.Notification;
import com.etsisi.dev.etsisicrowdsensing.model.Subject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationsFragment extends Fragment
                                    implements NotificationItemClickListener{
    private OnFragmentInteractionListener mListener;
    private ArrayList<Object> mData;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment
     *
     * @return A new instance of fragment NotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationsFragment newInstance() {
        NotificationsFragment fragment = new NotificationsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadData();
        notificationsRecyclerViewConfig();

        /*
        notificationsRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(),
                        "Click ListItem Number " + position, Toast.LENGTH_LONG)
                        .show();
            }
        });

       */


    }

    private void loadData(){
        mData = new ArrayList<Object>();
        // Notifications sample
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        try {
            Date date = sdf.parse("21/12/2018 12:00");
            Subject sub = new Subject(1,"Fundamentos de Computadores", 3, 2, new ArrayList<>());
            mData.add(new FeedbackForm(sub, date, 2, 3104));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private void notificationsRecyclerViewConfig(){
        RecyclerView notificationsRecyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        notificationsRecyclerView.setLayoutManager(mLinearLayoutManager);

        //  This item decoration is used to draw a line under each item in Recyclerview.
        //notificationsRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
        //        DividerItemDecoration.VERTICAL));

        NotificationsAdapter notificationsAdapter = new NotificationsAdapter(this, mData);
        notificationsRecyclerView.setAdapter(notificationsAdapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onNotificationItemClick(int pos, Notification notification) {
        Intent intent;
        intent = new Intent(getContext(), FeedbackActivity.class);
        startActivity(intent);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

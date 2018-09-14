package com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.etsisi.dev.etsisicrowdsensing.NewIncidence;
import com.etsisi.dev.etsisicrowdsensing.menu.options.MapsActivity;
import com.etsisi.dev.etsisicrowdsensing.menu.options.NewsActivity;
import com.etsisi.dev.etsisicrowdsensing.R;
import com.etsisi.dev.etsisicrowdsensing.menu.options.TransportActivity;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CampusFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CampusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CampusFragment extends Fragment implements View.OnClickListener,
                                                        IncidenceItemClickListener{

    private OnFragmentInteractionListener mListener;

    private CardView wellnessCard, transportCard, foodCard, mapCard, newsCard;

    private RecyclerView incidencesRecyclerView;
    private RecyclerView.LayoutManager incidencesLayoutManager;
    private RecyclerView.Adapter incidencesAdapter;
    private ArrayList<Incidence> incidencesDataset;

    private RecyclerView calendarRecyclerView;
    private RecyclerView.LayoutManager calendarLayoutManager;
    private RecyclerView.Adapter calendarAdapter;
    private ArrayList<Incidence> calendarDataset;


    public CampusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment CampusFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CampusFragment newInstance() {
        CampusFragment fragment = new CampusFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_campus, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        // Set action bar
        Toolbar myToolbar = (Toolbar) getView().findViewById(R.id.my_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(myToolbar);
        ActionBar myActionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        myActionBar.setDisplayShowTitleEnabled(false);
        myActionBar.setDisplayShowHomeEnabled(true);
        loadLogoImage();

        cardListeners();



        // Incidences data sample
        incidencesDataset = new ArrayList<Incidence>();

        Incidence in1 = new Incidence(R.drawable.add_incidence, "Añadir");
        Incidence in2 = new Incidence(R.drawable.bubble_material, "Material");
        Incidence in3 = new Incidence(R.drawable.icon_map, "Ambiente");

        incidencesDataset.add(in1);
        incidencesDataset.add(in2);
        incidencesDataset.add(in3);




        incidencesRecyclerView = (RecyclerView) getView().findViewById(R.id.incidences_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        incidencesRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        incidencesLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        incidencesRecyclerView.setLayoutManager(incidencesLayoutManager);

        // specify an adapter (see also next example)
        incidencesAdapter = new IncidencesAdapter(this, incidencesDataset);
        incidencesRecyclerView.setAdapter(incidencesAdapter);



        try {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.d("Firebase id login", "Refreshed token: " + refreshedToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cardListeners(){
        // Cards listeners
        transportCard = (CardView) getView().findViewById(R.id.transportCard);
        foodCard = (CardView) getView().findViewById(R.id.foodCard);
        mapCard = (CardView) getView().findViewById(R.id.mapCard);
        newsCard = (CardView) getView().findViewById(R.id.newsCard);

        transportCard.setOnClickListener(this);
        foodCard.setOnClickListener(this);
        mapCard.setOnClickListener(this);
        newsCard.setOnClickListener(this);
    }

    // Load Logo ImageView using Glide Library
    public void loadLogoImage(){
        int logoResourceId = R.drawable.campus_logo;
        ImageView imageView = (ImageView) getView().findViewById(R.id.action_bar_logo);
        Glide
                .with(this)
                .load(logoResourceId)
                .into(imageView);
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
    public void onClick(View view) {
        Intent i;

        switch (view.getId()) {
            case R.id.mapCard: i = new Intent(getActivity(), MapsActivity.class); startActivity(i); break;
            case R.id.newsCard: i = new Intent(getActivity(), NewsActivity.class); startActivity(i); break;
            case R.id.transportCard: i = new Intent(getActivity(), TransportActivity.class); startActivity(i); break;

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onIncidenceItemClick(int pos, Incidence incidence, ImageView sharedImageView) {
        Intent intent;

        if(incidence.getText().equals("Añadir")){
            intent = new Intent(getContext(), NewIncidence.class);
        }
        else{
            intent = new Intent(getContext(), IncidenceDetailActivity.class);
            intent.putExtra("title", incidence.getText());

        }


        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                getActivity(),
                sharedImageView,
                "bubbleImage");

        startActivity(intent, options.toBundle());
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

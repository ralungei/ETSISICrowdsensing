package com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsisi.dev.etsisicrowdsensing.CalendarActivity;
import com.etsisi.dev.etsisicrowdsensing.MainActivity;
import com.etsisi.dev.etsisicrowdsensing.utils.SwipeHelper;
import com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus.incidents.IncidentsAdapter;
import com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus.incidents.NewIncidence;
import com.etsisi.dev.etsisicrowdsensing.model.Event;
import com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus.events.EventItemClickListener;
import com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus.events.EventsAdapter;
import com.etsisi.dev.etsisicrowdsensing.model.Incidence;
import com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus.incidents.IncidenceDetailActivity;
import com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus.incidents.IncidenceItemClickListener;
import com.etsisi.dev.etsisicrowdsensing.menu.options.FoodActivity;
import com.etsisi.dev.etsisicrowdsensing.menu.options.MapsActivity;
import com.etsisi.dev.etsisicrowdsensing.menu.options.NewsActivity;
import com.etsisi.dev.etsisicrowdsensing.R;
import com.etsisi.dev.etsisicrowdsensing.menu.options.TransportActivity;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CampusFragment.OnCampusFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CampusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CampusFragment extends Fragment implements View.OnClickListener,
        IncidenceItemClickListener,
        EventItemClickListener {

    private static final String TAG = "CampusFragment";

    static final int NEW_INCIDENCE_REQUEST = 1;
    static final int INCIDENCE_DELETE_REQUEST = 2;
    private String NEW_INCIDENCE_EXTRA = "new_incidence";
    private String DELETE_INCIDENCE_EXTRA = "delete_incidence";


    private OnCampusFragmentInteractionListener mListener;

    private static final String ARG_INCIDENCES_ARRAY = "incidences-array";


    private CardView transportCard, foodCard, mapCard, newsCard;

    private TextView openCalendar;

    private RecyclerView incidentsRecyclerView;
    private RecyclerView.LayoutManager incidencesLayoutManager;
    private RecyclerView.Adapter incidencesAdapter;
    private ArrayList<Incidence> incidencesDataset;

    private RecyclerView eventsRecyclerView;
    private RecyclerView.LayoutManager eventsLayoutManager;
    private EventsAdapter eventsAdapter;
    private ArrayList<Event> eventsDataset;

    private Incidence selectedIncidenceForDetail;

    public CampusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CampusFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CampusFragment newInstance() {
        CampusFragment fragment = new CampusFragment();
        return fragment;
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "Fragment was stopped");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            incidencesDataset = savedInstanceState.getParcelableArrayList(ARG_INCIDENCES_ARRAY);
        } else {
            incidencesDataset = new ArrayList<Incidence>();
            /**
             * Add button bubble incidence
             * Must be added before recovering incidences from DB
             */
            addIncidenceBubble();
            incidencesDataset.addAll(((MainActivity)getActivity()).getIncidencesDataset());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_campus, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
            Log.d(TAG, "Hi");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ARG_INCIDENCES_ARRAY, incidencesDataset);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        actionBarConfig();

        cardListeners();

        incidencesRecyclerViewConfig();

        eventsRecyclerViewConfig();

        editDeleteRows(eventsRecyclerView);

        openCalendar = getActivity().findViewById(R.id.open_calendar_text);
        openCalendar.setOnClickListener(this);

        try {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.d("Firebase id login", "Refreshed token: " + refreshedToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addIncidenceBubble(){
        Incidence addEvent = new Incidence("Añadir", null, 0, null);
        incidencesDataset.add(addEvent);
    }

    public void reloadIncidencesData(){
        incidencesDataset.clear();
        addIncidenceBubble();
        incidencesDataset.addAll(((MainActivity)getActivity()).getIncidencesDataset());
        incidencesAdapter.notifyDataSetChanged();
    }

    public void actionBarConfig() {
        // Set action bar
        Toolbar myToolbar = (Toolbar) getView().findViewById(R.id.my_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);
        ActionBar myActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        myActionBar.setDisplayShowTitleEnabled(false);
        myActionBar.setDisplayShowHomeEnabled(true);
        loadLogoImage();
    }

    public void incidencesRecyclerViewConfig() {
        // Empty recycler view message
        // TextView emptyView = getActivity().findViewById(R.id.empty_view);

        // Incidences data sample

        incidentsRecyclerView = (RecyclerView) getView().findViewById(R.id.incidences_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        incidentsRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        incidencesLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        incidentsRecyclerView.setLayoutManager(incidencesLayoutManager);

        // specify an adapter (see also next example)
        incidencesAdapter = new IncidentsAdapter(getContext(), this, incidencesDataset);
        incidentsRecyclerView.setAdapter(incidencesAdapter);

        if (incidencesDataset.isEmpty()) {
            incidentsRecyclerView.setVisibility(View.GONE);
            //    emptyView.setVisibility(View.VISIBLE);
        } else {
            incidentsRecyclerView.setVisibility(View.VISIBLE);
            //    emptyView.setVisibility(View.GONE);
        }

    }

    public void eventsRecyclerViewConfig() {
        eventsDataset = new ArrayList<Event>();

        Event ev1 = new Event("Estadística", new Date(), "Definición axiomática de probabilidad", 10, "Temas 1,2", "ENTREGA");
        Event ev2 = new Event("Aspectos éticos y sociales", new Date(), "Aula 5201", 10, "Tema 7", "EXAMEN");
        Event ev3 = new Event("Fundamentos de Ingeniería del Software", new Date(), "Bloque IX", 20, "Tema 2", "EXAMEN");

        eventsDataset.add(ev1);
        eventsDataset.add(ev2);
        eventsDataset.add(ev3);

        eventsRecyclerView = (RecyclerView) getView().findViewById(R.id.events_recycler_view);


        //TextView emptyView = getView().findViewById(R.id.empty_view);
        //eventsRecyclerView.setEmptyView(emptyView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        // use a linear layout manager
        eventsLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        eventsRecyclerView.setLayoutManager(eventsLayoutManager);

        // specify an adapter (see also next example)
        eventsAdapter = new EventsAdapter(getContext(), this, eventsDataset);
        eventsRecyclerView.setAdapter(eventsAdapter);
    }


    public void cardListeners() {
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
    public void loadLogoImage() {
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
        if (context instanceof OnCampusFragmentInteractionListener) {
            mListener = (OnCampusFragmentInteractionListener) context;
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
            case R.id.mapCard:
                i = new Intent(getActivity(), MapsActivity.class);
                startActivity(i);
                break;
            case R.id.newsCard:
                i = new Intent(getActivity(), NewsActivity.class);
                startActivity(i);
                break;
            case R.id.transportCard:
                i = new Intent(getActivity(), TransportActivity.class);
                startActivity(i);
                break;
            case R.id.foodCard:
                i = new Intent(getActivity(), FoodActivity.class);
                startActivity(i);
                break;
            case R.id.open_calendar_text:
                i = new Intent(getActivity(), CalendarActivity.class);
                startActivity(i);
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onIncidenceItemClick(int pos, Incidence incidence, ImageView sharedImageView) {
        Intent intent;

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                getActivity(),
                sharedImageView,
                "bubbleImage");


        if (incidence.getCategory().equals("Añadir")) {
            intent = new Intent(getContext(), NewIncidence.class);
            startActivityForResult(intent, NEW_INCIDENCE_REQUEST);
        } else {
            this.selectedIncidenceForDetail = incidence;
            intent = new Intent(getContext(), IncidenceDetailActivity.class);
            intent.putExtra("incidence", incidence);
            startActivityForResult(intent, INCIDENCE_DELETE_REQUEST, options.toBundle());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_INCIDENCE_REQUEST) {
            if (resultCode == getActivity().RESULT_OK) {
                Incidence newIncidence = data.getParcelableExtra((NEW_INCIDENCE_EXTRA));

                if(mListener.addIncidence(newIncidence)){
                    Log.d(TAG, "Incidence was successfully added.");
                    reloadIncidencesData();
                }
                else
                    Log.e(TAG, "Incidence couldn't be added.");
            }
        } else if (requestCode == INCIDENCE_DELETE_REQUEST) {
            if (resultCode == getActivity().RESULT_OK) {
                if(mListener.deleteIncidence(selectedIncidenceForDetail)){
                    reloadIncidencesData();
                    Log.d(TAG, "Incidence was deleted");
                }
                else
                    Log.e(TAG, "Couldn't delete incidence");

            }
        }
    }


    @Override
    public void onEventItemClick(int pos, Event event) {
        Intent intent;
        // TODO Decide if on touch must be included in the Event Recycler View items
        //intent = new Intent(getContext(), EventDetailActivity.class);
        //startActivity(intent);
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
    public interface OnCampusFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);

        /**
         * Incidences data interaction with activity
         */
        boolean addIncidence(Incidence incidence);
        boolean deleteIncidence(Incidence incidence);


    }


    /**
     * Event row deletion
     */

    public void editDeleteRows(RecyclerView recyclerView) {
        SwipeHelper swipeHelper = new SwipeHelper(getContext(), recyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "ELIMINAR",
                        0,
                        Color.parseColor("#FF3C30"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                // TODO: onDelete
                                eventsAdapter.removeItem(pos);
                            }
                        }
                ));

                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "EDITAR",
                        0,
                        Color.parseColor("#C7C7CB"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                // TODO: OnUnshare
                            }
                        }
                ));
            }
        };
    }
}

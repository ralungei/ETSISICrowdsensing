package com.etsisi.dev.etsisicrowdsensing.user.wizard.plan;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.etsisi.dev.etsisicrowdsensing.model.Plan;
import com.etsisi.dev.etsisicrowdsensing.R;
import com.etsisi.dev.etsisicrowdsensing.UserScheduleActivity;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlanWizardFragment.OnPlanSubmittedListener} interface
 * to handle interaction events.
 * Use the {@link PlanWizardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlanWizardFragment extends Fragment {

    private static final String ARG_PLANS = "plans-array";
    private static final String ARG_USER_NAME = "user-name";

    private ArrayList<Plan> planesArray;

    private static String TAG = "PlanWizardFragment";

    private OnPlanSubmittedListener mListener;

    private TextView userNameView;

    private String userName;

    /**
     * Plan selected in the spinner
     */
    private Plan selectedPlan;

    public PlanWizardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment PlanWizardFragment.
     */

    public static PlanWizardFragment newInstance(ArrayList<Plan> planesArray) {
        PlanWizardFragment fragment = new PlanWizardFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PLANS, planesArray);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            planesArray = (ArrayList<Plan>) getArguments().getSerializable(ARG_PLANS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plan_wizard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get name Text View and fill it with the user name
        userNameView = getView().findViewById(R.id.userNameView);
        userNameView.setText(userName);



        final Spinner spinner = (Spinner) getView().findViewById(R.id.spinner);


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, getPlanesTitlesArray(planesArray));

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPlan = planesArray.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        Button nextButton = getView().findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null)
                    mListener.onPlanSubmitted(selectedPlan);
                ((UserScheduleActivity) getActivity()).onNextPressed();
            }
        });

        Button backButton = getView().findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((UserScheduleActivity) getActivity()).onBackPressed();
            }
        });

/*
        mDatabase = FirebaseDatabase.getInstance().getReference().child("planes");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG,dataSnapshot.toString());
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    spinnerArray.add(data.child("descripcion").getValue().toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ERROR FIREBASE", databaseError.getMessage());
            }
        });
        Log.d("ArrayLIST","Datos de la BD " + spinnerArray.isEmpty());
*/
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(ARG_USER_NAME, userName);
        outState.putParcelableArrayList(ARG_PLANS, planesArray);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null) {
            userName = savedInstanceState.getString(ARG_USER_NAME);
            planesArray = (ArrayList<Plan>) savedInstanceState.getSerializable(ARG_PLANS);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPlanSubmittedListener) {
            mListener = (OnPlanSubmittedListener) context;
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

    public void setUserName(String name){
        userNameView.setText("Hola " + upperCaseWords(name) + ",");
    }

    public static String upperCaseWords(String sentence) {
        String words[] = sentence.replaceAll("\\s+", " ").trim().split(" ");
        String newSentence = "";
        for (String word : words) {
            for (int i = 0; i < word.length(); i++)
                newSentence = newSentence + ((i == 0) ? word.substring(i, i + 1).toUpperCase(): (i != word.length() - 1) ? word.substring(i, i + 1).toLowerCase() : word.substring(i, i + 1).toLowerCase().toLowerCase());
            newSentence += " ";
        }


        newSentence = newSentence.substring(0, newSentence.length() - 1);

        return newSentence;
    }

    public ArrayList<String> getPlanesTitlesArray(ArrayList<Plan> mArray) {
        ArrayList<String> titles = new ArrayList<>();
        for (Plan plan : mArray) {
            String planName = plan.getTitle();
            titles.add(planName);
        }
        return titles;
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
    public interface OnPlanSubmittedListener {
        void onPlanSubmitted(Plan plan);
    }
}
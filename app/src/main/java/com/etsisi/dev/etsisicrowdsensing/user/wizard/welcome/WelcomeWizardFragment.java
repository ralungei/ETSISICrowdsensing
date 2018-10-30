package com.etsisi.dev.etsisicrowdsensing.user.wizard.welcome;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.etsisi.dev.etsisicrowdsensing.R;
import com.etsisi.dev.etsisicrowdsensing.UserScheduleActivity;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WelcomeWizardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WelcomeWizardFragment extends Fragment {


    private View progressLayout;
    private View welcomeLayout;

    public WelcomeWizardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment NameWizardFragment.
     */

    public static WelcomeWizardFragment newInstance() {
        WelcomeWizardFragment fragment = new WelcomeWizardFragment();
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
        return inflater.inflate(R.layout.fragment_welcome_wizard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        welcomeLayout = getView().findViewById(R.id.welcomeLayout);

        Button nextButton = getView().findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((UserScheduleActivity) getActivity()).onNextPressed();
            }
        });
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void showProgress(final boolean show){
        progressLayout.setVisibility(show ? View.VISIBLE : View.GONE);
        welcomeLayout.setVisibility(show ? View.GONE : View.VISIBLE);
    }

}



package com.etsisi.dev.etsisicrowdsensing;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.etsisi.dev.etsisicrowdsensing.model.Plan;
import com.etsisi.dev.etsisicrowdsensing.model.Subject;
import com.etsisi.dev.etsisicrowdsensing.model.UserSubject;
import com.etsisi.dev.etsisicrowdsensing.user.wizard.groups.GroupsWizardFragment;
import com.etsisi.dev.etsisicrowdsensing.user.wizard.name.NameWizardFragment;
import com.etsisi.dev.etsisicrowdsensing.user.wizard.plan.PlanWizardFragment;
import com.etsisi.dev.etsisicrowdsensing.user.wizard.subjects.SubjectWizardFragment;
import com.etsisi.dev.etsisicrowdsensing.user.wizard.welcome.WelcomeWizardFragment;
import com.etsisi.dev.etsisicrowdsensing.utils.NonSwipeableViewPager;
import com.etsisi.dev.etsisicrowdsensing.web.api.network.RetrofitInstance;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserScheduleActivity extends FragmentActivity
        implements
        NameWizardFragment.OnNameSubmittedListener,
        PlanWizardFragment.OnPlanSubmittedListener,
        SubjectWizardFragment.OnSubjectsSubmittedListener,
        GroupsWizardFragment.OnGroupsSubmittedListener {

    private static String TAG = "UserScheduleActivity";

    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private NonSwipeableViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private ScreenSlidePagerAdapter mPagerAdapter;

    /**
     * Firebase Database object
     */
    private FirebaseFirestore db;

    /**
     * The number of collections recovered from database
     */
    private static final int NUM_COLLECTIONS = 2;

    private volatile int sync = 0;

    /**
     * JSON File Path from Assets Folder
     */
    private static String PlanesJSONFilePath = "planes.json";

    /**
     * JSON File Path from Assets Folder
     */
    private static String AsignaturasJSONFilePath = "subjects.json";

    private Gson gson;


    //  START VARIABLES INITIALLY LOADED
    /**
     * Array with the plans recovered from the JSON file
     */
    private ArrayList<Plan> planesArrayList = new ArrayList<>();

    /**
     * Array with the plans recovered from the JSON file
     */
    private ArrayList<Subject> subjectsArrayList = new ArrayList<>();

    /**
     * HashMap with the subject array for each course of the selected plan recovered from the JSON file
     */
    private HashMap<Integer, ArrayList<Subject>> planSubjectsPerCourseHashMap = new HashMap<>();

    /**
     * List with  with the courses
     */
    private ArrayList<String> coursesList = new ArrayList<>();
    //  END VARIABLES INITIALLY LOADED


    //  START VARIABLES LOADED FROM FRAGMENTS
    /**
     * The HashMap with the user selected subjects of each course.
     */
    private HashMap<Integer, ArrayList<Subject>> selectedSubjectsPerCourseHashMap;
    //  END VARIABLES LOADED FROM FRAGMENTS

    /**
     * Variable with the user name introduced by the user
     */
    private String userName;

    /**
     * Variable with the plan selected by the user
     */
    private Plan plan;

    /**
     * Variable with the UserSubject objects to be loaded on Database
     * It contains selected Subjects + Group
     */
    private ArrayList<UserSubject> userSelectedSubjectsAndGroupsArrayList;


    /**
     * Array with the plans recovered from the Firebase cloud database
     */
    private ArrayList<String> planesTitlesArray = new ArrayList<>();

    /**
     * Array with the subjects recovered from the Firebase cloud database
     */
    //private ArrayList<Subject> subjectsArray = new ArrayList<Subject>();

    /**
     * Web API Connection
     */
    private RetrofitInstance restService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_schedule);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        // Instantiate database connection
        db = FirebaseFirestore.getInstance();

        // WebAPI Rest Interface Service
        restService = new RetrofitInstance();

        coursesList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.cursos_array)));
        loadPlanesWebAPI();
        //loadSubjectsJSON();
        loadAsignaturasWebAPI();
        //loadPlanSubjectsPerCourseHashMap();


        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (NonSwipeableViewPager) findViewById(R.id.pager);

        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());

        mPager.setAdapter(mPagerAdapter);

    }


    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    public void onNextPressed() {
        mPager.setCurrentItem(mPager.getCurrentItem() + 1);
    }


    public void onFinishPressed() {
        Map<String, Object> data = new HashMap<>();
        data.put("nombre", userName);
        data.put("plan", plan.getTitle());

        db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "User name and plan successfully written into Firestore Database!");
                    }
                });


        for(UserSubject userSubject: userSelectedSubjectsAndGroupsArrayList){
            db.collection("users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("asignaturas")
                    .document(String.valueOf(userSubject.getSubjectId()))
                    .set(userSubject);
        }


        // Save name and plan in shared preferences too
        SharedPreferences settings = getPreferences( 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("name", userName);
        editor.putString("plan", plan.getTitle());
        editor.apply();


        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

/*
    public void dataCollectionFirestore() {

        db.collection("asignaturas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Subject subject = document.toObject(Subject.class);
                                subjectsArray.add(subject);
                            }
                            Log.d(TAG, "Documents in 'asignaturas' where successfully recovered from database");

                            //sync++;
                            //if(sync == NUM_COLLECTIONS)
                            // showProgressBar(false)
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


        // Collect plans array
        db.collection("planes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String planName = document.get("nombre").toString();

                                if (planName.length() > 26)
                                    planName = planName.substring(0, 26);

                                //plansArray.add(planName);
                            }

                            Log.d(TAG, "Documents in 'planes where successfully recovered from database");

                            //sync++;
                            //if(sync == NUM_COLLECTIONS)
                            // showProgressBar(false)

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
*/

/*
    public void loadPlanesJSON() {
        Log.i(TAG, "Started loading Planes JSON File");
        String json = loadJSONFromAsset(PlanesJSONFilePath);
        gson = new Gson();
        Type listType = new TypeToken<List<Plan>>() {
        }.getType();
        planesArrayList = gson.fromJson(json, listType);


        String str = "";
        for(Plan p: planesArrayList){
            str += p.getTitle() + "|";
        }
        Log.d(TAG,str);

        Log.i(TAG, "Successfully loaded Planes JSON File");
    }
    */

    public void loadPlanesWebAPI(){
        Call<ArrayList<Plan>> call = restService.getPlanesService().getPlanes();
        call.enqueue(new Callback<ArrayList<Plan>>() {
            @Override
            public void onResponse(Call<ArrayList<Plan>> call, Response<ArrayList<Plan>> response) {
                if(response.isSuccessful()){
                    planesArrayList = response.body();
                    loadPlanSubjectsPerCourseHashMap();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Plan>> call, Throwable t) {

            }
        });

    }

    /*
    public void loadSubjectsJSON() {
        Log.i(TAG, "Started loading Subjects JSON File");
        String json = loadJSONFromAsset(AsignaturasJSONFilePath);
        gson = new Gson();
        Type listType = new TypeToken<List<Subject>>() {
        }.getType();
        subjectsArrayList = gson.fromJson(json, listType);

        String str = "";
        for(Subject s: subjectsArrayList){
            str += s.getTitle() + "|";
        }
        Log.d(TAG,str);

        Log.i(TAG, "Successfully loaded Subjects JSON File");
    }
    */

    public void loadAsignaturasWebAPI() {
        Call<ArrayList<Subject>> call = restService.getSubjectsService().getAsignaturas();
        call.enqueue(new Callback<ArrayList<Subject>>() {
            @Override
            public void onResponse(Call<ArrayList<Subject>> call, Response<ArrayList<Subject>> response) {
                if(response.isSuccessful()){
                    subjectsArrayList = response.body();
                    loadPlanSubjectsPerCourseHashMap();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Subject>> call, Throwable t) {

            }
        });
    }

    public String loadJSONFromAsset(String fileName) {
        String json = null;
        try {
            InputStream is = this.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    /**
     * This method loads in a HashMap the subjects by course
     */
    public void loadPlanSubjectsPerCourseHashMap() {
        Log.i(TAG, "Started slicing subjects for each course");

        planSubjectsPerCourseHashMap = new HashMap<>();
        for (Subject s : subjectsArrayList) {
            int subjectCourse = s.getCourse() - 1;
            // Create subject arraylist if not created yet
            if( planSubjectsPerCourseHashMap.get(subjectCourse) == null)
                planSubjectsPerCourseHashMap.put(subjectCourse, new ArrayList<>());
            // Add subject to corresponding course position in hashmap
            planSubjectsPerCourseHashMap.get(subjectCourse).add(s);
        }

        printHashMap(planSubjectsPerCourseHashMap);

        Log.i(TAG, "Successfully sliced subjects for each course");


    }


    @Override
    public void onNameSubmitted(String name) {
        this.userName = name;
        PlanWizardFragment f = (PlanWizardFragment) mPagerAdapter.getFragment(mPager.getCurrentItem() + 1);
        f.setUserName(name);
        Log.d(TAG, "Username is -> " + this.userName);

    }


    @Override
    public void onPlanSubmitted(Plan plan) {
        this.plan = plan;
        Log.d(TAG, plan.getTitle());
    }

    @Override
    public void onSubjectsSubmitted(HashMap<Integer, ArrayList<Subject>> selectedSubjects) {
        this.selectedSubjectsPerCourseHashMap = selectedSubjects;
        GroupsWizardFragment f = (GroupsWizardFragment) mPagerAdapter.getFragment(mPager.getCurrentItem() + 1);
        f.setSelectedSubjectsPerCourseHashMap(selectedSubjectsPerCourseHashMap);

        printHashMap(selectedSubjectsPerCourseHashMap);
    }

    @Override
    public void onGroupsSubmitted(ArrayList<UserSubject> userSubjectsArrayList){
        this.userSelectedSubjectsAndGroupsArrayList = userSubjectsArrayList;

        String s = "";
        for(UserSubject userSubject: userSubjectsArrayList){
            s += "SubjectId -> " + userSubject.getSubjectId() + " selected course is " + userSubject.getCourse();
        }
        Log.i(TAG,s);


    }

    public void printHashMap(HashMap<Integer, ArrayList<Subject>> hm){
        for (Map.Entry<Integer, ArrayList<Subject>> entry : hm.entrySet()) {
            String subjects = "";
            for (Subject s : entry.getValue()) {
                subjects += s.getTitle() + ",";
            }

            Log.d(TAG, "Key " + entry.getKey() + " values are -> " + subjects);
        }
    }




    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private final SparseArray<WeakReference<Fragment>> instantiatedFragments = new SparseArray<>();


        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return WelcomeWizardFragment.newInstance();
                case 1:
                    return NameWizardFragment.newInstance();
                case 2:
                    return PlanWizardFragment.newInstance(planesArrayList);
                case 3:
                    return SubjectWizardFragment.newInstance(coursesList, planSubjectsPerCourseHashMap);
                case 4:
                    return GroupsWizardFragment.newInstance(coursesList);
                default:
                    return null;
            }

        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            final Fragment fragment = (Fragment) super.instantiateItem(container, position);
            instantiatedFragments.put(position, new WeakReference<>(fragment));
            return fragment;
        }

        @Override
        public void destroyItem(final ViewGroup container, final int position, final Object object) {
            instantiatedFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        @Nullable
        public Fragment getFragment(final int position) {
            final WeakReference<Fragment> wr = instantiatedFragments.get(position);
            if (wr != null) {
                return wr.get();
            } else {
                return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}

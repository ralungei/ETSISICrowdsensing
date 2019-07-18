package com.etsisi.dev.etsisicrowdsensing;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private FirebaseAuth mAuth;

    private FirebaseFirestore dbFirestore;


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mRepeatedPasswordView;

    // Buttons
    private Button mSignInButton;
    private Button mNextButton;
    private TextView mCreateAccountText;
    private TextView mSignInText;

    private View mProgressView;
    private View mLoginFormGroup;

    private View mYesAccountView;
    private View mNoAccountView;

    private View repeatedPasswordLayout;

    private View parentView;

    // USE IN CASE ACCESS TO USER INFORMATION IS NOT POSSIBLE FROM MAIN ACTIVITY
    //public static final String USERNAME_MESSAGE = "com.etsisi.dev.etsisicrowdsensing.USER_MESSAGE";

    // Log
    private static String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        parentView = findViewById(R.id.parent_container);
        setupUI(parentView);

        loadLogoImage();

        // Get database connection
        dbFirestore = FirebaseFirestore.getInstance();

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);


        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mRepeatedPasswordView = (EditText) findViewById(R.id.repeated_password);
        repeatedPasswordLayout = findViewById(R.id.repeated_password_layout);

        // Login button
        mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setEnabled(validateFormFilled());
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        // Email Next Button
        mNextButton = findViewById(R.id.email_next_button);
        mNextButton.setEnabled(validateFormFilled());

        mLoginFormGroup = findViewById(R.id.login_form_group);
        mProgressView = findViewById(R.id.login_progress);
        mYesAccountView = findViewById(R.id.yes_account_layout);
        mNoAccountView = findViewById(R.id.no_account_layout);

        // Set up login button
        TextWatcher loginTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSignInButton.setEnabled(validateFormFilled());
                mNextButton.setEnabled(validateFormFilled());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        mEmailView.addTextChangedListener(loginTextWatcher);
        mPasswordView.addTextChangedListener(loginTextWatcher);
        mRepeatedPasswordView.addTextChangedListener(loginTextWatcher);

        mCreateAccountText = (TextView) findViewById(R.id.create_account);
        mCreateAccountText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clean fields
                //mEmailView.setText(null);
                mEmailView.setError(null);
                mPasswordView.setText(null);
                mPasswordView.setError(null);
                mRepeatedPasswordView.setText(null);
                // Show repeat password field
                repeatedPasswordLayout.setVisibility(View.VISIBLE);
                // Hide sign in button
                mSignInButton.setVisibility(View.GONE);
                // Show next button
                mNextButton.setVisibility(View.VISIBLE);
                // Hide no account message layout
                mNoAccountView.setVisibility(View.GONE);
                // Show yes account message layout
                mYesAccountView.setVisibility(View.VISIBLE);
                //Remove focus
                View current = getCurrentFocus();
                if (current != null)
                    current.clearFocus();
            }
        });


        mSignInText = findViewById(R.id.sign_in);
        mSignInText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clean fields
                //mEmailView.setText(null);
                mEmailView.setError(null);
                mPasswordView.setText(null);
                mPasswordView.setError(null);
                mRepeatedPasswordView.setText(null);
                // Hide next button
                mNextButton.setVisibility(View.GONE);
                // Hide repeat password field
                repeatedPasswordLayout.setVisibility(View.GONE);
                // Show sign in button
                mSignInButton.setVisibility(View.VISIBLE);

                // Show no account message layout
                mNoAccountView.setVisibility(View.VISIBLE);
                // Hide yes account message layout
                mYesAccountView.setVisibility(View.GONE);
                //Remove focus
                View current = getCurrentFocus();
                if (current != null)
                    current.clearFocus();

            }
        });

        mNextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptCreateAccount();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        showProgress(true);
        // COMMENT/UNCOMMENT FOR USER REMEMBER
        //signOut();
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
   /*     mAuth.addAuthStateListener(firebaseAuth -> {
            if (mAuth.getCurrentUser() == null)
            //User not logged
                mAuth.signOut();
        });*/
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);




    }



    // Load Logo ImageView using Glide Library
    public void loadLogoImage() {
        int logoResourceId = R.drawable.campus_logo;
        ImageView imageView = (ImageView) findViewById(R.id.login_logo);
        Glide
                .with(this)
                .load(logoResourceId)
                .into(imageView);
    }



    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void setupUI(View view) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard(v);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }


    /**
     * Attempts to sign in the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        /*
        if (mAuth != null) {
            return;
        }
        */

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        /**
         * Check password validity here if not usin Firebase User Authentication
         */
        // Check for a valid password, if the user entered one.
        /*
        if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        */

        // Check for a valid email address.
        if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            signIn(email, password);

        }
    }


    /**
     * Attempts to register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptCreateAccount() {
        /*
        if (mAuth != null) {
            return;
        }
        */

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String repeatedPassword = mRepeatedPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check passwords match
        if (!password.equals(repeatedPassword)) {
            mRepeatedPasswordView.setError(getString(R.string.error_password_match));
            focusView = mRepeatedPasswordView;
            cancel = true;
        }

        /**
         * Check password validity here if not usin Firebase User Authentication
         */
        /*
        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        */

        // Check for a valid email address.
        if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            createAccount(email, password);

        }
    }

    private boolean validateFormFilled() {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String repeatedPassword = mRepeatedPasswordView.getText().toString();

        boolean loginConditions = !(TextUtils.isEmpty(email)) && !(TextUtils.isEmpty(password));
        boolean newAccConditions = loginConditions && !(TextUtils.isEmpty(repeatedPassword));

        if (repeatedPasswordLayout.getVisibility() == View.GONE)
            return loginConditions;
        else
            return newAccConditions;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormGroup.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
            mLoginFormGroup.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormGroup.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormGroup.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
        }
    }




    /**
     * Authentication methods
     */

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        /* When comment to check form is filled
        if (!validateFormFilled()) {
            return;
        }
        */

        //showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            /**
                             * Uncomment for development check
                             */
                            //Toast.makeText(LoginActivity.this, "Authentication was successful.",Toast.LENGTH_SHORT).show();
                            updateUI(user);
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                mPasswordView.setError(getString(R.string.incorrect_password));
                                mPasswordView.requestFocus();
                            } catch (FirebaseAuthInvalidUserException e) {
                                mEmailView.setError(getString(R.string.incorrect_email));
                                mEmailView.requestFocus();
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                                showProgress(false);
                            }


                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            /**
                             * Uncomment for development check
                             */

                            //Toast.makeText(LoginActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                            showProgress(false);


                            //updateUI(null);
                        }


                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateFormFilled()) {
            return;
        }

        //showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            final FirebaseUser user = mAuth.getCurrentUser();

                            /**
                             * Uncomment for development check
                             */
                            //Toast.makeText(LoginActivity.this, "Account creation succeeded.",Toast.LENGTH_SHORT).show();

                            // Add a new document with a generated id.
                            // TODO Save User Id and Email in Mongo


                            Map<String, Object> data = new HashMap<>();
                            data.put("email", user.getEmail());
                            CollectionReference usersCollectionRef = dbFirestore.collection("users");
                            usersCollectionRef
                                    .document(user.getUid())
                                    .set(data)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "User id successfully written into Firestore Database!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "User id couln't be written into Firestore Database!", e);

                                        }
                                    });

                            // Send email verification
                            user.sendEmailVerification().addOnCompleteListener(LoginActivity.this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // TODO Mensaje informando al usuario de que se ha enviado un email de verificación
                                        Toast.makeText(LoginActivity.this, "Se ha enviado un email de verificación a la cuenta " + user.getEmail(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.e(TAG, "sendEmailVerification", task.getException());
                                        /**
                                         * Uncomment for development check
                                         */
                                        //Toast.makeText(LoginActivity.this,"Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                    }
                                    // [END_EXCLUDE]
                                }
                            });

                            updateUI(user);
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                mPasswordView.setError(getString(R.string.error_invalid_password));
                                mPasswordView.requestFocus();
                            } catch (FirebaseAuthUserCollisionException e) {
                                mEmailView.setError(getString(R.string.error_user_exists));
                                mEmailView.requestFocus();
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                            }


                            /**
                             * Uncomment for development check
                             */
                            /*
                            // If sign in fails, display a message to the user.
                            Log.d(TAG, "createUserWithEmail:failure " + task.getException());
                            Toast.makeText(LoginActivity.this, "Account creation failed message." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                            */

                            //updateUI(null);
                            showProgress(false);
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }


    /**
     * UI Update
     *
     * @param user show error message if null
     *             complete login if valid user
     */
    private void updateUI(FirebaseUser user) {
        //hideProgressDialog();
        if (user != null) {
            Log.d(TAG, "Logged user is " + user.getEmail());
            //String name = user.getDisplayName();
            //String email = user.getEmail();
            //Uri photoUrl = user.getPhotoUrl();


            // check user has completed starting form
            DocumentReference docRef = dbFirestore.collection("users").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        Intent intent;
                        // Check if user has a schedule. If not start UserScheduleActivity
                        if (document.get("plan") != null) {
                            intent = new Intent(LoginActivity.this, MainActivity.class);
                        } else {
                            intent = new Intent(LoginActivity.this, UserScheduleActivity.class);
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
                        finish();
                    } else {
                        showProgress(false);
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

            //mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,
            //        user.getEmail(), user.isEmailVerified()));
            //mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            //findViewById(R.id.email_password_buttons).setVisibility(View.GONE);
            //findViewById(R.id.email_password_fields).setVisibility(View.GONE);
            //findViewById(R.id.signed_in_buttons).setVisibility(View.VISIBLE);

            //findViewById(R.id.verify_email_button).setEnabled(!user.isEmailVerified());
        } else {
            Log.d(TAG, "Updating UI but user is no longer logged");
            showProgress(false);
            /* Contraseña incorrecta alert
            // 1. Instantiate an AlertDialog.Builder with its constructor
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage(R.string.alert_login_fail_message).setTitle(R.string.alert_login_fail_title);

            // Add the buttons
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    dialog.dismiss();
                }
            });

            // 3. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();
            dialog.show();
            */

            //mStatusTextView.setText(R.string.signed_out);
            //mDetailTextView.setText(null);

            //findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
            //findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
            //findViewById(R.id.signed_in_buttons).setVisibility(View.GONE);
        }
    }
}


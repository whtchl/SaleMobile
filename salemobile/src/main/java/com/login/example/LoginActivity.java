package com.login.example;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.bingoogolapple.refreshlayout.salemobile.*;

import cm.hardwarereport.example.*;

import android.content.SharedPreferences;
import android.app.Activity;

/**
 * Created by tchl on 11/4/15.
 */
public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener {//, LoaderManager.LoaderCallbacks<Cursor

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button mEmailSignInButton;
    ProgressDialog ringProgressDialog;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;

    static String TAG = "LoginActivity";

    private boolean isManager = false;

    App getApp() {
        return (App) getApplication();
    }
    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };


    private UserLoginTask mAuthTask = null;
        @Override
     protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
/*        FacebookSdk.sdkInitialize(getApplicationContext());*/
                setContentView(R.layout.activity_login);

                initInstances();
        }

    private void initInstances() {
        // Set up the login form.
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.txt_email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.txt_password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(this);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }





    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        /*getLoaderManager().initLoader(0, null, this);*/
    }



    private boolean mayRequestContacts() {
         return false;
    /*    if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;*/
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        ValidateUserInfo validateUserInfo = new ValidateUserInfo();

        if(TextUtils.isEmpty(password)){
            mPasswordView.setError(getString(R.string.error_no_password));
            focusView = mPasswordView;
            cancel = true;
        }
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !validateUserInfo.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } /*else if (!validateUserInfo.isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }*/

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    @Override
    public void onClick(View v) {
        String email = mEmailView.getText().toString();

        switch (v.getId()) {
            case R.id.email_sign_in_button:
                attemptLogin();
                break;
        }
    }

    private void onSignInClicked() {
//        toastLoading.show();
        // User clicked the sign-in button, so begin the sign-in process and automatically
        // attempt to resolve any errors that occur.
        ringProgressDialog = ProgressDialog.show(LoginActivity.this, "Connecting...", "Atempting to connect", true);
        ringProgressDialog.setCancelable(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mShouldResolve = true;
/*                    mGoogleApiClient.connect();*/
                } catch (Exception e) {
                    ringProgressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }).start();
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

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

/*        private void  isManager(String name,String shortphone){
            ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.USERINFO);
            query.whereEqualTo(Constants.PARSEUSERS, mEmail);
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> scoreList, ParseException e) {
                    if (e == null) {

                        if (scoreList.size() > 0) {
                            isManager = true;

                        } else {
                            isManager = false;
                        }
                        Log.d("score", "tchl Retrieved " + scoreList.size() + " scores" + " isManager:" + isManager);
                    } else {
                        Log.d("score", "tchl Error: " + e.getMessage());
                        isManager = false;
                    }
                }
            });
        }*/
        private void  saveInfo() {
        //实例化SharedPreferences对象（第一步）
        SharedPreferences mySharedPreferences = getSharedPreferences(Constants.SHAREPRE, Activity.MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象（第二步）
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        //用putString的方法保存数据
        // editor.putString("")
        editor.putString(Constants.NAME,mEmail);
        editor.putString(Constants.SHORTPHONE, mPassword);
        //editor.putBoolean(Constants.ISMANAGER,true);
        //提交当前数据
        editor.commit();
        //使用toast信息提示框提示成功写入数据
        //Toast.makeText(LoginActivity.this, "数据成功写入SharedPreferences！" , Toast.LENGTH_LONG).show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            Log.i(TAG,"tchl doInBackground");
           // try {
                // Simulate network access.
                //Thread.sleep(2000);
                /*isManager(mEmail,mPassword);*/
                getApp().setIsManager(mEmail);
                saveInfo();
            //}
            /*catch (InterruptedException e) {
                return false;
            }
*/
/*            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }*/

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                startActivity(new Intent(LoginActivity.this, cn.bingoogolapple.refreshlayout.salemobile.ui.activity.MainActivity.class));
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

    }

}
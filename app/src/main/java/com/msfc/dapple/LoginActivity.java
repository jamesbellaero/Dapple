package com.msfc.dapple;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, CompletedListener {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private String cookie="";
    ListenableRequest request;
    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView musernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        musernameView = (AutoCompleteTextView) findViewById(R.id.username);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
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

        Button musernameSignInButton = (Button) findViewById(R.id.username_sign_in_button);
        musernameSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(musernameView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
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
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        musernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = musernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
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

            RequestQueue queue = Volley.newRequestQueue(this);

            String url ="http://sample-env-2.m6ifx8gegy.us-east-2.elasticbeanstalk.com:80/api/login";
//                String url = "http://10.0.2.2:80/api/login";
            request = new ListenableRequest( Request.Method.POST,url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            try {
                                completed(true);

//                                    VolleyLog.v("Response:%n %s", response);
                            } catch (Exception e) {
                                e.printStackTrace();

                                completed(false);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    completed(false);
                    int a = error.networkResponse.statusCode;

                }
            },this);
            Map<String,String> parameters=new HashMap<>();
            parameters.put("username","james");
            parameters.put("password","passdiddle");
            request.setParams(parameters);
// Add the request to the RequestQueue.
            queue.add(request);
          //  mAuthTask = new UserLoginTask(username, password,this);
           // mAuthTask.execute((Void) null);
        }
    }

    private boolean isusernameValid(String username) {
        //TODO: Replace this with your own logic
        return username.contains("@");
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
    public void completed(boolean success){
        showProgress(false);
        if(success){
            cookie=request.getCookies();
            Intent intent = new Intent(this,MenuActivity.class);
            intent.putExtra("cookie",cookie);
            startActivity(intent);
        }

    }
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only username addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Nickname
                .CONTENT_ITEM_TYPE},

                // Show primary username addresses first. Note that there won't be
                // a primary username address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> usernames = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            usernames.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addusernamesToAutoComplete(usernames);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addusernamesToAutoComplete(List<String> usernameAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, usernameAddressCollection);

        musernameView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Nickname.NAME,
                ContactsContract.CommonDataKinds.Nickname.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String musername;
        private final String mPassword;
        CompletedListener listener;
        UserLoginTask(String username, String password, CompletedListener listener) {
            musername = username;
            mPassword = password;
            this.listener=listener;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try{
//                URL url = new URL("http://sample-env-2.m6ifx8gegy.us-east-2.elasticbeanstalk.com:80/api/login");
//               // Toast t = new Toast((LoginActivity)listener);
//               // t.show();
//                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//                String postDataString="{\"username\":\"james\",\"password\":\"3821594011\"}";
//                byte[] postData=postDataString.getBytes();
//                conn.setDoOutput(true);
//                conn.setDoOutput(true);
//                conn.setInstanceFollowRedirects(false);
//                conn.setRequestMethod("POST");
//                conn.setRequestProperty("Content-Type","application/json");
//                conn.setRequestProperty("Content-Length",""+postData.length);
//                conn.setRequestProperty("charset","UTF-8");
//
//                DataOutputStream wr=new DataOutputStream(conn.getOutputStream());
//                wr.write(postData);
//                wr.close();
//                conn.getOutputStream().close();
//                int a =conn.getResponseCode();
//                String b = conn.getResponseMessage();
//                Map<String, List<String>> headerFields = conn.getHeaderFields();
//                cookie=headerFields.get("Set-Cookie").get(0);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }


            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
               // listener.completed(true);
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
             //   listener.completed(false);
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
       //     listener.completed(false);
        }
    }
}

package com.zhunio.twitter.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.zhunio.twitter.R;
import com.zhunio.twitter.async.LogInCallback;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.parse.ParseUser.getCurrentUser;
import static com.zhunio.twitter.util.Util.displayAlert;
import static com.zhunio.twitter.util.Util.isPasswordValid;
import static com.zhunio.twitter.util.Util.isUsernameValid;

/**
 * Allows the user to log into the app using {@link Parse}'s framework.
 *
 * @author Richard I. Zhunio
 * @see Parse
 * @see ParseException
 * @see ParseUser
 * @see LogInCallback
 */
public class LogInActivity extends AppCompatActivity implements LogInCallback {

    /** Activity tag for logging. */
    private static final String TAG = "LogInActivity";

    /** Request code for Sign up. */
    private static final int REQUEST_SIGN_UP = 0;

    /** Bind the following views */
    @BindView(R.id.edit_username) EditText mEditUsername;
    @BindView(R.id.edit_password) EditText mEditPassword;
    @BindView(R.id.btn_login) Button mButtonLogin;
    @BindView(R.id.text_sign_up_link) TextView mTextSignUpLink;
    @BindView(R.id.progress_bar) ProgressBar mProgressBar;

    /**
     * If the {@link SignUpActivity} returned a {@code REQUEST_SIGN_UP} and the {@code resultCode}
     * is {@code RESULT_OK}, then we log in using the {@link #onLoginSuccess} method.
     *
     * @see Activity#onActivityResult
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGN_UP) {
            if (resultCode == RESULT_OK)
                onLoginSuccess(getCurrentUser());

        }
    }

    /**
     * Log in the current user if has already signed in.
     *
     * @param savedInstanceState The {@link Bundle} containing all saved data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        ButterKnife.bind(this);


        // Log the current user
        if (getCurrentUser() != null)
            onLoginSuccess(getCurrentUser());
    }

    /**
     * Checks whether the form is syntactically correct and then tries to to sign in the user.
     *
     * @param view {@link EditText} clicked.
     */
    public void onLogin(View view) {
        Log.d(TAG, "Logging In...");

        // If form is invalid, let the user correct the issue and try again
        if (!formIsValid())
            return;

        // Disable login button while attempting to log in
        mButtonLogin.setEnabled(false);

        // Get username and password
        String username = mEditUsername.getText().toString();
        String password = mEditPassword.getText().toString();

        // Enable progress bar
        mProgressBar.setVisibility(VISIBLE);

        // Log in user
        // onLoginResponse handles the callback
        ParseUser.logInInBackground(username, password, this::onLoginResponse);
    }

    /**
     * Once {@link ParseUser#logInInBackground} has finished authenticating and signing in the user,
     * we allow the {@code user} access to the app if it has been authenticated correctly.
     *
     * @param user      {@link ParseUser} from the server.
     * @param exception {@link ParseException} from the server.
     *
     * @see LogInCallback
     */
    @Override
    public void onLoginResponse(ParseUser user, ParseException exception) {
        // Disable progress bar
        mProgressBar.setVisibility(GONE);

        // If user is not null we can allow the user access to the app, otherwise not.
        if (user != null)
            onLoginSuccess(user);
        else
            onLoginFailed(exception);
    }

    /**
     * If {@link LogInActivity} is successful, start the {@link TweetsActivity}.
     *
     * @param user {@link ParseUser} received upon successful log in.
     */
    private void onLoginSuccess(ParseUser user) {
        String message = "Welcome " + user.getUsername() + "!";

        Log.i(TAG, message);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        // Start tweets activity
        Intent intent = new Intent(getApplicationContext(), TweetsActivity.class);
        startActivity(intent);
        finish();
    }


    /**
     * If {@link LogInActivity} is unsuccessful, allow the user to try again.
     *
     * @param exception {@link Exception} received upon unsuccessful log in.
     */
    private void onLoginFailed(Exception exception) {
        String message = exception.getMessage() + " Please Try again.";

        Log.i(TAG, message);
        displayAlert(this, "Authentication", message);

        mButtonLogin.setEnabled(true);
    }

    /**
     * Handler for starting the {@link SignUpActivity}.
     *
     * @param view {@link EditText} clicked.
     */
    public void onSignUp(View view) {
        Log.i(TAG, "From " + TAG + " to " + SignUpActivity.class.getSimpleName());

        // Start the SignUpActivity
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivityForResult(intent, REQUEST_SIGN_UP);
    }

    /**
     * Check if the username and password are valid in the form.
     *
     * @return true if the username and password are valid.
     */
    private boolean formIsValid() {
        // Check if username is valid
        boolean valid = isUsernameValid(this, mEditUsername);

        // Check if passwordis valid
        valid = valid && isPasswordValid(this, mEditPassword);

        return valid;
    }
}
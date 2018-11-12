package com.zhunio.twitter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.zhunio.twitter.R;
import com.zhunio.twitter.User;
import com.zhunio.twitter.async.LogInCallback;
import com.zhunio.twitter.async.SignUpCallback;

import org.json.JSONArray;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.zhunio.twitter.User.IS_FOLLOWING;
import static com.zhunio.twitter.util.Util.displayAlert;
import static com.zhunio.twitter.util.Util.isPasswordValid;
import static com.zhunio.twitter.util.Util.isReEnterPasswordValid;
import static com.zhunio.twitter.util.Util.isUsernameValid;

/**
 * Allows the user to sign up into the app using {@link Parse}'s framework.
 *
 * @author Richard I. Zhunio
 * @see Parse
 */
public class SignUpActivity extends AppCompatActivity implements SignUpCallback {

    /** Activity tag for signing up. */
    private static final String TAG = "SignUpActivity";

    /** Bind the following views */
    @BindView(R.id.edit_username) EditText mEditUsername;
    @BindView(R.id.edit_password) EditText mEditPassword;
    @BindView(R.id.edit_reEnterPassword) EditText mEditReEnterPassword;
    @BindView(R.id.btn_sign_up) Button mButtonSignUp;
    @BindView(R.id.progress_bar) ProgressBar mProgressBar;

    /**
     * Bind {@link ButterKnife}'s framework.
     *
     * @param savedInstanceState The {@link Bundle} containing all saved data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
    }

    /**
     * Checks whether the form is syntactically correct and then tries to to register the user.
     *
     * @param view {@link Button} clicked.
     */
    public void onSignUp(View view) {
        Log.i(TAG, "Signing Up...");

        // If form is invalid, let the user correct the issue and try again
        if (!formIsValid())
            return;

        // Disable sign up button while attempting to register
        mButtonSignUp.setEnabled(false);

        // Get username and password
        String username = mEditUsername.getText().toString();
        String password = mEditPassword.getText().toString();

        // Create ParseUser to sign up
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        // Add an array for saving who the user follows
        user.put(IS_FOLLOWING, new JSONArray());

        // Enable progress bar
        mProgressBar.setVisibility(VISIBLE);

        // Sign up user
        // onSignUpResponse handles the callback
        user.signUpInBackground(this::onSignUpResponse);
    }

    /**
     * Once {@link ParseUser#signUpInBackground} has finished authentication and signing up the
     * user, we allow the user to complete the sign up process if it has been authenticated
     * correctly.
     *
     * @param exception {@link Exception} from the server.
     *
     * @see LogInCallback
     */
    @Override
    public void onSignUpResponse(ParseException exception) {
        // Disable progress bar
        mProgressBar.setVisibility(GONE);

        // if exception is null, we can register and allow the user access to the app
        if (exception == null)
            onSignUpSuccess();
        else
            onSignUpFailed(exception);
    }

    /** If {@link SignUpActivity} is successful, start the {@link LogInActivity}. */
    private void onSignUpSuccess() {
        mButtonSignUp.setEnabled(true);

        // Return to LoginActivity
        setResult(RESULT_OK, null);
        finish();
    }

    /**
     * If {@link SignUpActivity} is unsuccessful, allow the user to try again.
     *
     * @param exception {@link Exception} received upon unsuccessful sign up.
     */
    private void onSignUpFailed(Exception exception) {
        String message = exception.getMessage() + " Please Try again.";
        displayAlert(this, "Sign up Failed", message);
        mButtonSignUp.setEnabled(true);
    }

    /**
     * Handler for starting the {@link LogInActivity}.
     *
     * @param view {@link TextView} clicked.
     */
    public void onLogin(View view) {
        Log.i(TAG, "From " + TAG + " to " + LogInActivity.class.getSimpleName());

        // Start the LoginActivity
        Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Checks if the username, password, and reEnterPassword are valid in the form.
     *
     * @return true if the username, password, and reEnterPassword are valid.
     */
    private boolean formIsValid() {
        // Check if username is valid
        boolean valid = isUsernameValid(this, mEditUsername);

        // Check if password is valid
        valid = valid && isPasswordValid(this, mEditPassword);

        // Check if reentered password is valid
        valid = valid && isReEnterPasswordValid(this, mEditPassword, mEditReEnterPassword);

        return valid;
    }
}

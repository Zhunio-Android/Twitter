package com.zhunio.twitter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.zhunio.twitter.R;
import com.zhunio.twitter.Tweet;
import com.zhunio.twitter.async.SaveCallback;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.parse.ParseUser.getCurrentUser;
import static com.zhunio.twitter.util.Util.launchActivity;

/**
 * Allows the user to compose a new {@link Tweet}.
 *
 * @author Richard I. Zhunio
 * @see Parse
 * @see ParseException
 * @see ParseUser
 * @see SaveCallback
 */
public class TweetComposeActivity extends AppCompatActivity implements SaveCallback {

    /** Activity tag for logging. */
    private static final String TAG = "TweetComposeActivity";

    /** Bind the following views */
    @BindView(R.id.edit_compose_tweet) EditText mTextTweet;
    @BindView(R.id.progress_bar) ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_compose);
        ButterKnife.bind(this);
    }

    /**
     * Saves the composed message to the server.
     *
     * @param view {@link Button} clicked.
     */
    public void onTweet(View view) {
        // Enable progress bar
        mProgressBar.setVisibility(VISIBLE);

        // Get tweet message
        String tweetMessage = mTextTweet.getText().toString();
        Tweet tweet = new Tweet(getCurrentUser().getUsername(), tweetMessage);

        // Save tweet
        // onSaveResponse handles the callback
        tweet.saveInBackground(this::onSaveResponse);
    }

    /**
     * Once {@link Tweet#saveInBackground} has finished saving the composed  {@link Tweet} to the
     * server, we come back to the {@link TweetsActivity}.
     *
     * @param exception {@link ParseException} from the server.
     */
    @Override
    public void onSaveResponse(ParseException exception) {
        // Disable progress bar
        mProgressBar.setVisibility(GONE);

        if (exception == null)
            onTweetSuccess();
        else
            onTweetFailed(exception);
    }

    /** If {@link TweetComposeActivity} is successful, start the {@link TweetsActivity}. */
    private void onTweetSuccess() {
        String message = "Tweet saved successfully !";

        Log.i(TAG, message);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        // Finish the compose screen and return to the tweets activity
        launchActivity(this, TweetsActivity.class);
        finish();
    }

    /**
     * If {@link LogInActivity} is unsuccessful, start the {@link TweetsActivity} to allow them to
     * try again.
     *
     * @param exception {@link ParseException} from the server.
     */
    private void onTweetFailed(ParseException exception) {
        String message = exception.getMessage();

        Log.i(TAG, message);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        // Start the tweets activity
        launchActivity(this, TweetsActivity.class);
        finish();
    }
}

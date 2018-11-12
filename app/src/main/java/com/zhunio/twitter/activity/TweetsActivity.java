package com.zhunio.twitter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.zhunio.twitter.R;
import com.zhunio.twitter.Tweet;
import com.zhunio.twitter.async.FindCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.parse.ParseUser.getCurrentUser;
import static com.zhunio.twitter.Tweet.TWEET;
import static com.zhunio.twitter.Tweet.USERNAME;
import static com.zhunio.twitter.User.CREATED_AT;
import static com.zhunio.twitter.User.IS_FOLLOWING;
import static com.zhunio.twitter.util.Util.launchActivity;

/**
 * Allows the user to see the {@link Tweet}s feed from the people it follows.
 *
 * @author Richard I. Zhunio
 * @see Parse
 * @see ParseException
 * @see ParseUser
 * @see FindCallback
 */
public class TweetsActivity extends AppCompatActivity implements FindCallback<Tweet> {

    /** Activity tag for logging */
    private static final String TAG = "TweetsActivity";

    /** Bind the following view */
    @BindView(R.id.list_tweets) ListView mListViewTweets;

    /** Inflates the tweets menu. */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tweets, menu);

        return true;
    }

    /**
     * Adds functionality for when the logout or following menu items are selected.
     *
     * @param item {@link MenuItem} selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_logout:
                onLogOut();
                return true;
            case R.id.menu_following:
                onFollowing();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /** Retrieve {@link Tweet}s from the server. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets);
        ButterKnife.bind(this);

        // Retrieve tweets only from people that the current user follows
        ParseQuery.getQuery(Tweet.class)
                .whereContainedIn(USERNAME, getCurrentUser().getList(IS_FOLLOWING))
                .orderByDescending(CREATED_AT)
                // onFindResponse handles the callback
                .findInBackground(this::onFindResponse);
    }

    /**
     * Set ups the {@link ListView} and its {@link ListAdapter} to populate the {@link
     * TweetsActivity}.
     *
     * @param tweets    List of {@link Tweet}s received.
     * @param exception {@link ParseException} received.
     */
    @Override
    public void onFindResponse(List<Tweet> tweets, ParseException exception) {
        if (exception == null) {
            // Column names associated with each tweet
            String[] columnNames = new String[]{USERNAME, TWEET};
            // Views to display the tweets
            int[] views = new int[]{R.id.text_user, R.id.text_tweet};

            // Adapter for double-row list item
            ListAdapter adapter = new SimpleAdapter(TweetsActivity.this, toListMap(tweets),
                    R.layout.list_tweet_row, columnNames, views);

            // Set the adapter
            mListViewTweets.setAdapter(adapter);
        }
    }

    /** Start the {@link UsersFollowingActivity}. */
    private void onFollowing() {
        launchActivity(this, UsersFollowingActivity.class);
    }

    /** Log out the current user and start the {@link LogInActivity} */
    private void onLogOut() {
        String message = "Bye bye " + getCurrentUser().getUsername() + "!";

        Log.i(TAG, message);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        ParseUser.logOutInBackground(exception -> {
            if (exception == null) {
                // Return to the Login activity and finish this activity
                launchActivity(this, LogInActivity.class);
                finish();
            }
        });
    }

    /** Start the {@link TweetComposeActivity} to compose a new tweet. */
    public void onTweet(View view) {
        launchActivity(this, TweetComposeActivity.class);
    }

    /**
     * Converts a {@link List} of {@link Tweet}s to a {@link List} of {@link Map}s mapping a {@link
     * String} to a {@link String} containing {@link Tweet#USERNAME} and {@link Tweet#TWEET} as
     * keys.
     *
     * @param tweets {@link List} of {@link Tweet}s.
     */
    private List<Map<String, String>> toListMap(List<Tweet> tweets) {
        List<Map<String, String>> tweetListMap = new ArrayList<>();

        for (Tweet tweet : tweets) {
            HashMap<String, String> tweetMap = new HashMap<>();
            tweetMap.put(USERNAME, tweet.getUsername());
            tweetMap.put(TWEET, tweet.getTweet());
            tweetListMap.add(tweetMap);
        }

        return tweetListMap;
    }

}

package com.zhunio.twitter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.zhunio.twitter.R;
import com.zhunio.twitter.async.FindCallback;
import com.zhunio.twitter.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.parse.ParseUser.getCurrentUser;
import static com.zhunio.twitter.User.IS_FOLLOWING;
import static java.util.Objects.requireNonNull;

/**
 * Allows the user to follow {@link ParseUser}s.
 *
 * @author Richard I. Zhunio
 * @see Parse
 * @see ParseException
 * @see ParseUser
 * @see FindCallback
 * @see AdapterView
 */
public class UsersFollowingActivity extends AppCompatActivity implements FindCallback<ParseUser>,
        AdapterView.OnItemClickListener {

    /** Activity tag for logging. */
    private static final String TAG = "UsersFollowingActivity";

    /** Adapter for populating {@link ListView}. */
    private ArrayAdapter<String> mAdapter;

    /** {@link List} of usernames. */
    private List<String> mUsernames;

    /** Bind the following views */
    @BindView(R.id.list_following) ListView mListViewUsers;

    /**
     * Retrieve {@link ParseUser} from the server.
     *
     * @param savedInstanceState The {@link Bundle} containing all saved data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);
        ButterKnife.bind(this);

        // Init usernames
        mUsernames = new ArrayList<>();

        // Init adapter
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked, mUsernames);

        // Init list view
        mListViewUsers.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        mListViewUsers.setAdapter(mAdapter);
        mListViewUsers.setOnItemClickListener(this);

        // Get users
        // onFindResponse handles the callback
        ParseQuery.getQuery(ParseUser.class).findInBackground(this::onFindResponse);
    }

    /**
     * Once the {@link ParseQuery#findInBackground} has finished finding all users, filter out the
     * current user and also set which users are being followed.
     *
     * @param users     {@link ParseUser}s from the server.
     * @param exception {@link ParseException} from the server.
     */
    @Override
    public void onFindResponse(List<ParseUser> users, ParseException exception) {
        // Retrieve the list of following people
        List<String> isFollowing = requireNonNull(getCurrentUser().getList(IS_FOLLOWING));

        if (exception == null && !users.isEmpty()) {

            // Convert user list to user string list
            for (ParseUser user : users) {
                if (!user.getUsername().equals(getCurrentUser().getUsername()))
                    mUsernames.add(user.getUsername());
            }

            // Notify the adapter of data changed
            mAdapter.notifyDataSetChanged();

            // Check which users are being followed
            for (int i = 0; i < mUsernames.size(); i++) {
                if (isFollowing.contains(mUsernames.get(i)))
                    mListViewUsers.setItemChecked(i, true);
            }
        }

    }


    /** Each {@link CheckedTextView} item listener. */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Cast the item view
        CheckedTextView checkView = (CheckedTextView) view;

        // Retrieve the list of following people
        List<String> isFollowing = requireNonNull(getCurrentUser().getList(IS_FOLLOWING));

        // if check view is checked, add it to the following list
        // otherwise, remove the item from the following list
        if (checkView.isChecked())
            getCurrentUser().add(IS_FOLLOWING, mUsernames.get(position));
        else {
            // isFollowing.remove(Object) does not work
            // we must use this work around
            isFollowing.remove(mUsernames.get(position));
            getCurrentUser().remove(IS_FOLLOWING);
            getCurrentUser().put(IS_FOLLOWING, isFollowing);
        }

        getCurrentUser().saveInBackground();
    }


    /** Starts the {@link TweetsActivity}. */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Util.launchActivity(this, TweetsActivity.class);
        finish();
    }
}

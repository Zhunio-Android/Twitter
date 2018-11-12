package com.zhunio.twitter;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Subclass of {@link ParseObject} for type completion.
 *
 * @author Richard I. Zhunio
 */
@ParseClassName("Tweet")
public class Tweet extends ParseObject {

    /** keys used on the {@link Tweet}. */
    public static final String USERNAME = "username";
    public static final String TWEET = "tweet";

    public Tweet() {}

    public Tweet(String username, String tweet) {
        setUser(username);
        setTweet(tweet);
    }

    public String getUsername() {
        return getString(USERNAME);
    }

    public void setUser(String username) {
        put(USERNAME, username);
    }

    public String getTweet() {
        return getString(TWEET);
    }

    public void setTweet(String tweet) {
        put(TWEET, tweet);
    }
}

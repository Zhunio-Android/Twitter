package com.zhunio.twitter.async;

import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Represents an asynchronous response receiving a {@link ParseUser} and a {@link ParseException}.
 *
 * @author Richard I. Zhunio
 */
public interface LogInCallback {

    /**
     * Override this code to perform an operation with the {@link ParseUser} and {@link
     * ParseException}.
     *
     * @param user {@link ParseUser} received from the server.
     * @param exception {@link ParseException} received from the server.
     */
    void onLoginResponse(ParseUser user, ParseException exception);
}

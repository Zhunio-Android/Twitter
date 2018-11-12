package com.zhunio.twitter.async;

import com.parse.ParseException;

/**
 * Represents an asynchronous response receiving a {@link ParseException}.
 *
 * @author Richard I. Zhunio
 */
public interface SignUpCallback {

    /**
     * Override this code to perform an operation with the {@link ParseException}.
     *
     * @param exception {@link ParseException} received from the server.
     */
    void onSignUpResponse(ParseException exception);
}

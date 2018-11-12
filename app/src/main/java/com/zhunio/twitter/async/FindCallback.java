package com.zhunio.twitter.async;

import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;

/**
 * Represents an asynchronous response receiving a {@link List} and a {@link ParseException}.
 *
 * @author Richard I. Zhunio
 * @see ParseObject
 */
public interface FindCallback<T extends ParseObject> {

    /**
     * Override this code to perform an operation with the {@link List} and {@link ParseException}.
     *
     * @param objects   {@link List} received from the server.
     * @param exception {@link ParseException} received from the server.
     */
    void onFindResponse(List<T> objects, ParseException exception);
}

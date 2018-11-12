package com.zhunio.twitter;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

/** App used to initialize {@link com.parse}. */
public class TwitterApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Register Tweet subclass
        ParseObject.registerSubclass(Tweet.class);

        // Initialize parse
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.app_id))
                // if defined
                .clientKey(getString(R.string.client_key))
                .server(getString(R.string.server_url))
                .build()
        );

        // Save the current Installation to Back4App
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}

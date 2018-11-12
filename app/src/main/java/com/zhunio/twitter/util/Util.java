package com.zhunio.twitter.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.TextView;

import com.zhunio.twitter.R;

/**
 * Utility functions.
 *
 * @author Richard I. Zhunio
 */
public class Util {

    /**
     * Displays an {@link AlertDialog} inside the given context with the given title and message.
     *
     * @param context {@link Context} to display this {@link AlertDialog} on.
     * @param title   The title of the {@link AlertDialog}.
     * @param message The message of the {@link AlertDialog}.
     */
    public static void displayAlert(Context context, String title, String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.cancel())
                .create()
                .show();
    }

    /**
     * Notifies the user view the {@link TextView#setError} method of any issue in the given view.
     *
     * @param view       the view currently being edited.
     * @param errMessage the message to set {@link TextView#setError} to.
     */
    public static void notifyUserViaView(TextView view, String errMessage) {
        view.setError(errMessage);
    }

    /**
     * Check whether the email is valid or not.
     *
     * @return true if the email is valid.
     */
    public static boolean isUsernameValid(Context context, EditText editUsername) {

        String username = editUsername.getText().toString();
        boolean valid = true;

        // Check if username is valid
        if (username.isEmpty() || username.length() < 5) {
            notifyUserViaView(editUsername, context.getString(R.string.edit_username_error));
            valid = false;
        } else
            notifyUserViaView(editUsername, null);

        return valid;
    }

    /**
     * Check whether the password is valid or not.
     *
     * @return true if the password is valid.
     */
    public static boolean isPasswordValid(Context context, EditText editPassword) {

        String password = editPassword.getText().toString();
        boolean valid = true;

        // Check if username is valid
        if (password.isEmpty() || password.length() < 5) {
            notifyUserViaView(editPassword, context.getString(R.string.edit_password_error));
            valid = false;
        } else
            notifyUserViaView(editPassword, null);

        return valid;
    }

    /**
     * Check whether the editPassword and password are valid or not.
     *
     * @return true if they both match and are valid.
     */
    public static boolean isReEnterPasswordValid(Context context, EditText editPassword, EditText
            editReEnterPassword) {

        String password = editPassword.getText().toString();
        String reEnterPassword = editReEnterPassword.getText().toString();

        boolean valid = true;

        // Check if reEnterPassword is valid
        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 5 ||
                !(reEnterPassword.equals(password))) {
            notifyUserViaView(editReEnterPassword, context.getString(R.string
                    .edit_password_dont_match));
            valid = false;
        } else
            notifyUserViaView(editReEnterPassword, null);

        return valid;
    }

    /**
     * Launches the {@code nextClass} activity from the given {@link Activity}.
     *
     * @param activity  {@link Activity} switching from.
     * @param nextClass next {@link Class} to launch.
     */
    public static void launchActivity(Activity activity, Class<?> nextClass) {
        Intent intent = new Intent(activity.getApplicationContext(), nextClass);
        activity.startActivity(intent);
    }
}

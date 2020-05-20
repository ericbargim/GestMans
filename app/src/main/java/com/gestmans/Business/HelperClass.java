package com.gestmans.Business;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AlertDialog;

import com.gestmans.R;

public class HelperClass {
    public static void createDialogMessageSingle(String title, String message, String buttonMessage, Context context) {
        // Create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setNeutralButton(buttonMessage, (dialog, which) -> dialog.dismiss())
                .create();
        final AlertDialog dialog = builder.show();

        // Change the button color
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(context.getColor(R.color.purpleGradient));

        // Show the dialog
        dialog.show();
    }

    public static void createDialogMessageDual(String title, String message, String positiveButtonMessage, String negativeButtonMessage, Context context) {
        // Create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonMessage, (dialog, which) -> dialog.dismiss())
                .setNegativeButton(negativeButtonMessage, (dialog, which) -> dialog.dismiss())
                .create();
        final AlertDialog dialog = builder.show();

        // Change the buttons color
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(context.getColor(R.color.purpleGradient));

        // Show the dialog
        dialog.show();
    }

    public static void showKeyboard(Activity activity) {
        // Get the keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        try {
            // If the keyboard is not null
            if (inputMethodManager != null) {

                // Force to show the keyboard
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

    }

    public static void hideKeyboard(Activity activity) {
        // Get the keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        try {
            // If the keyboard is not null
            if (inputMethodManager != null) {

                // Hide the keyboard
                inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }
}

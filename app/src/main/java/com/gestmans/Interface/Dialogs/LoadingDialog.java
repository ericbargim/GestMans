package com.gestmans.Interface.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Window;

import com.gestmans.R;

public class LoadingDialog {

    private Activity activity;
    private AlertDialog alertDialog;

    public LoadingDialog(Activity myActivity) {
        activity = myActivity;
    }

    public void startLoadingDialog() {
        // Create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        // Inflate the XML layout
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_loading_screen, null));

        // Do nothing when the user clicks on screen
        builder.setCancelable(false);

        // Show the dialog
        alertDialog = builder.create();

        Window window = alertDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(R.drawable.component_round);
        }

        alertDialog.show();
    }

    public void dismissDialog() {
        // Dismiss the dialog
        alertDialog.dismiss();
    }

}

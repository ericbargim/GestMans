package com.gestmans.Business;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AlertDialog;

import com.gestmans.R;

import java.util.ArrayList;
import java.util.List;

public class HelperClass {

    private static final String[] orderDishes = new String[]{"first", "second", "dessert", "drink", "menu"};

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

    public static String sortArray(String[] givenDishesTypes) {
        ArrayList<String> al = new ArrayList<>();
        for (String orderDish : orderDishes) {
            for (String givenDishesType : givenDishesTypes) {
                if (givenDishesType.equals(orderDish)) {
                    al.add(givenDishesType);
                    break;
                }
            }
        }
        String returningData = "";
        for (String element : al) {
            returningData += element + "-";
        }
        return returningData;
    }

    public static String removeLastChar(String givenString) {
        try {
            // Erase the leftover '-'
            givenString = givenString.substring(0, givenString.length() - 1);
            Log.d(App.getContext().getString(R.string.HELPER_CLASS), givenString);
            return givenString;
        } catch (StringIndexOutOfBoundsException ex) {
            return "error";
        }
    }

    public static List<String> capitalizeLetters(List<String> givenList) {
        List<String> al = new ArrayList<>();
        for (String element : givenList) {
            element = element.substring(0, 1).toUpperCase() + element.substring(1);
            al.add(element);
        }
        return al;
    }
}

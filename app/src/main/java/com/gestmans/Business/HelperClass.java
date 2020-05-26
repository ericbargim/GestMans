package com.gestmans.Business;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;

import androidx.appcompat.app.AlertDialog;

import com.gestmans.Business.Objects.Dish;
import com.gestmans.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HelperClass {

    public static final String[] orderDishes = new String[]{"starter", "first", "second", "dessert", "drink", "menu"};

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

    public static boolean createDialogMessageDual(String title, String message, String positiveButtonMessage, String negativeButtonMessage, Context context) {
        final boolean[] positiveClicked = {false};
        // Create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonMessage, (dialog, which) -> {
                    positiveClicked[0] = true;
                    Log.d(App.getContext().getString(R.string.HELPER_CLASS), "Positive clicked");
                })
                .setNegativeButton(negativeButtonMessage, (dialog, which) -> {
                    dialog.dismiss();
                    Log.d(App.getContext().getString(R.string.HELPER_CLASS), "Negative clicked");
                })
                .create();
        final AlertDialog dialog = builder.show();

        // Change the buttons color
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getColor(R.color.purpleGradient));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getColor(R.color.purpleGradient));

        // Show the dialog
        dialog.show();
        return positiveClicked[0];
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

        // Loop the list and capitalize first letter of each string
        for (String element : givenList) {
            element = element.substring(0, 1).toUpperCase() + element.substring(1).toLowerCase();
            al.add(element);
        }
        return al;
    }

    public static boolean checkDishTypeExistsInOrder(ListAdapter listAdapter, String neededDishType) {
        boolean dishTypeExists = false;

        // Loop each dish of the Order ListView until it finds the neededDishType
        for (int i = 0; i < listAdapter.getCount(); i++) {
            Dish actualDish = (Dish) listAdapter.getItem(i);
            if (actualDish.getDishType().toLowerCase().equals(neededDishType)) {
                dishTypeExists = true;
                break;
            }
        }
        return dishTypeExists;
    }

    public static JSONArray itemsToJSON(ListAdapter listAdapter, String neededDishType) {
        List<JSONObject> allDishes = new ArrayList<>();
        JSONArray arrayAllDishesJSON = new JSONArray();
        int numDishes = 1;

        // Loop the dishes of the Order ListView
        for (int i = 0; i < listAdapter.getCount(); i++) {
            Dish actualDish = (Dish) listAdapter.getItem(i);

            // Check if the dish is the neededDishType
            if (actualDish.getDishType().toLowerCase().equals(neededDishType)) {
                JSONObject dishPartsJSON = new JSONObject();
                JSONArray dishArrayJSON = new JSONArray();
                JSONObject dishCompleteJSON = new JSONObject();

                // Get the ID and Quantity of the dish
                String dishId = actualDish.getId();
                String dishQuantity = String.valueOf(actualDish.getQuantity());
                try {
                    // Put both ID and Quantity into a single JSONObject
                    dishPartsJSON.put("name", dishId);
                    Log.d(App.getContext().getString(R.string.HELPER_CLASS), dishPartsJSON.toString());
                    dishPartsJSON.put("quantity", dishQuantity);
                    Log.d(App.getContext().getString(R.string.HELPER_CLASS), dishPartsJSON.toString());

                    // Add the created dish object to an array
                    dishArrayJSON.put(dishPartsJSON);
                    Log.d(App.getContext().getString(R.string.HELPER_CLASS), dishArrayJSON.toString());

                    // And create a JSONObject with the index and the array
                    dishCompleteJSON.put(String.valueOf(numDishes), dishArrayJSON);
                    Log.d(App.getContext().getString(R.string.HELPER_CLASS), dishCompleteJSON.toString());

                    // Finally, add the object to a List
                    allDishes.add(dishCompleteJSON);

                    // And add 1 to the index counter
                    numDishes++;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        // Loop the objects of the list
        for (JSONObject actualObject : allDishes) {
            // Add all the dishes objects of the list to an array
            Log.d(App.getContext().getString(R.string.HELPER_CLASS), actualObject.toString());
            arrayAllDishesJSON.put(actualObject);
        }
        Log.d(App.getContext().getString(R.string.HELPER_CLASS), arrayAllDishesJSON.toString());

        // Return the filled array
        return arrayAllDishesJSON;
    }

    public static JSONArray emptyJSON() {
        // Simply return a simple array
        return new JSONArray();
    }
}

package com.gestmans.Business.Utilities;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;

import androidx.appcompat.app.AlertDialog;

import com.gestmans.Business.Adapters.ListViewOrderMenusAdapter;
import com.gestmans.Business.Objects.Dish;
import com.gestmans.Business.Objects.Menu;
import com.gestmans.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelperClass {

    public static int CAMERA_PERMISSION = 1;

    public static final String[] orderDishTypes = new String[]{"drink", "starter", "first", "second", "dessert"};
    public static final String[] orderMenuDishTypes = new String[]{"drink", "first", "second", "dessert"};

    public static void createDialogMessageNeutral(String title, String message, String buttonMessage, Context context) {
        // Create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
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

        // Loop the possible dish types
        for (String orderDish : orderDishTypes) {
            // Loop the given dish types
            for (String givenDishesType : givenDishesTypes) {
                // If the given dish type is on the possible dish types, add it to the List
                if (givenDishesType.equals(orderDish)) {
                    al.add(givenDishesType);
                    break;
                }
            }
        }
        String returningData = "";
        // Format the List, to return it as a string
        for (String element : al) {
            returningData += element + "-";
        }
        Log.d(App.getContext().getString(R.string.HELPER_CLASS) + "Sorted array", returningData);
        return returningData;
    }

    public static String removeLastChar(String givenString) {
        try {
            // Erase the leftover '-'
            givenString = givenString.substring(0, givenString.length() - 1);
            Log.d(App.getContext().getString(R.string.HELPER_CLASS) + "Last char removed", givenString);
            return givenString;
        } catch (StringIndexOutOfBoundsException ex) {
            return "error";
        }
    }

    public static List<String> stringToListCapitalizeLetters(String data) {
        // Transform String to List and capitalize first letter
        return HelperClass.capitalizeLetters(Arrays.asList(data.split("-")));
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

    public static int checkIfMenuExists(ListViewOrderMenusAdapter adapterOrderMenus, Menu menu) {
        int menuExists = -1;

        // Loop all the menus of the ListAdapter
        for (int i = 0; i < adapterOrderMenus.getCount(); i++) {
            Menu actualMenu = adapterOrderMenus.getItem(i);
            Log.d(App.getContext().getString(R.string.HELPER_CLASS) + "Given menu", menu.toString());
            Log.d(App.getContext().getString(R.string.HELPER_CLASS) + "Actual menu", actualMenu.toString());

            // Check if both menus are equals
            // We can't use .equals() because the getMenuQuantity() will be different
            if (menu.getMenuName().equals(actualMenu.getMenuName()) &&
                    menu.getDishDrink().getId().equals(actualMenu.getDishDrink().getId()) &&
                    menu.getDishFirst().getId().equals(actualMenu.getDishFirst().getId()) &&
                    menu.getDishSecond().getId().equals(actualMenu.getDishSecond().getId()) &&
                    menu.getDishDessert().getId().equals(actualMenu.getDishDessert().getId())) {
                menuExists = i;
                break;
            }
        }
        return menuExists;
    }

    public static JSONArray dishesToJSON(ListAdapter listAdapter, String neededDishType) {
        JSONArray arrayAllDishesJSON = new JSONArray();

        // Loop the dishes of the Order ListView
        for (int i = 0; i < listAdapter.getCount(); i++) {
            Dish actualDish = (Dish) listAdapter.getItem(i);

            // Check if the dish is the neededDishType
            if (actualDish.getDishType().toLowerCase().equals(neededDishType)) {
                JSONObject dishPartsJSON = new JSONObject();

                // Get the ID and Quantity of the dish
                String dishId = actualDish.getId();
                String dishQuantity = String.valueOf(actualDish.getQuantity());
                try {
                    // Put both ID and Quantity into a single JSONObject
                    dishPartsJSON.put("name", dishId);
                    dishPartsJSON.put("quantity", dishQuantity);
                    Log.d(App.getContext().getString(R.string.HELPER_CLASS) + "Dish parts", dishPartsJSON.toString());

                    // And create a JSONArray with the JSONObject
                    arrayAllDishesJSON.put(dishPartsJSON);
                    Log.d(App.getContext().getString(R.string.HELPER_CLASS) + "Array all dishes", arrayAllDishesJSON.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        // Return the filled array
        return arrayAllDishesJSON;
    }

    public static JSONArray menusToJSON(ListAdapter listAdapter) {
        JSONArray arrayAllMenusJSON = new JSONArray();

        // Loop the dishes of the Order ListView
        for (int i = 0; i < listAdapter.getCount(); i++) {
            Menu actualMenu = (Menu) listAdapter.getItem(i);
            JSONObject menuPartsJSON = new JSONObject();

            // Get the name and Quantity of the menu
            String menuName = actualMenu.getMenuName();
            String menuQuantity = String.valueOf(actualMenu.getMenuQuantity());
            try {
                // Put the name name and quantity in the JSONObject
                menuPartsJSON.put("name", menuName);
                Log.d(App.getContext().getString(R.string.HELPER_CLASS) + "Menu parts", menuPartsJSON.toString());
                menuPartsJSON.put("quantity", menuQuantity);
                Log.d(App.getContext().getString(R.string.HELPER_CLASS) + "Menu parts", menuPartsJSON.toString());

                // Put the selected dishes of the menu
                menuPartsJSON.put(orderMenuDishTypes[0], new JSONObject().put("id", actualMenu.getDishDrink().getId()));
                Log.d(App.getContext().getString(R.string.HELPER_CLASS) + "Menu parts", menuPartsJSON.toString());

                menuPartsJSON.put(orderMenuDishTypes[1], new JSONObject().put("id", actualMenu.getDishFirst().getId()));
                Log.d(App.getContext().getString(R.string.HELPER_CLASS) + "Menu parts", menuPartsJSON.toString());

                menuPartsJSON.put(orderMenuDishTypes[2], new JSONObject().put("id", actualMenu.getDishSecond().getId()));
                Log.d(App.getContext().getString(R.string.HELPER_CLASS) + "Menu parts", menuPartsJSON.toString());

                menuPartsJSON.put(orderMenuDishTypes[3], new JSONObject().put("id", actualMenu.getDishDessert().getId()));
                Log.d(App.getContext().getString(R.string.HELPER_CLASS) + "Menu parts", menuPartsJSON.toString());

                // And create a JSONArray with the JSONObject
                arrayAllMenusJSON.put(menuPartsJSON);
                Log.d(App.getContext().getString(R.string.HELPER_CLASS) + "Array all menus", menuPartsJSON.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // Return the filled array
        return arrayAllMenusJSON;
    }

    public static JSONArray emptyJSON() {
        // Return an empty array
        return new JSONArray();
    }
}

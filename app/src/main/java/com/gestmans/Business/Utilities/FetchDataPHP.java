package com.gestmans.Business.Utilities;

import android.os.AsyncTask;
import android.util.Log;

import com.gestmans.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchDataPHP extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... type) {
        // Depending the sent string, it executes a certain method
        String data = "";
        switch (type[0]) {
            case "username_exist":
                data = loginCheck(0, type);
                break;
            case "username_password_matches":
                data = loginCheck(1, type);
                break;
            case "qr_code_exist":
                data = loginCheck(2, type);
                break;
            case "get_name_lastname":
                data = getNameLastname(type[1]);
                break;
            case "available_room_tables":
                data = roomTables(true);
                break;
            case "unavailable_room_tables":
                data = roomTables(false);
                break;
            case "get_dish_type":
                data = getDishType();
                break;
            case "get_dish_names":
            case "get_dishes_menu":
                data = getDishes(type[1], type[2]);
                break;
            case "get_menus":
                data = getMenus();
                break;
            case "send_order":
                data = processOrder(type[1], type[2]);
                break;
            case "receive_order":
                data = receiveOrder(type[1]);
                break;
            case "booking_alert":
                data = bookingAlert(type[1]);
                break;
        }
        Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS) + type[0], data);
        return data;
    }

    private String loginCheck(int type, String[] data) {
        String returningData;
        URL url = null;

        try {
            // Go to the URL of PHP file to check if the username exist, username and password matches or qr code exist
            switch (type) {
                case 0:
                    url = new URL("https://gestmans.000webhostapp.com/PHP/login/username_exists.php?user=" + data[1]);
                    break;
                case 1:
                    url = new URL("https://gestmans.000webhostapp.com/PHP/login/password_hash.php?user=" + data[1] + "&password=" + data[2]);
                    break;
                case 2:
                    url = new URL("https://gestmans.000webhostapp.com/PHP/login/qr_code_exists.php?qr=" + data[1]);
                    break;
            }
            returningData = getJSONWeb(url);

            // Format the JSON received to String
            returningData = formatJSONUniqueValueSuccess(returningData);
            Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS) + "Returned rows", returningData);
        } catch (IOException e) {
            e.printStackTrace();
            returningData = "-1";
        }
        return returningData;
    }

    private String getNameLastname(String data) {
        String returningData;
        URL url;

        try {
            // Go to the URL of PHP file to get the first name and last name of the given username
            url = new URL("https://gestmans.000webhostapp.com/PHP/login/get_name_lastname.php?string=" + data);
            returningData = getJSONWeb(url);

            // Format the JSON received to String and remove unnecessary characters
            returningData = formatJSONUniqueValueSuccess(returningData);
            returningData = returningData.replaceAll("[^A-Za-z\\s]", "");
            Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS) + "Name formatted", returningData);
        } catch (IOException e) {
            e.printStackTrace();
            returningData = "error";
        }
        return returningData;
    }

    private String roomTables(boolean available) {
        String returningData;
        URL url;

        try {
            // Go to the URL of PHP file to get the available / unavailable tables
            if (available) {
                url = new URL("https://gestmans.000webhostapp.com/PHP/app/tables/select_available_room_tables.php?table=roomTables");
            } else {
                url = new URL("https://gestmans.000webhostapp.com/PHP/app/tables/select_unavailable_room_tables.php?table=roomTables");
            }
            returningData = getJSONWeb(url);

            // Format the JSON to get a string of the different items, split by '-'
            JSONObject initialJO = new JSONObject(returningData);
            JSONArray outerArray = (JSONArray) initialJO.get("products");
            returningData = "";
            for (int i = 0; i < outerArray.length(); i++) {
                JSONArray innerArray = (JSONArray) outerArray.get(i);
                Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS) + "Inner JA", innerArray.toString());
                String numTable = (String) innerArray.get(0);
                Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS) + "Table number", numTable);
                returningData += numTable + "-";
            }

            // Erase the leftover '-'
            returningData = HelperClass.removeLastChar(returningData);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            returningData = "error";
        }
        return returningData;
    }

    private String getDishType() {
        String returningData;
        URL url;

        try {
            // Go to the URL of PHP file to get the multiple dish types
            url = new URL("https://gestmans.000webhostapp.com/PHP/app/dishes/get_dish_type.php");
            returningData = getJSONWeb(url);

            // Format the JSOn received to String
            returningData = formatJSONUniqueArray(returningData);

            // Personalized sort array
            returningData = HelperClass.sortArray(returningData.split("-"));

            // If there are menus, add it
            if (thereAreMenus()) {
                returningData += "menu";
            } else {
                // Erase the leftover '-'
                returningData = HelperClass.removeLastChar(returningData);
            }
        } catch (IOException e) {
            e.printStackTrace();
            returningData = "error";
        }
        return returningData;
    }

    private String getDishes(String typeSearch, String dishTypeOrMenu) {
        String returningData;
        URL url = null;

        try {
            switch (typeSearch) {
                case "no_menu":
                    // Go to the URL of PHP file to get Dishes of the sent dish type
                    url = new URL("https://gestmans.000webhostapp.com/PHP/app/dishes/get_dish_with_id.php?dish=" + dishTypeOrMenu);
                    break;
                case "menu":
                    // Go to the URL of PHP file to get Dishes of the sent menu
                    url = new URL("https://gestmans.000webhostapp.com/PHP/app/dishes/menu/get_id_name_with_menu_id.php?idmenu=" + dishTypeOrMenu);
                    break;
            }
            returningData = getJSONWeb(url);

            // Format the received JSON to String
            returningData = formatJSONDishesToString(returningData, typeSearch, dishTypeOrMenu);
        } catch (IOException e) {
            e.printStackTrace();
            returningData = "error";
        }
        return returningData;
    }

    private String getMenus() {
        String returningData;
        URL url;

        try {
            // Go to the URL of PHP file to get available menus
            url = new URL("https://gestmans.000webhostapp.com/PHP/app/dishes/menu/get_menu.php");
            returningData = getJSONWeb(url);

            // Format the JSON received to String
            returningData = formatJSONUniqueArray(returningData);
        } catch (IOException e) {
            e.printStackTrace();
            returningData = "error";
        }
        return returningData;
    }

    private String processOrder(String json, String type) {
        String returningData;
        URL url = null;

        try {
            switch (type) {
                case "new":
                    // Go to the URL of PHP file to send the order
                    url = new URL("https://gestmans.000webhostapp.com/PHP/app/order/new_order.php?json=" + json);
                    break;
                case "update":
                    // Go to the URL of PHP file to update the order
                    url = new URL("https://gestmans.000webhostapp.com/PHP/app/order/update_order.php?json=" + json);
                    break;
            }
            returningData = getJSONWeb(url);

            // Format the JSON received to String
            returningData = formatJSONUniqueValueSuccess(returningData);
        } catch (IOException e) {
            e.printStackTrace();
            returningData = "error";
        }
        return returningData;
    }

    private String receiveOrder(String table) {
        String returningData;
        URL url;

        try {
            // Go to the URL of PHP file to receive the order
            url = new URL("https://gestmans.000webhostapp.com/PHP/app/order/receive_order.php?table=" + table);
            returningData = getJSONWeb(url);

            // This time, the JSON is treated on the EditOrderSelectionFragment
        } catch (IOException e) {
            e.printStackTrace();
            returningData = "error";
        }
        return returningData;
    }

    private String bookingAlert(String numTable) {
        String returningData;
        URL url;

        try {
            // Go to the URL of PHP file to get the first name and last name of the given username
            url = new URL("https://gestmans.000webhostapp.com/PHP/app/booking/notify_booking.php?table=" + numTable);
            returningData = getJSONWeb(url);

            // Format the JSON received to String
            returningData = formatJSONUniqueValueSuccess(returningData);
        } catch (IOException e) {
            e.printStackTrace();
            returningData = "error";
        }
        return returningData;
    }

    private static String getJSONWeb(URL url) {
        HttpURLConnection connectionURL = null;
        InputStream is = null;
        BufferedReader br = null;
        String returningData;

        try {
            // Open link and get text sent by PHP (JSON format)
            Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS) + "Web URL", url.toString());
            connectionURL = (HttpURLConnection) url.openConnection();
            is = connectionURL.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            returningData = br.readLine();
            Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS) + "Web Data", returningData);
            return returningData;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (connectionURL != null) {
                connectionURL.disconnect();
            }
        }
    }

    private static String formatJSONUniqueValueSuccess(String data) {
        try {
            // Format the JSON to get the value of key success
            JSONObject initialJO = new JSONObject(data);
            Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS) + "Initial JO", initialJO.toString());
            data = String.valueOf(initialJO.get("success"));
        } catch (JSONException e) {
            e.printStackTrace();
            data = "error";
        }

        return data;
    }

    private static String formatJSONUniqueArray(String data) {
        try {
            // Format the JSON to get a string of the different items, split by '-'
            JSONObject initialJO = new JSONObject(data);

            Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS) + "Initial JO", initialJO.toString());
            data = "";
            JSONArray ja = (JSONArray) initialJO.get("success");
            for (int i = 0; i < ja.length(); i++) {
                String dishType = (String) ja.get(i);
                Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS) + "Dish type", dishType);
                data += dishType + "-";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            data = "error";
        }

        // Erase the leftover '-'
        data = HelperClass.removeLastChar(data);

        return data;
    }

    private static String formatJSONDishesToString(String data, String typeSearch, String dishType) {
        try {
            // Format the JSON to get a string of the different items, split by '-'
            JSONObject initialJO = new JSONObject(data);
            data = "";
            JSONArray initialJA = null;
            switch (typeSearch) {
                case "menu":
                    initialJA = initialJO.getJSONArray("menu");
                    break;
                case "no_menu":
                    initialJA = initialJO.getJSONArray(dishType);
                    break;
            }

            Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS) + "Initial JA", initialJA.toString());

            // Check if there are dishes in the menu
            if (initialJA.length() > 0) {
                for (int i = 0; i < initialJA.length(); i++) {
                    JSONArray dishArray = (JSONArray) initialJA.get(i);
                    Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS) + "Menu array", dishArray.toString());
                    for (int j = 0; j < dishArray.length(); j++) {
                        String partDish = String.valueOf(dishArray.get(j));
                        data += partDish + ",";
                    }
                    data = HelperClass.removeLastChar(data);
                    data += "-";
                }

                // Erase the leftover '-'
                data = HelperClass.removeLastChar(data);
            }

            // If not, send an empty string
            else {
                data = "empty";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            data = "error";
        }
        return data;
    }

    private static boolean thereAreMenus() {
        String returningData;
        URL url;
        boolean thereAreMenus = false;

        try {
            // Go to the URL of PHP file to get Dishes of the sent dish type
            url = new URL("https://gestmans.000webhostapp.com/PHP/app/dishes/menu/get_menu.php");
            Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS) + "Web URL", url.toString());
            returningData = getJSONWeb(url);

            // Format the JSON to get a string of the different items, split by '-'
            JSONObject initialJo = new JSONObject(returningData);
            JSONArray ja = (JSONArray) initialJo.get("success");
            if (ja.length() > 0) {
                thereAreMenus = true;
                Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS) + "Check menus", "There are menus");
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return thereAreMenus;
    }
}

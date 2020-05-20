package com.gestmans.Business;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
            case "get_dishes":
                data = getDishes(type[1]);
                break;
        }
        return data;
    }

    private String getDishes(String type) {
        String returningData;
        URL url;
        HttpURLConnection connectionURL = null;
        InputStream is = null;
        BufferedReader br = null;
        try {
            // Go to the URL of PHP file to get Dishes of the sent dish type
            url = new URL("https://gestmans.000webhostapp.com/PHP/get_menu.php?dish=" + type);

            // Open link and get text shown (JSON format)
            connectionURL = (HttpURLConnection) url.openConnection();
            is = connectionURL.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            returningData = br.readLine();
            Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS), returningData);

            // Format the JSON to get a string of the different items, split by '-'
            JSONObject initialJo = new JSONObject(returningData);
            returningData = "";
            JSONArray ja = (JSONArray) initialJo.get(type);
            Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS), ja.toString());
            for (int i = 0; i < ja.length(); i++) {
                String dish = (String) ja.get(i);
                Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS), dish);
                returningData += dish + "-";
            }

            // Erase the leftover '-'
            returningData = returningData.substring(0, returningData.length() - 1);
            Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS), returningData);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            returningData = "error";
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
        return returningData;
    }

    private String getDishType() {
        String returningData;
        URL url;
        HttpURLConnection connectionURL = null;
        InputStream is = null;
        BufferedReader br = null;
        try {
            // Go to the URL of PHP file to get the multiple dish types
            url = new URL("https://gestmans.000webhostapp.com/PHP/get_dish_type.php");

            // Open link and get text shown (JSON format)
            connectionURL = (HttpURLConnection) url.openConnection();
            is = connectionURL.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            returningData = br.readLine();
            Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS), returningData);

            // Format the JSON to get a string of the different items, split by '-'
            JSONObject initialJo = new JSONObject(returningData);
            Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS), initialJo.toString());
            returningData = "";
            JSONArray ja = (JSONArray) initialJo.get("success");
            for (int i = 0; i < ja.length(); i++) {
                String dishType = (String) ja.get(i);
                Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS), dishType);
                returningData += dishType + "-";
            }
            // Erase the leftover '-'
            returningData = returningData.substring(0, returningData.length() - 1);
            Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS), returningData);

            final String[] orderDishes = new String[]{"first", "second", "dessert", "drink", "menu"};
            final List<String> stringListCopy = Arrays.asList(returningData.split("-"));
            Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS), Arrays.toString(returningData.split("-")));
            ArrayList<String> sortedList = new ArrayList<>(stringListCopy);
            Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS), sortedList + " - before sorting");
            Collections.sort(sortedList, Comparator.comparing(s -> orderDishes[stringListCopy.indexOf(s)]));
            Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS), sortedList + " - after sorting");

            ArrayList<String> al = new ArrayList<>();
            for (int i = 0; i < returningData.split("-").length; i++) {
                for (int j = 0; j < returningData.split("-").length; j++) {
                    String data = returningData.split("-")[j];
                    String dataFormatted = data.substring(0, 1).toUpperCase() + data.substring(1).toLowerCase();
                    switch (i) {
                        case 0:
                            if (data.equals("first")) {
                                al.add(dataFormatted);
                            }
                            break;
                        case 1:
                            if (data.equals("second")) {
                                al.add(dataFormatted);
                            }
                            break;
                        case 2:
                            if (data.equals("dessert")) {
                                al.add(dataFormatted);
                            }
                            break;
                        case 3:
                            if (data.equals("drink")) {
                                al.add(dataFormatted);
                            }
                            break;
                        case 4:
                            if (data.equals("menu")) {
                                al.add(dataFormatted);
                            }
                            break;
                    }
                }
            }
            returningData = "";
            for (String element : al) {
                returningData += element + "-";
            }

            // Erase the leftover '-'
            returningData = returningData.substring(0, returningData.length() - 1);
            Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS), returningData);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            returningData = "error";
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
        return returningData;
    }

    private String getNameLastname(String data) {
        String returningData;
        URL url;
        HttpURLConnection connectionURL = null;
        InputStream is = null;
        BufferedReader br = null;
        try {
            // Go to the URL of PHP file to get the name and lastname of the given username
            url = new URL("https://gestmans.000webhostapp.com/PHP/get_name_lastname.php?string=" + data);

            // Open link and get text shown (JSON format)
            connectionURL = (HttpURLConnection) url.openConnection();
            is = connectionURL.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            returningData = br.readLine();
            Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS), returningData);

            // Format the JSON to get a string of the name and lastname, removing unnecessary characters
            JSONObject initialJO = new JSONObject(returningData);
            returningData = String.valueOf(initialJO.get("success"));
            returningData = returningData.replaceAll("[^A-Za-z\\s]", "");
            Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS), returningData);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            returningData = "error";
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
        return returningData;
    }

    private String loginCheck(int type, String[] data) {
        String returningData;
        URL url = null;
        HttpURLConnection connectionURL = null;
        InputStream is = null;
        BufferedReader br = null;
        try {
            // Go to the URL of PHP file to check if user exist, or user and password matches or qr code exist
            switch (type) {
                case 0:
                    url = new URL("https://gestmans.000webhostapp.com/PHP/username_exists.php?user=" + data[1]);
                    break;
                case 1:
                    url = new URL("https://gestmans.000webhostapp.com/PHP/username_password_matches.php?user=" + data[1] + "&password=" + data[2]);
                    break;
                case 2:
                    url = new URL("https://gestmans.000webhostapp.com/PHP/qr_code_exists.php?qr=" + data[1]);
                    break;
            }

            // Open link and get text shown (JSON format)
            connectionURL = (HttpURLConnection) url.openConnection();
            is = connectionURL.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            returningData = br.readLine();
            Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS), returningData);

            // Format the JSON to get a string of the received rows
            JSONObject initialJO = new JSONObject(returningData);
            int rowsSelected = (int) initialJO.get("success");
            returningData = String.valueOf(rowsSelected);
            Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS), returningData);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            returningData = "-1";
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
        return returningData;
    }

    private String roomTables(boolean available) {
        String returningData;
        URL url;
        HttpURLConnection connectionURL = null;
        InputStream is = null;
        BufferedReader br = null;
        try {
            // Go to the URL of PHP file to get the available / unavailable tables
            if (available) {
                url = new URL("https://gestmans.000webhostapp.com/PHP/select_available_room_tables.php?table=roomTables");
            } else {
                url = new URL("https://gestmans.000webhostapp.com/PHP/select_unavailable_room_tables.php?table=roomTables");
            }

            // Open link and get text shown (JSON format)
            connectionURL = (HttpURLConnection) url.openConnection();
            is = connectionURL.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            returningData = br.readLine();
            Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS), returningData);

            // Format the JSON to get a string of the different items, split by '-'
            JSONObject initialJO = new JSONObject(returningData);
            JSONArray outerArray = (JSONArray) initialJO.get("products");
            returningData = "";
            for (int i = 0; i < outerArray.length(); i++) {
                JSONArray innerArray = (JSONArray) outerArray.get(i);
                Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS), innerArray.toString());
                String numTable = (String) innerArray.get(0);
                Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS), numTable);
                returningData += numTable + "-";
            }

            // Erase the leftover '-'
            returningData = returningData.substring(0, returningData.length() - 1);
            Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS), returningData);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            returningData = "error";
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
        return returningData;
    }
}

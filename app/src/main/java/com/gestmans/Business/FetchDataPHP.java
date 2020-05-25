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
                data = getDishes(type[1], type[2]);
                break;
            case "get_dish_name_id":
                data = getDishes(type[1], type[2]);
                break;
            case "get_menus":
                data = getMenus();
                break;
        }
        return data;
    }

    private String getMenus() {
        String returningData;
        URL url;
        HttpURLConnection connectionURL = null;
        InputStream is = null;
        BufferedReader br = null;
        try {
            // Go to the URL of PHP file to get Dishes of the sent dish type
            url = new URL("https://gestmans.000webhostapp.com/PHP/get_menu.php");

            // Open link and get text shown (JSON format)
            connectionURL = (HttpURLConnection) url.openConnection();
            is = connectionURL.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            returningData = br.readLine();
            Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS), returningData);

            // Format the JSON to get a string of the different items, split by '-'
            JSONObject initialJo = new JSONObject(returningData);
            returningData = "";
            JSONArray ja = (JSONArray) initialJo.get("success");
            Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS), ja.toString());
            for (int i = 0; i < ja.length(); i++) {
                String dish = (String) ja.get(i);
                Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS), dish);
                returningData += dish + "-";
            }

            // Erase the leftover '-'
            returningData = HelperClass.removeLastChar(returningData);
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

    private String getDishes(String dishType, String returningType) {
        String returningData;
        URL url = null;
        HttpURLConnection connectionURL = null;
        InputStream is = null;
        BufferedReader br = null;
        try {
            // Go to the URL of PHP file to get Dishes of the sent dish type
            switch (returningType) {
                case "name":
                    url = new URL("https://gestmans.000webhostapp.com/PHP/get_dish_name.php?dish=" + dishType);
                    break;
                case "name_id":
                    url = new URL("https://gestmans.000webhostapp.com/PHP/get_dish_with_id.php?dish=" + dishType);
                    break;
            }

            // Open link and get text shown (JSON format)
            connectionURL = (HttpURLConnection) url.openConnection();
            is = connectionURL.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            returningData = br.readLine();
            Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS), returningData);

            // Format the JSON to get a string of the different items, split by '-'
            JSONObject initialJo = new JSONObject(returningData);
            returningData = "";
            JSONArray ja = (JSONArray) initialJo.get(dishType);
            Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS), ja.toString());
            switch (returningType) {
                case "name":
                    for (int i = 0; i < ja.length(); i++) {
                        String dish = (String) ja.get(i);
                        Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS), dish);
                        returningData += dish + "-";
                    }
                    break;
                case "name_id":
                    for (int i = 0; i < ja.length(); i++) {
                        JSONArray dishArray = (JSONArray) ja.get(i);
                        Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS), dishArray.toString());
                        for (int j = 0; j < dishArray.length(); j++) {
                            String partDish = String.valueOf(dishArray.get(j));
                            returningData += partDish + ",";
                        }
                        returningData = HelperClass.removeLastChar(returningData);
                        returningData += "-";
                    }
                    break;
            }

            // Erase the leftover '-'
            returningData = HelperClass.removeLastChar(returningData);
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
            returningData = HelperClass.removeLastChar(returningData);

            // Personalized sort array
            returningData = HelperClass.sortArray(returningData.split("-"));

            //If there are menus, add it
            if (thereAreMenus()) {
                returningData += "menu";
            } else {
                // Erase the leftover '-'
                returningData = HelperClass.removeLastChar(returningData);
            }
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
            returningData = HelperClass.removeLastChar(returningData);
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

    private static boolean thereAreMenus() {
        String returningData;
        URL url;
        HttpURLConnection connectionURL = null;
        InputStream is = null;
        BufferedReader br = null;
        boolean thereAreMenus = false;
        try {
            // Go to the URL of PHP file to get Dishes of the sent dish type
            url = new URL("https://gestmans.000webhostapp.com/PHP/get_menu.php");

            // Open link and get text shown (JSON format)
            connectionURL = (HttpURLConnection) url.openConnection();
            is = connectionURL.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            returningData = br.readLine();
            Log.d(App.getContext().getString(R.string.FETCH_PHP_CLASS), returningData);

            // Format the JSON to get a string of the different items, split by '-'
            JSONObject initialJo = new JSONObject(returningData);
            returningData = "";
            JSONArray ja = (JSONArray) initialJo.get("success");
            if (ja.length() > 0) {
                thereAreMenus = true;
            }
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
        return thereAreMenus;
    }
}

package com.gestmans.Business;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class fetchDataPHP extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... type) {
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
        }
        return data;
    }

    private String getDishType() {
        String returningData;
        URL url = null;
        HttpURLConnection connectionURL = null;
        InputStream is = null;
        BufferedReader br = null;
        try {
            url = new URL("http://192.168.1.33:8080/Programa/Programas/get_dish_type.php");
            connectionURL = (HttpURLConnection) url.openConnection();
            is = connectionURL.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            returningData = br.readLine();
            Log.d(/*Resources.getSystem().getString(R.string.FETCH_PHP_CLASS)*/"PHP", returningData);
            JSONObject initialJo = new JSONObject(returningData);
            Log.d(/*Resources.getSystem().getString(R.string.FETCH_PHP_CLASS)*/"PHP", initialJo.toString());
            returningData = "";
            JSONArray ja = (JSONArray) initialJo.get("success");
            for (int i = 0; i < ja.length(); i++) {
                String dishType = (String) ja.get(i);
                Log.d(/*Resources.getSystem().getString(R.string.FETCH_PHP_CLASS)*/"PHP", dishType);
                returningData += dishType + "-";
            }
            returningData = returningData.substring(0, returningData.length() - 1);
            Log.d(/*Resources.getSystem().getString(R.string.FETCH_PHP_CLASS)*/"PHP", returningData);
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
        URL url = null;
        HttpURLConnection connectionURL = null;
        InputStream is = null;
        BufferedReader br = null;
        try {
            url = new URL("http://192.168.1.33:8080/Programa/Programas/get_name_lastname.php?string=" + data);
            connectionURL = (HttpURLConnection) url.openConnection();
            is = connectionURL.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            returningData = br.readLine();
            Log.d(/*Resources.getSystem().getString(R.string.FETCH_PHP_CLASS)*/"PHP", returningData);
            JSONObject initialJO = new JSONObject(returningData);
            returningData = String.valueOf(initialJO.get("success"));
            returningData = returningData.replaceAll("[^A-Za-z\\s]","");
            Log.d(/*Resources.getSystem().getString(R.string.FETCH_PHP_CLASS)*/"PHP", returningData);
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
            switch (type) {
                case 0:
                    url = new URL("http://192.168.1.33:8080/Programa/Programas/username_exists.php?user=" + data[1]);
                    break;
                case 1:
                    url = new URL("http://192.168.1.33:8080/Programa/Programas/username_password_matches.php?user=" + data[1] + "&password=" + data[2]);
                    break;
                case 2:
                    url = new URL("http://192.168.1.33:8080/Programa/Programas/qr_code_exists.php?qr=" + data[1]);
                    break;
            }
            connectionURL = (HttpURLConnection) url.openConnection();
            is = connectionURL.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            returningData = br.readLine();
            Log.d(/*Resources.getSystem().getString(R.string.FETCH_PHP_CLASS)*/"PHP", returningData);
            JSONObject initialJO = new JSONObject(returningData);
            int rowsSelected = (int) initialJO.get("success");
            returningData = String.valueOf(rowsSelected);
            Log.d(/*Resources.getSystem().getString(R.string.FETCH_PHP_CLASS)*/"PHP", returningData);
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

    private String roomTables(boolean available) {
        String returningData;
        URL url;
        HttpURLConnection connectionURL = null;
        InputStream is = null;
        BufferedReader br = null;
        try {
            if (available) {
                url = new URL("http://192.168.1.33:8080/Programa/Programas/select_available_room_tables.php");
            } else {
                url = new URL("http://192.168.1.33:8080/Programa/Programas/select_unavailable_room_tables.php");
            }
            connectionURL = (HttpURLConnection) url.openConnection();
            is = connectionURL.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            returningData = br.readLine();
            Log.d(/*Resources.getSystem().getString(R.string.FETCH_PHP_CLASS)*/"PHP", returningData);
            JSONObject initialJO = new JSONObject(returningData);
            JSONArray outerArray = (JSONArray) initialJO.get("products");
            returningData = "";
            for (int i = 0; i < outerArray.length(); i++) {
                JSONArray innerArray = (JSONArray) outerArray.get(i);
                Log.d(/*Resources.getSystem().getString(R.string.FETCH_PHP_CLASS)*/"PHP", innerArray.toString());
                String numTable = (String) innerArray.get(0);
                Log.d(/*Resources.getSystem().getString(R.string.FETCH_PHP_CLASS)*/"PHP", numTable);
                returningData += numTable + "-";
            }
            returningData = returningData.substring(0, returningData.length() - 1);
            Log.d(/*Resources.getSystem().getString(R.string.FETCH_PHP_CLASS)*/"PHP", returningData);
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

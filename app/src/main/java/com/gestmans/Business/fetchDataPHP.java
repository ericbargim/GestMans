package com.gestmans.Business;

import android.os.AsyncTask;
import android.util.Log;

import com.gestmans.Interface.Activities.AppMainActivity;
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

public class fetchDataPHP extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... type) {
        String data = "";
        switch (type[0]) {
            case "available_room_tables":
                data = roomTables(data, true);
                break;
            case "unavailable_room_tables":
                data= roomTables(data, false);
                break;
        }
        return data;
    }

    private String roomTables(String data, boolean available) {
        try {
            URL url;
            if (available) {
                url = new URL("http://192.168.1.33:8080/Programa/Programas/select_available_room_tables.php");
            } else {
                url = new URL("http://192.168.1.33:8080/Programa/Programas/select_unavailable_room_tables.php");
            }
            HttpURLConnection connectionURL = (HttpURLConnection) url.openConnection();
            InputStream is = connectionURL.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            data = br.readLine();
            Log.d("FETCH_PHP_CLASS", data);
            JSONObject initialJO = new JSONObject(data);
            JSONArray outerArray = (JSONArray) initialJO.get("products");
            data = "";
            for (int i = 0; i < outerArray.length(); i++) {
                JSONArray innerArray = (JSONArray) outerArray.get(i);
                Log.d("FETCH_PHP_CLASS", innerArray.toString());
                String numTable = (String) innerArray.get(0);
                Log.d("FETCH_PHP_CLASS", numTable);
                data += numTable + "-";
            }
            data = data.substring(0, data.length() - 1);
            Log.d("FETCH_PHP_CLASS", data);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            data = "error";
        }
        return data;
    }
}

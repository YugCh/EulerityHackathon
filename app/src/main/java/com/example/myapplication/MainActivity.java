package com.example.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private final static String APP_ID = "yrc247@stern.nyu.edu";

    private final static String GetURL = "https://eulerity-hackathon.appspot.com/fonts/all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner fontSpinner = findViewById(R.id.fontSpinner);

        new GetArrayTask(fontSpinner).execute();
    }

    private class GetArrayTask extends AsyncTask<Void, Void, JSONArray> {
        private Spinner fontSpinner;

        public GetArrayTask(Spinner fontSpinner) {
            this.fontSpinner = fontSpinner;
        }

        @Override
        protected JSONArray doInBackground(Void... arg0) {
            JSONArray fonts = new JSONArray();
            try {
                URL url = new URL(GetURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();

                String inputString;
                while ((inputString = bufferedReader.readLine()) != null) {
                    builder.append(inputString);
                }

                fonts = new JSONArray(builder.toString());

                urlConnection.disconnect();
                return fonts;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return fonts;
        }

        @Override
        protected void onPostExecute(JSONArray fonts) {

            try{
                fontSpinner = findViewById(R.id.fontSpinner);
                List<String> list = new ArrayList<>();

                for(int i = 0; i < fonts.length(); i++) {
                    JSONObject font = fonts.getJSONObject(i);
                    String fontName = font.getString("family");
                    list.add(fontName);
                }

                LinkedHashSet<String> hashSet = new LinkedHashSet<>(list);
                ArrayList<String> newList = new ArrayList<>(hashSet);

                final ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, newList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                fontSpinner.setAdapter(dataAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
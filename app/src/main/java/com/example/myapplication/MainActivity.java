package com.example.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private final static String APP_ID = "yrc247@stern.nyu.edu";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String GetURL = "https://eulerity-hackathon.appspot.com/fonts/all";

        TextView textView = findViewById(R.id.fontPrompt);

        new GetArrayTask(textView).execute(GetURL);
    }

    private class GetArrayTask extends AsyncTask<String, Void, String> {
        private TextView textView;
        private Spinner fontSpinner;

        public GetArrayTask(TextView textView) {
            this.textView = textView;
        }

        @Override
        protected String doInBackground(String... strings) {
            String weather = "undefined";
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();

                String inputString;
                while ((inputString = bufferedReader.readLine()) != null) {
                    builder.append(inputString);
                }
                //JSONObject topLevel = new JSONObject(builder.toString());
                //JSONObject main = topLevel.getJSONObject("fonts");
                //weather = String.valueOf(main.getString("family"));

                fontSpinner = (Spinner) findViewById(R.id.fontSpinner);
                List<String> list = new ArrayList<String>();

                JSONArray fonts = new JSONArray(builder.toString());
                for(int i = 0; i < fonts.length(); i++) {
                    JSONObject font = fonts.getJSONObject(i);
                    list.add(font.getString("family"));
                }
                final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fontSpinner.setAdapter(dataAdapter);
                    }
                });
                urlConnection.disconnect();
                return weather;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return weather;
        }

        @Override
        protected void onPostExecute(String temp) {
            textView.setText("Current weather: " + temp);
        }
    }
}
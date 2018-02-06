package com.akshay.anfapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final int WIFI_SETTINGS = 0;
    RecyclerAdapter adapter;
    private String BASE_URL = "https://www.abercrombie.com/anf/nativeapp/qa/codetest/codeTest_exploreData.json";
    private List<AFResponseBean> responseBeanList;
    private RecyclerView recyclerView;
    private LinearLayout noInternetFoundContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_activity);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        noInternetFoundContainer = (LinearLayout) findViewById(R.id.no_internet_found_container);

        responseBeanList = new ArrayList<>();

        if (NetworkUtils.isNetworkAvailable(this)) {
            executeServiceCall();
        } else {
            recyclerView.setVisibility(View.GONE);
            noInternetFoundContainer.setVisibility(View.VISIBLE);
            showNetworkNotAvailableDialog();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == WIFI_SETTINGS) {
            if (NetworkUtils.isNetworkAvailable(this)) {
                recyclerView.setVisibility(View.VISIBLE);
                noInternetFoundContainer.setVisibility(View.GONE);
                executeServiceCall();
            }
        }
    }

    private void executeServiceCall() {
        new GetPromotionsAsyncTask().execute(BASE_URL);
    }

    private void showNetworkNotAvailableDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Warning");
        alertDialog.setMessage("Internet Not Available");
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "SETTINGS",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        showAndroidWIFISettings();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });
        alertDialog.show();
    }

    private void showAndroidWIFISettings() {
        startActivityForResult(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS), WIFI_SETTINGS);
    }

    private String readStream(InputStream inputStream) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

    public class GetPromotionsAsyncTask extends AsyncTask<String, Void, String> {
        String jsonResponse;

        @Override
        protected String doInBackground(String... strings) {

            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                int responseCode = urlConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    jsonResponse = readStream(urlConnection.getInputStream());
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e("Json Response", "" + jsonResponse);
            Gson gson = new Gson(); //GSON for Serializing and De-serializing

            responseBeanList = gson.fromJson(jsonResponse, new TypeToken<List<AFResponseBean>>() {
            }.getType());

            adapter = new RecyclerAdapter(MainActivity.this, responseBeanList);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
            recyclerView.setLayoutManager(linearLayoutManager);

            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);


        }
    }

}

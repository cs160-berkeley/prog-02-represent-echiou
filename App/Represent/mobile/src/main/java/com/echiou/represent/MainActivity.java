package com.echiou.represent;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Wearable;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "u7QF6HENk6ME6YaZDN5BnLlRl";
    private static final String TWITTER_SECRET = "6s4LlsZLgQd7XicJvemjAyQDvoQmx6YCArMrsfF0HH25YMNlh8";

    public final static String EXTRA_MESSAGE = "com.echiou.represent.MESSAGE";
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private InputStream input;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Wearable.API)  // used for data layer API
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /** Called when the user clicks the Use Current Location button */
    public void changeViewLocation(View view) {
        Intent intent = new Intent(this, CongressionalViewActivity.class);
        String mLatitude = "";
        String mLongitude = "";
        String county = "";
        String state = "";

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitude = String.valueOf(mLastLocation.getLatitude());
            mLongitude = String.valueOf(mLastLocation.getLongitude());
        } else {
            Log.e("Location", "Not found");
        }

        Log.d("Sending to CVActivity", "latitude=" + mLatitude + "&longitude=" + mLongitude + "&apikey=9adaa4743c4c4c76815a0c92f82934d1");

        intent.putExtra(EXTRA_MESSAGE, "latitude=" + mLatitude + "&longitude=" + mLongitude + "&apikey=9adaa4743c4c4c76815a0c92f82934d1");

        try {
            // Getting current location
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

            String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + mLatitude + "," + mLongitude + "&key=AIzaSyBdKluJeAem24eNtB7VZzBLWnOZ6ekXGzA";
            input = new URL(url).openStream();

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }
            JSONObject input_json = new JSONObject(responseStrBuilder.toString());
            JSONArray jsonArray = input_json.optJSONArray("results");

            JSONArray address_components = jsonArray.getJSONObject(0).getJSONArray("address_components");
            county = county_finder(address_components);
            state = state_finder(address_components);
        } catch(Exception e) {
            Log.e("Error in reverse Geolookup", e.toString());
        }

        intent.putExtra("LOCATION", county);
        intent.putExtra("STATE", state);
        startActivity(intent);
    }

    /** Called when the user clicks the Use Zip button */
    public void changeViewZip(View view) {
        Intent intent = new Intent(this, CongressionalViewActivity.class);
        EditText editText = (EditText) findViewById(R.id.zip_input);
        String mLatitude = "";
        String mLongitude = "";
        String county = "";
        String state = "";

        Log.d("Sending to CVActivity", "zip=" + editText.getText().toString() + "&apikey=9adaa4743c4c4c76815a0c92f82934d1");

        String message = "zip=" + editText.getText().toString() + "&apikey=9adaa4743c4c4c76815a0c92f82934d1";
        intent.putExtra(EXTRA_MESSAGE, message);

        try {
            // Getting current location
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

            String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + editText.getText().toString() + "&region=us&key=AIzaSyBdKluJeAem24eNtB7VZzBLWnOZ6ekXGzA";
            input = new URL(url).openStream();

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }
            JSONObject input_json = new JSONObject(responseStrBuilder.toString());
            JSONArray jsonArray = input_json.optJSONArray("results");
            Log.d("JSONARRAY", jsonArray.toString());

            mLatitude = jsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lat");
            mLongitude = jsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lng");

            String url2 = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + mLatitude + "," + mLongitude + "&key=AIzaSyBdKluJeAem24eNtB7VZzBLWnOZ6ekXGzA";
            input = new URL(url2).openStream();

            BufferedReader streamReader2 = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            StringBuilder responseStrBuilder2 = new StringBuilder();

            String inputStr2;
            while ((inputStr2 = streamReader2.readLine()) != null) {
                responseStrBuilder2.append(inputStr2);
            }
            JSONObject input_json2 = new JSONObject(responseStrBuilder2.toString());
            JSONArray jsonArray2 = input_json2.optJSONArray("results");

            JSONArray address_components = jsonArray2.getJSONObject(0).getJSONArray("address_components");
            county = county_finder(address_components);
            state = state_finder(address_components);
        } catch(Exception e) {
            Log.e("Error in reverse Geolookup", e.toString());
        }

        intent.putExtra("LOCATION", county);
        intent.putExtra("STATE", state);
        startActivity(intent);
    }

    public String county_finder(JSONArray address_components) {
        String county = "";
        for(int i = 0; i < address_components.length(); i++) {
            try {
                if (address_components.getJSONObject(i).getJSONArray("types").getString(0).contentEquals("administrative_area_level_2")) {
                    county = address_components.getJSONObject(i).getString("long_name");
                    break;
                }
            } catch (Exception e) {
                Log.e("Error in getting county", e.toString());
            }
        }
        return county;
    }

    public String state_finder(JSONArray address_components) {
        String state = "";
        for(int i = 0; i < address_components.length(); i++) {
            try {
                if (address_components.getJSONObject(i).getJSONArray("types").getString(0).contentEquals("administrative_area_level_1")) {
                    state = address_components.getJSONObject(i).getString("short_name");
                    break;
                }
            } catch (Exception e) {
                Log.e("Error in getting county", e.toString());
            }
        }
        return state;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

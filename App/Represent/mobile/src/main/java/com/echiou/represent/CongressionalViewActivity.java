package com.echiou.represent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import io.fabric.sdk.android.Fabric;


public class CongressionalViewActivity extends Activity {
    public final static String PICTURE_MESSAGE = "com.echiou.represent.PICTURE_MESSAGE";
    public final static String NAME_MESSAGE = "com.echiou.represent.NAME_MESSAGE";
    public final static String DIST_MESSAGE = "com.echiou.represent.DIST_MESSAGE";
    public final static String PARTY_MESSAGE = "com.echiou.represent.PARTY_MESSAGE";
    public final static String EMAIL_MESSAGE = "com.echiou.represent.EMAIL_MESSAGE";
    public final static String PHONE_MESSAGE = "com.echiou.represent.PHONE_MESSAGE";
    public final static String TERM_MESSAGE = "com.echiou.represent.TERM_MESSAGE";
    public final static String COMMITTEE_MESSAGE = "com.echiou.represent.COMMITTEE_MESSAGE";
    public final static String BILLS_MESSAGE = "com.echiou.represent.BILLS_MESSAGE";
    public final static String HANDLES_MESSAGE = "com.echiou.represent.HANDLES_MESSAGE";
    public final static String TWITTER_KEY = "u7QF6HENk6ME6YaZDN5BnLlRl";
    public final static String TWITTER_SECRET = "6s4LlsZLgQd7XicJvemjAyQDvoQmx6YCArMrsfF0HH25YMNlh8";

    private InputStream input;

    TwitterApiClient twitterApiClient;
    StatusesService statusesService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional_view);
        final ListView repList = (ListView)findViewById(R.id.replistview);
        final ListView senList = (ListView)findViewById(R.id.senlistview);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView location = (TextView) findViewById(R.id.header);
        String county = intent.getStringExtra("LOCATION");
        String state = intent.getStringExtra("STATE");
        location.setText(county);

        String[] repNames = new String[2];
        String[] repParties = new String[2];
        String[] repEmails = new String[2];
        String[] repPhoneNumbers = new String[2];
        String[] repTweets = new String[2];
        String[] repHandles = new String[2];
        String[] repDist = new String[2];
        String[] repBio = new String[2];
        String[] repSite = new String[2];

        String[] senNames = new String[2];
        String[] senParties = new String[2];
        String[] senEmails = new String[2];
        String[] senPhoneNumbers = new String[2];
        String[] senTweets = new String[2];
        String[] senHandles = new String[2];
        String[] senDist = new String[2];
        String[] senBio = new String[2];
        String[] senSite = new String[2];


        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

            String url = "http://congress.api.sunlightfoundation.com/legislators/locate?" + message;
            input = new URL(url).openStream();

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }
            JSONObject input_json = new JSONObject(responseStrBuilder.toString());
            JSONArray jsonArray = input_json.optJSONArray("results");

            if (jsonArray.length() == 3) {
                repNames = new String[1];
                repParties = new String[1];
                repEmails = new String[1];
                repPhoneNumbers = new String[1];
                repTweets = new String[1];
                repHandles = new String[1];
                repDist = new String[1];
                repBio = new String[1];
                repSite = new String[1];

                for (int i=0;i<jsonArray.length();i++){
                    if(i == 0) { // the only rep
                        JSONObject rep = jsonArray.getJSONObject(i);
                        repNames[i] = rep.get("first_name").toString() + " "
                                + rep.get("last_name").toString();
                        repParties[i] = "Political Party: " + rep.get("party").toString();
                        repEmails[i] = rep.get("oc_email").toString();
                        repPhoneNumbers[i] = rep.get("phone").toString();
                        repTweets[i] ="(Rep's first tweet)"; // TODO: Fix to use tweet
                        repHandles[i] = rep.get("twitter_id").toString();
                        repDist[i] = rep.get("state").toString() + rep.get("district").toString();
                        repBio[i] = rep.get("bioguide_id").toString();
                        repSite[i] = rep.get("website").toString();
                    }
                    else { // senator
                        JSONObject sen = jsonArray.getJSONObject(i);
                        senNames[i - 1] = sen.get("first_name").toString() + " "
                                + sen.get("last_name").toString();
                        senParties[i - 1] = "Political Party: " + sen.get("party").toString();
                        senEmails[i - 1] = sen.get("oc_email").toString();
                        senPhoneNumbers[i - 1] = sen.get("phone").toString();
                        senTweets[i - 1] ="(Sen's first tweet)"; // TODO: Fix to use tweet
                        senHandles[i - 1] = sen.get("twitter_id").toString();
                        senDist[i - 1] = sen.get("state").toString();
                        senBio[i - 1] = sen.get("bioguide_id").toString();
                        senSite[i - 1] = sen.get("website").toString();
                    }
                }
            } else { // else it is length 4 (2 reps, 2 sens)
                for (int i=0;i<jsonArray.length();i++){
                    if(i <= 1) { // the only rep
                        JSONObject rep = jsonArray.getJSONObject(i);
                        repNames[i] = rep.get("first_name").toString() + " "
                                + rep.get("last_name").toString();
                        repParties[i] = "Political Party: " + rep.get("party").toString();
                        repEmails[i] = rep.get("oc_email").toString();
                        repPhoneNumbers[i] = rep.get("phone").toString();
                        repTweets[i] ="(Rep's first tweet)"; // TODO: Fix to use tweet
                        repHandles[i] = rep.get("twitter_id").toString();
                        repDist[i] = rep.get("state").toString() + rep.get("district").toString();
                        repBio[i] = rep.get("bioguide_id").toString();
                        repSite[i] = rep.get("website").toString();
                    }
                    else { // senator
                        JSONObject sen = jsonArray.getJSONObject(i);
                        senNames[i - 2] = sen.get("first_name").toString() + " "
                                + sen.get("last_name").toString();
                        senParties[i - 2] = "Political Party: " + sen.get("party").toString();
                        senEmails[i - 2] = sen.get("oc_email").toString();
                        senPhoneNumbers[i - 2] = sen.get("phone").toString();
                        senTweets[i - 2] ="(Sen's first tweet)"; // TODO: Fix to use tweet
                        senHandles[i - 2] = sen.get("twitter_id").toString();
                        senDist[i - 2] = sen.get("state").toString();
                        senBio[i - 2] = sen.get("bioguide_id").toString();
                        senSite[i - 2] = sen.get("website").toString();
                    }
                }
            }
        } catch(Exception e){
            Log.e("Error in getting information", e.toString());
        }

        Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
        sendIntent.putExtra("ACTIVITY_TYPE", "/rep");

        //OvR
        String[] OvR = {"0", "0", county + ", " + state};
        try {
            InputStream stream = getAssets().open("election-county-2012.json");
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String jsonString = new String(buffer, "UTF-8");

            JSONArray jsonArray = new JSONArray(jsonString);
            String truncate_county = county.substring(0, county.length() - 7);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if(jsonObject.getString("county-name").contentEquals(truncate_county)) {
                    OvR[0] = jsonObject.getString("obama-percentage");
                    OvR[1] = jsonObject.getString("romney-percentage");
                }
            }
        } catch (Exception e) {
            Log.e("Error reading election file", e.toString());
        }


        sendIntent.putExtra("NAME_AND_PARTY_MESSAGE", concat(repNames, concat(senNames, concat(repParties, concat(senParties, OvR)))));
        startService(sendIntent);


        final RepArrayAdapter repAdapter = new RepArrayAdapter(this, repNames, repParties, repEmails, repPhoneNumbers, repTweets, repHandles, repBio, repSite);
        final RepArrayAdapter senAdapter = new RepArrayAdapter(this, senNames, senParties, senEmails, senPhoneNumbers, senTweets, senHandles, senBio, senSite);
        repList.setAdapter(repAdapter);
        senList.setAdapter(senAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_congressional_view, menu);
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

    public String[] concat(String[] a, String[] b) {
        int aLen = a.length;
        int bLen = b.length;
        String[] c = new String[aLen+bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }
}

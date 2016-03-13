package com.echiou.represent;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created by ethan on 3/2/16.
 */
public class PhoneListenerService extends WearableListenerService {

    //   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
    private static final String CONGRESS = "/refresh_congress";
    private static final String SHOW_DETAILED = "/show_detailed";
    public final static String EXTRA_MESSAGE = "com.echiou.represent.MESSAGE";
    final Random rand = new Random();
    private InputStream input;

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());
        String mLatitude = "";
        String mLongitude = "";
        String county = "";
        String state = "";
        if( messageEvent.getPath().equalsIgnoreCase(CONGRESS) ) {
            // We don't care about the message's actual data - we are just going to pick a random district now.
            try {
                InputStream stream = getAssets().open("zips.json");
                int size = stream.available();
                byte[] buffer = new byte[size];
                stream.read(buffer);
                stream.close();
                String jsonString = new String(buffer, "UTF-8");

                JSONArray jsonArray = new JSONArray(jsonString);

                int index = rand.nextInt(jsonArray.length());
                JSONArray latlng = jsonArray.getJSONObject(index).getJSONArray("loc");
                mLatitude = String.valueOf(latlng.getDouble(1));
                mLongitude = String.valueOf(latlng.getDouble(0));
            } catch (Exception e) {
                Log.e("Error finding random location", e.toString());
            }
            Log.d("lat", mLatitude);
            Log.d("long", mLongitude);

            try {
                // Getting current location
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

            Intent intent = new Intent(this, CongressionalViewActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(EXTRA_MESSAGE, "latitude=" + mLatitude + "&longitude=" + mLongitude + "&apikey=9adaa4743c4c4c76815a0c92f82934d1");
            intent.putExtra("LOCATION", county);
            intent.putExtra("STATE", state);
            startActivity(intent);
        } else if( messageEvent.getPath().equalsIgnoreCase(SHOW_DETAILED) ) {
            try {
                String senName = new String(messageEvent.getData(), "UTF-8");

                Context mContext = getBaseContext();

                Intent intent = new Intent(mContext, DetailedViewActivity.class);
                String picture_message = "";
                String name_message = senName;
                String dist_message = "";
                String party_message = "";
                String email_message = "";
                String phone_message = "";
                String term_message = "";
                String bios = "";
                InputStream input;

                try {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                    StrictMode.setThreadPolicy(policy);

                    String[] nameArray = senName.split("\\s+");

                    String url = "https://congress.api.sunlightfoundation.com/legislators?first_name=" + nameArray[0] + "&last_name=" + nameArray[1] + "&apikey=9adaa4743c4c4c76815a0c92f82934d1";
                    input = new URL(url).openStream();

                    BufferedReader streamReader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
                    StringBuilder responseStrBuilder = new StringBuilder();

                    String inputStr;
                    while ((inputStr = streamReader.readLine()) != null) {
                        responseStrBuilder.append(inputStr);
                    }
                    JSONObject input_json = new JSONObject(responseStrBuilder.toString());
                    JSONObject rep_object = input_json.optJSONArray("results").getJSONObject(0);

                    if (rep_object.getString("title").contentEquals("Sen")) {
                        dist_message = "Senator for " + rep_object.getString("state");
                    } else {
                        dist_message = "Representative for " + rep_object.getString("state") + rep_object.getString("district");
                    }
                    if (rep_object.getString("party").contentEquals("D")) {
                        party_message = "Democrat";
                    } else {
                        party_message = "Republican";
                    }
                    email_message = rep_object.getString("oc_email");
                    phone_message = rep_object.getString("phone");
                    term_message = "Term ends on: " + rep_object.getString("term_end");
                    picture_message = rep_object.getString("twitter_id");
                    bios = rep_object.getString("bioguide_id");

                } catch(Exception e){
                    Log.e("Error in getting information", e.toString());
                }

                String[] committee_message = new String[2];
                try {
                    String url = "https://congress.api.sunlightfoundation.com/committees?member_ids=" + bios + "&apikey=9adaa4743c4c4c76815a0c92f82934d1";
                    input = new URL(url).openStream();

                    BufferedReader streamReader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
                    StringBuilder responseStrBuilder = new StringBuilder();

                    String inputStr;
                    while ((inputStr = streamReader.readLine()) != null) {
                        responseStrBuilder.append(inputStr);
                    }
                    JSONObject input_json = new JSONObject(responseStrBuilder.toString());
                    JSONArray committees_array = input_json.optJSONArray("results");

                    if (committees_array != null) {
                        for (int i = 0; i < committees_array.length() && i < committee_message.length; i++) {
                            committee_message[i] = committees_array.getJSONObject(i).getString("committee_id") + ": "
                                    + committees_array.getJSONObject(i).getString("name");
                        }
                    } else {
                        committee_message[0] = "No committees found.";
                    }
                } catch(Exception e){
                    Log.e("Error in getting bill information", e.toString());
                }



                String[] bills_message = new String[2];
                try {
                    String url = "https://congress.api.sunlightfoundation.com/bills?sponsor_id=" + bios + "&apikey=9adaa4743c4c4c76815a0c92f82934d1";
                    input = new URL(url).openStream();

                    BufferedReader streamReader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
                    StringBuilder responseStrBuilder = new StringBuilder();

                    String inputStr;
                    while ((inputStr = streamReader.readLine()) != null) {
                        responseStrBuilder.append(inputStr);
                    }
                    JSONObject input_json = new JSONObject(responseStrBuilder.toString());
                    JSONArray bills_array = input_json.optJSONArray("results");

                    if(bills_array != null) {
                        for (int i = 0; i < bills_message.length && i < bills_array.length(); i++) {
                            bills_message[i] = bills_array.getJSONObject(i).getString("bill_id") + ":\n"
                                    + bills_array.getJSONObject(i).getString("official_title");
                        }
                    } else {
                        bills_message[0] = "No committees found.";
                    }
                } catch(Exception e){
                    Log.e("Error in getting bill information", e.toString());
                }

                intent.putExtra(CongressionalViewActivity.HANDLES_MESSAGE, picture_message);
                intent.putExtra(CongressionalViewActivity.NAME_MESSAGE, name_message);
                intent.putExtra(CongressionalViewActivity.DIST_MESSAGE, dist_message);
                intent.putExtra(CongressionalViewActivity.PARTY_MESSAGE, party_message);
                intent.putExtra(CongressionalViewActivity.EMAIL_MESSAGE, email_message);
                intent.putExtra(CongressionalViewActivity.PHONE_MESSAGE, phone_message);
                intent.putExtra(CongressionalViewActivity.TERM_MESSAGE, term_message);
                intent.putExtra(CongressionalViewActivity.COMMITTEE_MESSAGE, committee_message);
                intent.putExtra(CongressionalViewActivity.BILLS_MESSAGE, bills_message);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
            catch (Exception e) {
                Log.e("Exception caught", e.toString());
            }
        } else {
            super.onMessageReceived(messageEvent);
        }

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
}

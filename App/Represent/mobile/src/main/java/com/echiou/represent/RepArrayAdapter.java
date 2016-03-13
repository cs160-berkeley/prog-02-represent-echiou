package com.echiou.represent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.echiou.represent.R;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import io.fabric.sdk.android.Fabric;

/**
 * Created by ethan on 3/1/16.
 */
public class RepArrayAdapter extends BaseAdapter {
    private final Context context;
    private final String[] repNames;
    private final String[] parties;
    private final String[] emails;
    private final String[] phoneNumbers;
    private final String[] tweets;
    private final String[] handles;
    private final String[] bios;
    private final String[] sites;
    private InputStream input;
    public final static String TWITTER_KEY = "u7QF6HENk6ME6YaZDN5BnLlRl";
    public final static String TWITTER_SECRET = "6s4LlsZLgQd7XicJvemjAyQDvoQmx6YCArMrsfF0HH25YMNlh8";


    public RepArrayAdapter(Context context, String[] repNames, String[] parties, String[] emails, String[] phoneNumbers, String[] tweets, String[] handles, String[] bio, String[] sites) {
        this.context = context;
        this.repNames = repNames;
        this.parties = parties;
        this.emails = emails;
        this.phoneNumbers = phoneNumbers;
        this.tweets = tweets;
        this.handles = handles;
        this.bios = bio;
        this.sites = sites;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View repRow = inflater.inflate(R.layout.rep_row_layout, parent, false);
        TextView nameView = (TextView) repRow.findViewById(R.id.rep_name);
        TextView partyView = (TextView) repRow.findViewById(R.id.rep_party);
        TextView emailView = (TextView) repRow.findViewById(R.id.rep_email);
        TextView phoneView = (TextView) repRow.findViewById(R.id.rep_phone);
        TextView siteView = (TextView) repRow.findViewById(R.id.rep_site);
        final TextView tweetView = (TextView) repRow.findViewById(R.id.rep_tweet);
        TextView handleView = (TextView) repRow.findViewById(R.id.rep_handle);
        final ImageView pictureView = (ImageView) repRow.findViewById(R.id.rep_picture);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this.context, new Twitter(authConfig));

        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
            @Override
            public void success(Result<AppSession> appSessionResult) {
                AppSession session = appSessionResult.data;
                TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(session);
                twitterApiClient.getStatusesService().userTimeline(null, handles[position], 1, null, null, false, false, false, true, new Callback<List<Tweet>>() {
                    @Override
                    public void success(Result<List<Tweet>> listResult) {
                        for(Tweet tweet: listResult.data) {
                            tweetView.setText(tweet.text);
                            Picasso.with(context).load(tweet.user.profileImageUrl).into(pictureView);
                        }
                    }
                    @Override
                    public void failure(TwitterException e) {
                        e.printStackTrace();
                    }
                });
            }
            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
            }
        });

        nameView.setText(repNames[position]);
        partyView.setText(parties[position]);
        emailView.setText(emails[position]);
        emailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");

                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{emails[position]});

                context.startActivity(Intent.createChooser(emailIntent, null));
            }
        });
        phoneView.setText(phoneNumbers[position]);
        siteView.setText(sites[position]);
        siteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse( sites[position] );
                context.startActivity( new Intent( Intent.ACTION_VIEW, uri ) );
            }
        });
        handleView.setText("- @" + handles[position]);
        handleView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse( "https://twitter.com/" + handles[position] );
                context.startActivity( new Intent( Intent.ACTION_VIEW, uri ) );
            }
        });

        //Attach onClickListener
        repRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailedViewActivity.class);
                int picture_message = 0;
                String name_message = "";
                String dist_message = "";
                String party_message = "";
                String email_message = "";
                String phone_message = "";
                String term_message = "";
                String handle_message = "";

                try {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                    StrictMode.setThreadPolicy(policy);

                    String url = "https://congress.api.sunlightfoundation.com/legislators?bioguide_id=" + bios[position] + "&apikey=9adaa4743c4c4c76815a0c92f82934d1";
                    input = new URL(url).openStream();

                    BufferedReader streamReader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
                    StringBuilder responseStrBuilder = new StringBuilder();

                    String inputStr;
                    while ((inputStr = streamReader.readLine()) != null) {
                        responseStrBuilder.append(inputStr);
                    }
                    JSONObject input_json = new JSONObject(responseStrBuilder.toString());
                    JSONObject rep_object = input_json.optJSONArray("results").getJSONObject(0);

                    name_message = rep_object.getString("first_name") + " " + rep_object.getString("last_name");
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
                    handle_message = rep_object.getString("twitter_id");

                } catch(Exception e){
                    Log.e("Error in getting information", e.toString());
                }

                String[] committee_message = new String[2];
                try {
                    String url = "https://congress.api.sunlightfoundation.com/committees?member_ids=" + bios[position] + "&apikey=9adaa4743c4c4c76815a0c92f82934d1";
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
                    String url = "https://congress.api.sunlightfoundation.com/bills?sponsor_id=" + bios[position] + "&apikey=9adaa4743c4c4c76815a0c92f82934d1";
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

                intent.putExtra(CongressionalViewActivity.PICTURE_MESSAGE, picture_message);
                intent.putExtra(CongressionalViewActivity.NAME_MESSAGE, name_message);
                intent.putExtra(CongressionalViewActivity.DIST_MESSAGE, dist_message);
                intent.putExtra(CongressionalViewActivity.PARTY_MESSAGE, party_message);
                intent.putExtra(CongressionalViewActivity.EMAIL_MESSAGE, email_message);
                intent.putExtra(CongressionalViewActivity.PHONE_MESSAGE, phone_message);
                intent.putExtra(CongressionalViewActivity.TERM_MESSAGE, term_message);
                intent.putExtra(CongressionalViewActivity.COMMITTEE_MESSAGE, committee_message);
                intent.putExtra(CongressionalViewActivity.BILLS_MESSAGE, bills_message);
                intent.putExtra(CongressionalViewActivity.HANDLES_MESSAGE, handle_message);
                context.startActivity(intent);
            }
        });
        return repRow;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return repNames.length;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }
}






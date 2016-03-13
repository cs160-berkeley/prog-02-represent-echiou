package com.echiou.represent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

import java.util.List;

import io.fabric.sdk.android.Fabric;


public class DetailedViewActivity extends Activity {

    public final static String TWITTER_KEY = "u7QF6HENk6ME6YaZDN5BnLlRl";
    public final static String TWITTER_SECRET = "6s4LlsZLgQd7XicJvemjAyQDvoQmx6YCArMrsfF0HH25YMNlh8";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);
        final ListView comList = (ListView)findViewById(R.id.committeelistview);
        final ListView billList = (ListView)findViewById(R.id.billlistview);

        final Intent intent = getIntent();

        int picture_message = intent.getIntExtra(CongressionalViewActivity.PICTURE_MESSAGE, 0);
        ImageView picture = (ImageView) findViewById(R.id.rep_picture);
        picture.setImageResource(picture_message);
        String name_message = intent.getStringExtra(CongressionalViewActivity.NAME_MESSAGE);
        TextView name = (TextView) findViewById(R.id.rep_name);
        name.setText(name_message);
        String dist_message = intent.getStringExtra(CongressionalViewActivity.DIST_MESSAGE);
        TextView dist = (TextView) findViewById(R.id.rep_dist);
        dist.setText(dist_message);
        String party_message = intent.getStringExtra(CongressionalViewActivity.PARTY_MESSAGE);
        TextView party = (TextView) findViewById(R.id.rep_party);
        party.setText(party_message);
        final String email_message = intent.getStringExtra(CongressionalViewActivity.EMAIL_MESSAGE);
        TextView email = (TextView) findViewById(R.id.rep_email);
        email.setText(email_message);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");

                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email_message});

                startActivity(Intent.createChooser(emailIntent, null));
            }
        });
        String phone_message = intent.getStringExtra(CongressionalViewActivity.PHONE_MESSAGE);
        TextView phone = (TextView) findViewById(R.id.rep_phone);
        phone.setText(phone_message);
        String term_message = intent.getStringExtra(CongressionalViewActivity.TERM_MESSAGE);
        TextView term = (TextView) findViewById(R.id.rep_term);
        term.setText(term_message);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        final Context context = this;

        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
            @Override
            public void success(Result<AppSession> appSessionResult) {
                AppSession session = appSessionResult.data;
                TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(session);
                twitterApiClient.getStatusesService().userTimeline(null, intent.getStringExtra(CongressionalViewActivity.HANDLES_MESSAGE), 1, null, null, false, false, false, true, new Callback<List<Tweet>>() {
                    @Override
                    public void success(Result<List<Tweet>> listResult) {
                        for(Tweet tweet: listResult.data) {
                            final ImageView pictureView = (ImageView) findViewById(R.id.rep_picture);
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

        String[] committee_message = intent.getStringArrayExtra(CongressionalViewActivity.COMMITTEE_MESSAGE);
        final ArrayAdapter com_adapter = new ArrayAdapter<String>(this, R.layout.detailed_listitem, committee_message);
        comList.setAdapter(com_adapter);
        String[] bills_message = intent.getStringArrayExtra(CongressionalViewActivity.BILLS_MESSAGE);
        final ArrayAdapter bills_adapter = new ArrayAdapter<String>(this, R.layout.detailed_listitem, bills_message);
        billList.setAdapter(bills_adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detailed_view, menu);
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
}

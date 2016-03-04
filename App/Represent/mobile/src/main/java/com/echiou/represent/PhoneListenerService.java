package com.echiou.represent;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Random;

/**
 * Created by ethan on 3/2/16.
 */
public class PhoneListenerService extends WearableListenerService {

    //   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
    private static final String CONGRESS = "/refresh_congress";
    private static final String SHOW_DETAILED = "/show_detailed";
    final Random rand = new Random();
    // TODO: Fix up the randoms for picking zip codes.
    String[] dists = {"AK01", "NV03", "NY10"};

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());
        if( messageEvent.getPath().equalsIgnoreCase(CONGRESS) ) {
            // We don't care about the message's actual data - we are just going to pick a random district now.
            String dist = dists[rand.nextInt(dists.length)];

            Intent intent = new Intent(this, CongressionalViewActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(MainActivity.EXTRA_MESSAGE, dist);
            startActivity(intent);
        } else if( messageEvent.getPath().equalsIgnoreCase(SHOW_DETAILED) ) {
            try {
                String senName = new String(messageEvent.getData(), "UTF-8");

                Context mContext = getBaseContext();

                Intent intent = new Intent(mContext, DetailedViewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //TODO: Use API to get congressman data
                int picture_message = R.drawable.rep1;
                String name_message = "Barbara Boxer";
                String dist_message = "Senator for CA";
                String party_message = "Democrat";
                String email_message = "boxer@boxer.senate.gov";
                String phone_message = "(202)224-3553";
                String term_message = "Election for next term in 2018";
                String[] committee_message = {"The Senate Select Committee on Ethics", "Senate Committee on Environment and Public Works"};
                String[] bills_message = {
                        "S. 2487 Female Veteran Suicide Prevention Act",
                        "S. 2412 Tule Lake National Historic Site Establishment Act of 2015",
                        "S. 2204 End of Suffering Act of 2015"
                };

                intent.putExtra(CongressionalViewActivity.PICTURE_MESSAGE, picture_message);
                intent.putExtra(CongressionalViewActivity.NAME_MESSAGE, name_message);
                intent.putExtra(CongressionalViewActivity.DIST_MESSAGE, dist_message);
                intent.putExtra(CongressionalViewActivity.PARTY_MESSAGE, party_message);
                intent.putExtra(CongressionalViewActivity.EMAIL_MESSAGE, email_message);
                intent.putExtra(CongressionalViewActivity.PHONE_MESSAGE, phone_message);
                intent.putExtra(CongressionalViewActivity.TERM_MESSAGE, term_message);
                intent.putExtra(CongressionalViewActivity.COMMITTEE_MESSAGE, committee_message);
                intent.putExtra(CongressionalViewActivity.BILLS_MESSAGE, bills_message);
                mContext.startActivity(intent);
            }
            catch (Exception e) {
                Log.e("Exception caught", e.toString());
            }
        } else {
            super.onMessageReceived(messageEvent);
        }

    }
}

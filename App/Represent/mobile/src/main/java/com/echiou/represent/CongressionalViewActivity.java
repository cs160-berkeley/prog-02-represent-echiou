package com.echiou.represent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional_view);
        final ListView repList = (ListView)findViewById(R.id.replistview);
        final ListView senList = (ListView)findViewById(R.id.senlistview);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView location = (TextView) findViewById(R.id.header);
        location.setText(message);

        String[] repNames;
        String[] repParties;
        String[] repEmails;
        String[] repPhoneNumbers;
        String[] repTweets;
        String[] repHandles;
        String[] repDist;
        int[] repPictures;

        String[] senNames;
        String[] senParties;
        String[] senEmails;
        String[] senPhoneNumbers;
        String[] senTweets;
        String[] senHandles;
        String[] senDist;
        int[] senPictures;
        // TODO: Parse for districts (regex?), get senators.
        // For now, just checking CA17 or zip code.
        if(message.contentEquals("CA17")){
            // TODO: Use API to find relevant representatives, compile into ListView.
            repNames = new String[] {"Michael M. Honda"};
            repParties = new String[] {"Democrat"};
            repEmails = new String[] {"honda@honda.house.gov"};
            repPhoneNumbers = new String[] {"(202)222-2631"};
            repTweets = new String[] {"(Honda's first tweet)"};
            repHandles = new String[] {"RepMikeHonda"};
            repDist = new String[] {"CA17"};
            repPictures = new int[] {R.drawable.rep1};

            senNames = new String[] {"Dianne Feinstein", "Barbara Boxer"};
            senParties = new String[] {"Democrat", "Democrat"};
            senEmails = new String[] {"feinstein@feinstein.senate.gov", "boxer@boxer.senate.gov"};
            senPhoneNumbers = new String[] {"(202)224-3841", "(202)224-3553"};
            senTweets = new String[] {"(Feinstein's first tweet)", "(Boxer's first tweet)"};
            senHandles = new String[] {"SenFeinstein", "SenatorBoxer"};
            senDist = new String[] {"CA", "CA"};
            senPictures = new int[] {R.drawable.rep3, R.drawable.rep4};
        } else if (message.contentEquals("AK01")){
            // TODO: Same
            repNames = new String[] {"Don Young"};
            repParties = new String[] {"Republican"};
            repEmails = new String[] {"donyoung@donyoung.house.gov"};
            repPhoneNumbers = new String[] {"(907)271-5978"};
            repTweets = new String[] {"(Young's first tweet)"};
            repHandles = new String[] {"RepDonYoung"};
            repDist = new String[] {"AK01"};
            repPictures = new int[] {R.drawable.rep1};

            senNames = new String[] {"Dan Sullivan", "Lisa Murkowskir"};
            senParties = new String[] {"Republican", "Republican"};
            senEmails = new String[] {"sullivan@sullivan.senate.gov", "murkowskir@murkowskir.senate.gov"};
            senPhoneNumbers = new String[] {"(202)224-3004", "(202)224-6665"};
            senTweets = new String[] {"(Sullivan's first tweet)", "(Murkowskir's first tweet)"};
            senHandles = new String[] {"SenDanSullivan", "lisamurkowskir"};
            senDist = new String[] {"AK", "AK"};
            senPictures = new int[] {R.drawable.rep3, R.drawable.rep4};
        } else if (message.contentEquals("NV03")){
            // TODO: Same
            repNames = new String[] {"Joe Heck"};
            repParties = new String[] {"Republican"};
            repEmails = new String[] {"joeheck@joeheck.house.gov"};
            repPhoneNumbers = new String[] {"(202)371-5237"};
            repTweets = new String[] {"(Heck's first tweet)"};
            repHandles = new String[] {"RepJoeHeck"};
            repDist = new String[] {"NV03"};
            repPictures = new int[] {R.drawable.rep1};

            senNames = new String[] {"Harry Reid", "Dean Heller"};
            senParties = new String[] {"Democrat", "Republican"};
            senEmails = new String[] {"reid@reid.senate.gov", "heller@heller.senate.gov"};
            senPhoneNumbers = new String[] {"(202)234-3010", "(202)264-1625"};
            senTweets = new String[] {"(Reid's first tweet)", "(Heller's first tweet)"};
            senHandles = new String[] {"SenReid", "SenDeanHeller"};
            senDist = new String[] {"NV", "NV"};
            senPictures = new int[] {R.drawable.rep3, R.drawable.rep4};
        } else if (message.contentEquals("NY10")){
            // TODO: Same
            repNames = new String[] {"Jerrold Nadler"};
            repParties = new String[] {"Democrat"};
            repEmails = new String[] {"nadler@nadler.house.gov"};
            repPhoneNumbers = new String[] {"(202)221-5978"};
            repTweets = new String[] {"(Nadler's first tweet)"};
            repHandles = new String[] {"RepJerroldNadler"};
            repDist = new String[] {"NY10"};
            repPictures = new int[] {R.drawable.rep1};

            senNames = new String[] {"Kirsten Gillibrand", "Chuck Schumer"};
            senParties = new String[] {"Democrat", "Democrat"};
            senEmails = new String[] {"gillibrand@gillibrand.senate.gov", "schumer@schumer.senate.gov"};
            senPhoneNumbers = new String[] {"(202)224-3034", "(202)264-6625"};
            senTweets = new String[] {"(Gillibrand's first tweet)", "(Schumer's first tweet)"};
            senHandles = new String[] {"SenKirstenGillibrand", "SenSchumer"};
            senDist = new String[] {"NY", "NY"};
            senPictures = new int[] {R.drawable.rep3, R.drawable.rep4};
        } else {
            // TODO: Same
            repNames = new String[] {"Michael M. Honda", "Anna G. Eshoo"};
            repParties = new String[] {"Democrat", "Democrat"};
            repEmails = new String[] {"honda@honda.house.gov", "eshoo@eshoo.house.gov"};
            repPhoneNumbers = new String[] {"(202)222-2631", "(202)225-8104"};
            repTweets = new String[] {"(Honda's first tweet)", "(Eshoo's first tweet)"};
            repHandles = new String[] {"RepMikeHonda", "RepAnnaEshoo"};
            repDist = new String[] {"CA17", "CA18"};
            repPictures = new int[] {R.drawable.rep1, R.drawable.rep2};

            senNames = new String[] {"Dianne Feinstein", "Barbara Boxer"};
            senParties = new String[] {"Democrat", "Democrat"};
            senEmails = new String[] {"feinstein@feinstein.senate.gov", "boxer@boxer.senate.gov"};
            senPhoneNumbers = new String[] {"(202)224-3841", "(202)224-3553"};
            senTweets = new String[] {"(Feinstein's first tweet)", "(Boxer's first tweet)"};
            senHandles = new String[] {"SenFeinstein", "SenatorBoxer"};
            senDist = new String[] {"CA", "CA"};
            senPictures = new int[] {R.drawable.rep3, R.drawable.rep4};
        }

        Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
        sendIntent.putExtra("ACTIVITY_TYPE", "/rep");
        //TODO: Get actual Obama v. Romney , construct strings.
        String[] OvR = {"70", "27", "CA District 17"};
        if (message.contentEquals("AK01")){
            OvR = new String[] {"41", "55", "At-large, Alaska"};
        } else if (message.contentEquals("NV03")){
            OvR = new String[] {"50", "49", "Nevada District 3"};
        } else if (message.contentEquals("NY10")){
            OvR = new String[] {"74", "25", "New York District 10"};
        }
        sendIntent.putExtra("NAME_AND_PARTY_MESSAGE", concat(repNames, concat(senNames, concat(repParties, concat(senParties, OvR)))));
        startService(sendIntent);


        final RepArrayAdapter repAdapter = new RepArrayAdapter(this, repNames, repParties, repEmails, repPhoneNumbers, repTweets, repHandles, repPictures);
        final RepArrayAdapter senAdapter = new RepArrayAdapter(this, senNames, senParties, senEmails, senPhoneNumbers, senTweets, senHandles, senPictures);
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

package com.echiou.represent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class DetailedViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);
        final ListView comList = (ListView)findViewById(R.id.committeelistview);
        final ListView billList = (ListView)findViewById(R.id.billlistview);

        Intent intent = getIntent();

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
        String email_message = intent.getStringExtra(CongressionalViewActivity.EMAIL_MESSAGE);
        TextView email = (TextView) findViewById(R.id.rep_email);
        email.setText(email_message);
        String phone_message = intent.getStringExtra(CongressionalViewActivity.PHONE_MESSAGE);
        TextView phone = (TextView) findViewById(R.id.rep_phone);
        phone.setText(phone_message);
        String term_message = intent.getStringExtra(CongressionalViewActivity.TERM_MESSAGE);
        TextView term = (TextView) findViewById(R.id.rep_term);
        term.setText(term_message);

        String[] committee_message = intent.getStringArrayExtra(CongressionalViewActivity.COMMITTEE_MESSAGE);
        final ArrayAdapter com_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, committee_message);
        comList.setAdapter(com_adapter);
        String[] bills_message = intent.getStringArrayExtra(CongressionalViewActivity.BILLS_MESSAGE);
        final ArrayAdapter bills_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, bills_message);
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

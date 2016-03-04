package com.echiou.represent;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.echiou.represent.R;

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
    private final int[] pictures;

    public RepArrayAdapter(Context context, String[] repNames, String[] parties, String[] emails, String[] phoneNumbers, String[] tweets, String[] handles, int[] pictures) {
        this.context = context;
        this.repNames = repNames;
        this.parties = parties;
        this.emails = emails;
        this.phoneNumbers = phoneNumbers;
        this.tweets = tweets;
        this.handles = handles;
        this.pictures = pictures;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View repRow = inflater.inflate(R.layout.rep_row_layout, parent, false);
        TextView nameView = (TextView) repRow.findViewById(R.id.rep_name);
        TextView partyView = (TextView) repRow.findViewById(R.id.rep_party);
        TextView emailView = (TextView) repRow.findViewById(R.id.rep_email);
        TextView phoneView = (TextView) repRow.findViewById(R.id.rep_phone);
        TextView tweetView = (TextView) repRow.findViewById(R.id.rep_tweet);
        TextView handleView = (TextView) repRow.findViewById(R.id.rep_handle);
        ImageView pictureView = (ImageView) repRow.findViewById(R.id.rep_picture);

        nameView.setText(repNames[position]);
        partyView.setText(parties[position]);
        emailView.setText(emails[position]);
        phoneView.setText(phoneNumbers[position]);
        tweetView.setText(tweets[position]);
        handleView.setText(handles[position]);
        pictureView.setImageResource(pictures[position]);

        //Attach onClickListener
        repRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailedViewActivity.class);
                //TODO: Use API to get congressman data
                int picture_message = R.drawable.rep1;
                String name_message = "Michael M. Honda";
                String dist_message = "Representative for CA17";
                String party_message = "Democrat";
                String email_message = "honda@honda.house.gov";
                String phone_message = "(202)225-2631";
                String term_message = "Election for next term in 2016";
                String[] committee_message = {"The House Committee on Appropriations"};
                String[] bills_message = {
                        "H.R.4471 Educator Preparation Reform Act",
                        "H.Res.561 Expressing support for support of transgender acceptance",
                        "H.R.4013 Equity and Excellence in American Education Act of 2015",
                        "H.Res.519 Supporting the ideas and goals of the International Day for the Elimination of Violence against Women"
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






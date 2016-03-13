package com.echiou.represent;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.util.Log;
import android.view.View;

import java.util.List;

/**
 * Created by ethan on 3/2/16.
 */
public class RepGridPagerAdapter extends FragmentGridPagerAdapter {

    private final Context mContext;
    private final String[] names;
    private final String[] parties;
    private final String[] ovr;

    public RepGridPagerAdapter(Context ctx, FragmentManager fm, String[] names, String[] parties, String[] ovr) {
        super(fm);
        mContext = ctx;
        this.names = names;
        this.parties = parties;
        this.ovr = ovr;
    }


    @Override
    public RepCardFragment getFragment(int row, int col) {
        RepCardFragment fragment;
        if(row == 0){
            fragment = getRepCardFragment(names[col], parties[col], names[col]);
        }
        else {
            if (ovr != null) {
                fragment = RepCardFragment.create(ovr[0] + "% v. " + ovr[1] + "%", "Obama v. Romney, 2012 election," + ovr[2]);
            }
            else {
                fragment = null;
            }
        }
        return fragment;
    }

    private RepCardFragment getRepCardFragment(String title, String text, String message) {
        final RepCardFragment fragment = RepCardFragment.create(title, text);
        final String mes = message;
        fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Log.d("Something", "Was clicked");

                Intent sendIntent = new Intent(mContext, WatchToPhoneService.class);
                sendIntent.putExtra("ACTIVITY_TYPE", "/det");
                sendIntent.putExtra("DET_CURRENT", mes);
                mContext.startService(sendIntent);
            }
        });
        return fragment;
    }

    @Override
    public int getRowCount() {
        if (ovr.length > 0) {
            return 2;
        }
        else{
            return 1;
        }
    }

    @Override
    public int getColumnCount(int i) {
        if (i == 0) {
            return names.length;
        }
        return 1; //Obama vs. Romney column has only 1!
    }

    static final int[] BG_IMAGES = new int[] {
        R.drawable.capitol,
        R.drawable.ovr
    };

    // Obtain the background image for the row
    @Override
    public Drawable getBackgroundForRow(int row) {
        return mContext.getResources().getDrawable(
                (BG_IMAGES[row]), null);
    }
}
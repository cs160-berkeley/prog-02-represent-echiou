package com.echiou.represent;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.wearable.view.CardFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class RepCardFragment extends CardFragment {
    private View fragmentView;
    private View.OnClickListener listener;

    @Override
    public View onCreateContentView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        fragmentView = super.onCreateContentView(inflater, container, savedInstanceState);
        fragmentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (listener != null) {
                    listener.onClick(view);
                }
            }
        });
        return fragmentView;
    }

    static public RepCardFragment create (String title, String desc) {
        RepCardFragment rcf = new RepCardFragment();
        Bundle args = new Bundle();
        args.putString(CardFragment.KEY_TITLE, title);
        args.putString(CardFragment.KEY_TEXT, desc);

        rcf.setArguments(args);
        return rcf;
    }

    public void setOnClickListener(final View.OnClickListener listener) {
        this.listener = listener;
    }
}

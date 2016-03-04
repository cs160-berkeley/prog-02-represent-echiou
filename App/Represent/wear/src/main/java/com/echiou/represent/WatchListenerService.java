package com.echiou.represent;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class WatchListenerService extends WearableListenerService {
    // In PhoneToWatchService, we passed in a path, either "/FRED" or "/LEXY"
    // These paths serve to differentiate different phone-to-watch messages
    private static final String REP = "/rep";
    private static final String OB_V_ROM = "/2012";

    public static final String REP_NAME_MESSAGE = "REP_NAME_MESSAGE";
    public static final String REP_PARTY_MESSAGE = "REP_PARTY_MESSAGE";
    public static final String OBAMA_V_ROMNEY_MESSAGE = "OBAMA_V_ROMNEY_MESSAGE";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in WatchListenerService, got: " + messageEvent.getPath());

        if( messageEvent.getPath().equalsIgnoreCase( REP ) ) { //Received a representative
            String[] values = byteArrToStringArr(messageEvent.getData());
            int repsLen = values.length - 3; // Subtract 3 for the obama v romney pages
            String[] names = Arrays.copyOfRange(values, 0, repsLen / 2); // always div 2!
            String[] parties = Arrays.copyOfRange(values, repsLen / 2, repsLen);
            String[] obamaVRomney = Arrays.copyOfRange(values, repsLen, values.length);
            Intent intent = new Intent(this, MainActivity.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //you need to add this flag since you're starting a new activity from a service
            intent.putExtra(REP_NAME_MESSAGE, names);
            intent.putExtra(REP_PARTY_MESSAGE, parties);
            intent.putExtra(OBAMA_V_ROMNEY_MESSAGE, obamaVRomney);
            startActivity(intent);
        } else {
            super.onMessageReceived( messageEvent );
        }

    }

    public String[] byteArrToStringArr(byte[] byteArr) {
        String[] stringArr = {};
        try {
            final ByteArrayInputStream byteArrayInputStream =
                    new ByteArrayInputStream(byteArr);
            final ObjectInputStream objectInputStream =
                    new ObjectInputStream(byteArrayInputStream);

            stringArr = (String[]) objectInputStream.readObject();

            objectInputStream.close();
        } catch(Exception e) {
            Log.d("Error", e.toString());
        }

        return stringArr;
    }
}
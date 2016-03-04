package com.echiou.represent;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ethan on 3/2/16.
 */
public class WatchToPhoneService extends Service implements GoogleApiClient.ConnectionCallbacks {

    private GoogleApiClient mWatchApiClient;
    private List<Node> nodes = new ArrayList<>();

    private static final String CONG = "/cong";
    private static final String DET = "/det";
    private String current = "cong";
    private String det_current = "";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("T", "creating WatchToPhoneService");
        //initialize the googleAPIClient for message passing
        mWatchApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .addConnectionCallbacks(this)
                .build();
        //and actually connect it
        mWatchApiClient.connect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mWatchApiClient.disconnect();
        Log.i("WatchToPhoneService", "Received start id " + startId + ": " + intent);

        final Bundle extras = intent.getExtras();
        current = extras.getString("ACTIVITY_TYPE");
        det_current = extras.getString("DET_CURRENT");

        mWatchApiClient.connect();
        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mWatchApiClient.disconnect();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override //alternate method to connecting: no longer create this in a new thread, but as a callback
    public void onConnected(Bundle bundle) {
        Log.d("T", "in onConnected");
        if (current.contentEquals(CONG)) {
            Wearable.NodeApi.getConnectedNodes(mWatchApiClient)
                    .setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                        @Override
                        public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                            nodes = getConnectedNodesResult.getNodes();
                            Log.d("T", "found nodes");
                            //when we find a connected node, we populate the list declared above
                            //finally, we can send a message
                            sendMessage("/refresh_congress", "Randomization done phone-side.");
                            Log.d("T", "sent");
                        }
                    });
        }
        else if (current.contentEquals(DET)){
            Wearable.NodeApi.getConnectedNodes(mWatchApiClient)
                    .setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                        @Override
                        public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                            nodes = getConnectedNodesResult.getNodes();
                            Log.d("T", "found nodes");
                            //when we find a connected node, we populate the list declared above
                            //finally, we can send a message
                            sendMessage("/show_detailed", det_current);
                            Log.d("T", "sent");
                        }
                    });
        }

    }

    @Override //we need this to implement GoogleApiClient.ConnectionsCallback
    public void onConnectionSuspended(int i) {}

    private void sendMessage(final String path, final String text ) {
        for (Node node : nodes) {
            Wearable.MessageApi.sendMessage(
                    mWatchApiClient, node.getId(), path, text.getBytes());
        }
    }

}

package com.android.ocasa.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by ignacio on 15/02/16.
 */
public class RequestTableReceiver extends BroadcastReceiver {

    private static final String TAG = "RequestTableReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        String tableId = intent.getStringExtra("id");

        Log.v(TAG, "Table : " + tableId);

        requestDownloadColumn(context, tableId);
        requestDownloadRecord(context, tableId);
    }

    private void requestDownloadColumn(Context context, String tableId){

    }

    private void requestDownloadRecord(Context context, String tableId){

    }
}

package com.doomers.hackpaytm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

public class NetStateListener extends BroadcastReceiver {

    public NetStateListener() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context,"called",Toast.LENGTH_LONG).show();
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE );
        NetworkInfo activeNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        // boolean isConnected = activeNetInfo != null && activeNetInfo.isConnectedOrConnecting();
        //if (isConnected) {
        if(!Constants.net_active && !Constants.app_back) {
            Intent i = new Intent(context, HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("WhichActivity","NetState");

            context.startActivity(i);

            Toast.makeText(context, "connected", Toast.LENGTH_LONG).show();
            Constants.net_active=true;

        }
        //}
        //else Toast.makeText(context,"Not connected",Toast.LENGTH_LONG).show();
    }
}

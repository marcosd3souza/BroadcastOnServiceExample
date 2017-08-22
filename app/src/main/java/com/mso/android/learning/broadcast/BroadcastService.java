package com.mso.android.learning.broadcast;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.mso.android.learning.Commons;
import com.mso.android.learning.activities.MainActivity;

import java.util.ArrayList;

/**
 * Created by marcos on 22/08/17.
 */

public class BroadcastService extends Service {

    private String LOG_TAG = null;
    private Handler mHandler = new Handler();
    private Runnable myTask;

    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Commons.BROADCAST_MESSAGE);
        registerReceiver(receiver, intentFilter);

        LOG_TAG = this.getClass().getSimpleName();
        Log.i(LOG_TAG, "In onCreate");
        myTask = new Runnable() {
            @Override
            public void run() {

                Log.i(LOG_TAG, "service on background");
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(Commons.BROADCAST_MESSAGE);
                broadcastIntent.putExtra(Commons.BROADCAST_MESSAGE_CONTENT, "Broadcast Data");
                sendBroadcast(broadcastIntent);
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(LOG_TAG, "In onStartCommand");

        mHandler.postDelayed(myTask, 5000);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(Commons.BROADCAST_MESSAGE)){
                //((TextView) findViewById(R.id.content)).setText(intent.getStringExtra(Commons.BROADCAST_MESSAGE_CONTENT));
                Toast.makeText(context, intent.getStringExtra(Commons.BROADCAST_MESSAGE_CONTENT), Toast.LENGTH_SHORT).show();

                Intent stopIntent = new Intent(context, BroadcastService.class);
                stopService(stopIntent);

                unregisterReceiver(this);
            }
        }
    };
}

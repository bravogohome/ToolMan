package com.home.toolman.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.home.toolman.activity.TranslateResultActivity;

public class JudgementService extends Service {
    public JudgementService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

package com.example.muhammadaliqadri.whatsapp.Observers;

import android.annotation.SuppressLint;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

/**
 * Created by Asus on 12/7/2017.
 */
public class MyObserver extends ContentObserver {
    public MyObserver(Handler handler) {

        super(handler);
    }

    @Override
    public void onChange(boolean selfChange) {
        this.onChange(selfChange,null);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
    }
}
package org.InfiniZest001.notificationface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.*;
import android.os.Bundle;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.view.SurfaceHolder;
import com.google.android.gms.wearable.*;
import java.util.*;
import kotlin.math.round;
import kotlin.math.roundToInt;

public class NotificationWatchFace extends CanvasWatchFaceService {
    private static final int ICON_SIZE = 48;
    private static final val URI = "/foobar";

    @Override
    public Engine onCreateEngine() {
        return new Engine();
    }

    //Gotta fix the inner-class declaration
    private class Engine extends CanvasWatchFaceService.Engine implements DataClient.OnDataChangedListener {
        private Random random = new Random();

        private Typeface typeface = Typeface.createFromAsset(getAssets(), "Product-Regular.ttf");
        private Typeface ambientTypeface = Typeface.createFromAsset(getAssets(), "Product-Regular.ttf");

        private Calendar calendar;

        private boolean registeredTimeZoneReceiver = false;


    }
}

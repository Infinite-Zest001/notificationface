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
        return new NotificationWatchFace.Engine();
    }

    //Gotta fix the inner-class declaration
    private class Engine extends CanvasWatchFaceService.Engine implements OnDataChangedListener {
        private Random random = new Random();

        private Typeface typeface = Typeface.createFromAsset(getAssets(), "Product-Regular.ttf");
        private Typeface ambientTypeface = Typeface.createFromAsset(getAssets(), "Product-Regular.ttf");

        private Calendar calendar;

        private boolean registeredTimeZoneReceiver = false;

        private Paint textPaint;

        private List bitmaps;
        private List safeBitmaps;

        private boolean ambient = false;
        private boolean lowBitAmbient = false;
        private boolean burnInProtection = false;

        private BroadcastReceiver timeZoneReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                calendar.setTimeZone(TimeZone.getDefault());
                invalidate();
            }
        }

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);

            setWatchFaceStyle(new WatchFaceStyle.Builder(NotificationWatchFace.this).build());

            calendar = Calendar.getInstance();

            textPaint = new Paint();
            textPaint.setTypeface(this.typeface);
            textPaint.setAntiAlias(true);
            textPaint.setColor(-1);
            textPaint.setTextAlign(Paint.Align.CENTER);
            this.textPaint = textPaint;
            }

        }

    }
}

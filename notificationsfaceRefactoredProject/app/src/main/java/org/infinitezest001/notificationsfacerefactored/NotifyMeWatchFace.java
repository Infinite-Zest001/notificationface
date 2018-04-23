package org.infinitezest001.notificationsfacerefactored;

import android.content.BroadcastReceiver;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.view.SurfaceHolder;

import com.google.android.gms.wearable.DataClient;

import java.net.URI;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import CanvasWatchFaceService;

public class NotifyMeWatchFace extends CanvasWatchFaceService {

    private static final int ICON_SIZE = 48;
    private static final String URI =  "/foobar";

    @Override
    public Engine onCreateEngine() {
        return new Engine();
    }

    private class Engine extends CanvasWatchFaceService.Engine implements DataClient.OnDataChangedListener {

        private Random random = new Random();

        //Typeface typeface = new Typeface.createFromAsset(fonts,  "GoogleSan-Regular.ttf");
        private Calendar calendar;

        private boolean registeredTimeZoneReceiver = false;

        private Paint textPaint;

        //private List<Bitmap> bitmaps = new List<Bitmap>() {

        //private List<Bitmap> safeBitmaps;

        private boolean ambient = false;
        private boolean lowBitAmbient = false;
        private boolean burnInProtection = false;

        private void BroadcastReceiver timeZoneReceiver = new BroadcastReceiver() object {

        }

    }

}

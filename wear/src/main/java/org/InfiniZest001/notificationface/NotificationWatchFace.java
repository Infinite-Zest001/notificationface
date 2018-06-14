package org.InfiniZest001.notificationface;

import android.app.Service;
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

import org.jfedor.notificationface.MyWatchFace;

import java.util.*;

import kotlin.collections.CollectionsKt;
import kotlin.math.MathKt;

import static java.lang.String.format;

public class NotificationWatchFace extends CanvasWatchFaceService {
    private static final int ICON_SIZE = 48;
    private static final String URI = "/foobar";

    @Override
    public Engine onCreateEngine() {
        return new NotificationWatchFace.Engine();
    }

    //public final class Engine extends CanvasWatchFaceService.Engine implements DataClient.OnDataChangedListener {
    public final class Engine extends CanvasWatchFaceService.Engine {
        private Random random = new Random();

        private Typeface typeface = Typeface.createFromAsset(NotificationWatchFace.this.getAssets(), "Product-Regular.ttf");
        private Typeface ambientTypeface = Typeface.createFromAsset(NotificationWatchFace.this.getAssets(), "Product-Regular.ttf");

        private Calendar calendar;

        private boolean registeredTimeZoneReceiver;

        private Paint textPaint;

        private List<Bitmap> bitmaps;
        private List<Bitmap> safeBitmaps;

        private boolean ambient;
        private boolean lowBitAmbient;
        private boolean burnInProtection;

        private final BroadcastReceiver timeZoneReciever;

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);

            this.setWatchFaceStyle((new WatchFaceStyle.Builder(NotificationWatchFace.this)).build());

            this.calendar = Calendar.getInstance();

            this.textPaint = new Paint();
            this.textPaint.setTypeface(this.typeface);
            this.textPaint.setAntiAlias(true);
            this.textPaint.setColor(Color.WHITE);
            this.textPaint.setTextAlign(Paint.Align.CENTER);
            }

        public void onPropertiesChanged(Bundle properties) {
            super.onPropertiesChanged(properties);
            this.lowBitAmbient = properties.getBoolean(
                    WatchFaceService.PROPERTY_LOW_BIT_AMBIENT, false);
            this.burnInProtection = properties.getBoolean(
                    WatchFaceService.PROPERTY_BURN_IN_PROTECTION, false);
        }

        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            this.ambient = inAmbientMode;

            if (this.lowBitAmbient) {
                this.textPaint.setAntiAlias(!inAmbientMode);
            }

            this.textPaint.setTypeface(this.ambient ? this.ambientTypeface : this.typeface);
        }

        public void onDraw(Canvas canvas, Rect bounds) {
            int size = bounds.width();

            canvas.drawColor(Color.BLACK);

            this.calendar.setTimeInMillis(System.currentTimeMillis());

            String text = format("%d:%02d",
                    this.calendar.get(Calendar.HOUR), this.calendar.get(Calendar.MINUTE));

            float textX = (float)bounds.width() * 0.5F;

            float textY = (float)Math.rint((float)bounds.height() * 0.5F +
                    (this.bitmaps.isEmpty() ? size * 0.1F : size * -0.033F));
            this.textPaint.setTextSize((float)Math.rint((float)size/4F));
            canvas.drawText(text, textX, textY, this.textPaint);

            int padding = 2;
            int x = bounds.width() / 2 - (bitmaps.size() * (ICON_SIZE + padding) /2);
            int y = bounds.height() / 2 + MathKt.roundToInt((float)size * 0.088f);
            if(this.ambient) {
                int maxOffset = MathKt.roundToInt((float)size * 0.177F);
                x += this.random.nextInt((maxOffset) - maxOffset / 2);
                y += this.random.nextInt(maxOffset);
            }

            int index = 0;
            for(Bitmap bitmap : ((this.ambient && this.burnInProtection) ? this.safeBitmaps : this.bitmaps)) {
               canvas.drawBitmap(bitmap, null, new Rect(x +index * (ICON_SIZE + padding), y,
                       x + index * (ICON_SIZE + padding) + ICON_SIZE, y + ICON_SIZE), this.textPaint);
               index++;
            }
        }

        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);

            if(visible) {
                Wearable.getDataClient(NotificationWatchFace.this).addListener(this);
                Wearable.getDataClient(NotificationWatchFace.this).getDataItems().addOnSuccessListener((DataClient.OnDataChangedListener) { dataItemBuffer -> {
                for(DataItem dataItem : dataItemBuffer) {
                    Engine.this.processDataItem(dataItem);
                }
                dataItemBuffer.release();
                Engine.this.invalidate();
                }
                registerReceiver();
                }
        } else {
                Wearable.getDataClient(NotificationWatchFace.this).removeListener(this);
            }
        }
        private final void registerReceiver() {
            if (!this.registeredTimeZoneReceiver) {
                this.registeredTimeZoneReceiver = true;

            }
        }

        private final void unregisterReceiver() {

        }

        public void onDataChanged(DataEventBuffer dataEvents) {
            for (DataEvent event : dataEvents) {
                if (event.getType() == DataEvent.TYPE_CHANGED && event.getDataItem().getUri().getPath() == URI) {
                    this.processDataItem(event.getDataItem());

                }
            }
        }

        private final void processDataItem(DataItem dataItem) {
            ArrayList newBitmaps = new ArrayList();
            ArrayList newSafeBitmaps = new ArrayList();
            DataMapItem dataMapItem = DataMapItem.fromDataItem(dataItem);
        }

        private final Bitmap loadBitmapFromByteArray(byte[] byteArray) {

        }

    }
}

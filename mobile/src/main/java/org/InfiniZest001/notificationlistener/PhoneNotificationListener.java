package org.InfiniZest001.notificationlistener;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.ByteArrayOutputStream;
import java.util.*;

import kotlin.collections.CollectionsKt;
import kotlin.collections.IndexedValue;


public class PhoneNotificationListener extends NotificationListenerService {
    private static final int ICON_SIZE = 48;
    private static final String URI = "/foobar";

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        this.sendToWatch(this.getCurrentRanking());
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn, RankingMap rankingMap) {
        this.sendToWatch(rankingMap);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn, RankingMap rankingMap) {
        this.sendToWatch(rankingMap);
    }

    private void sendToWatch(RankingMap rankingMap) {
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create(URI);
        ArrayList bitmaps = new ArrayList();
        for (StatusBarNotification notification : this.getActiveNotifications()) {
            Ranking ranking = new Ranking();
            rankingMap.getRanking(notification.getKey(), ranking);

            if (ranking.getImportance() <= NotificationManager.IMPORTANCE_MIN ||
                    notification.getPackageName().equals("android") &&
                            notification.getNotification().getChannelId().equals("FOREGROUND_SERVICE")) {
                continue;
            }

            Bitmap bitmap = this.drawableToBitmap(
                    notification.getNotification().getSmallIcon().loadDrawable(this));
            Iterable itX = (Iterable)bitmaps;
            boolean x;
            if (itX instanceof Collection  && ((Collection)itX).isEmpty()) {
                x = false;
            } else {
                Iterator itY = bitmaps.iterator();
                while(true) {
                    if (!itY.hasNext()) {
                        x = false;
                        break;
                    }

                    Object e = itY.next();
                    Bitmap it = (Bitmap)e;
                    if (it.sameAs(bitmap)) {
                        x = true;
                        break;
                    }
                }
            }

            if (!x) {
                bitmaps.add(bitmap);
            }
        }
        Iterator var18 = CollectionsKt.withIndex((Iterable)bitmaps).iterator();

        while(var18.hasNext()) {
            IndexedValue var16 = (IndexedValue)var18.next();
            int i = var16.component1();
            Bitmap bitmap = (Bitmap)var16.component2();
            putDataMapReq.getDataMap().putByteArray("icon" + i, this.bitmapToByteArray(bitmap));
            putDataMapReq.getDataMap().putByteArray("safeicon" + i, this.bitmapToByteArray(this.createBurnInSafeBitmap(bitmap)));
        }

        putDataMapReq.setUrgent();
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        Wearable.getDataClient((Context)this).putDataItem(putDataReq);
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(ICON_SIZE, ICON_SIZE, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return  stream.toByteArray();
    }

    private Bitmap createBurnInSafeBitmap(Bitmap bitmap) {
        Bitmap bitmap2 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        int x = 0;

        for(int var4 = bitmap.getWidth(); x < var4; ++x) {
            int y = 0;

            for(int var6 = bitmap.getHeight(); y < var6; ++y) {
                if (y % 4 == 2 * (x % 2)) {
                    bitmap2.setPixel(x, y, bitmap.getPixel(x, y));
                }
            }
        }

        return  bitmap2;
    }
}

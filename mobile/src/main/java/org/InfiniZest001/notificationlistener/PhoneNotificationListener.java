package org.InfiniZest001.notificationlistener;

import android.app.Notification;
import android.app.NotificationManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

import org.jfedor.notificationface.NotificationListener;

import java.io.ByteArrayOutputStream;
import java.util.*;

public class PhoneNotificationListener extends NotificationListenerService {
    private static final int ICON_SIZE = 48;
    private static final String URI = "/foobar";

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        sendToWatch(currentRanking);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn, RankingMap rankingMap) {
        sendToWatch(rankingMap);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn, RankingMap rankingMap) {
        sendToWatch(rankingMap);
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

            Bitmap bitmap = drawableToBitmap(
                    notification.getNotification().getSmallIcon().loadDrawable(this));
            //TODO: What does it mean? Iterable
            /*
            if (!bitmaps.equals(true)) { it.sameAs(bitmap) }) {
                bitmaps.add(bitmap)
            }
        }
        for ((i, bitmap) in bitmaps.withIndex()) {
            putDataMapReq.dataMap.putByteArray("icon$i", bitmapToByteArray(bitmap))
            putDataMapReq.dataMap.putByteArray("safeicon$i",
                    bitmapToByteArray(createBurnInSafeBitmap(bitmap)))
        }
        putDataMapReq.setUrgent()
        val putDataReq = putDataMapReq.asPutDataRequest()
        Wearable.getDataClient(this).putDataItem(putDataReq)
    */
        }
    }

    private final Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(ICON_SIZE, ICON_SIZE, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}

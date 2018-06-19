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

            Bitmap bitmap = this.drawableToBitmap(
                    notification.getNotification().getSmallIcon().loadDrawable(this));
            //Extracted from Kotlin decompile
            Iterable receiver = (Iterable)bitmaps;
            boolean x;
            if ( receiver instanceof Collection && ((Collection)receiver).isEmpty()) {
                x = false;
            } else {
                Iterator y = receiver.iterator();
                while(true) {
                    if (!y.hasNext()) {
                        x = false;
                        break;
                    }

                    Object e = y.next();
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
        //TODO implement the for in loop
    }

    private final Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(ICON_SIZE, ICON_SIZE, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    //TODO: Implement bitmapToByteArray
    //TODO: Implement  createBurnInSafeBitmap
}

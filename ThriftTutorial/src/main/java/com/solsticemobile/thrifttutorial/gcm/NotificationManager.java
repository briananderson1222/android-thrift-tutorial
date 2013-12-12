package com.solsticemobile.thrifttutorial.gcm;

/**
 * Created by briananderson on 12/11/13.
 */
public class NotificationManager {
    private static NotificationManager instance = null;
    private NotificationHandler handler;
    public static NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    public void onNotificationReceived(final String sender, final String message) {
        handler.onNotificationReceived(sender,message);
    }

    public void setHandler(NotificationHandler handler) {
        this.handler = handler;
    }
}

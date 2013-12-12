package com.solsticemobile.thrifttutorial.gcm;

/**
 * Created by briananderson on 12/11/13.
 */
public interface NotificationHandler {

    public void onNotificationReceived(String sender, String message);

}

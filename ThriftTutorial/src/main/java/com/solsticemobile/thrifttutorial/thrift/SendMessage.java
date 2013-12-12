package com.solsticemobile.thrifttutorial.thrift;

import android.os.AsyncTask;
import android.util.Log;

import com.chat.api.ChatAPI;
import com.solsticemobile.thrifttutorial.gcm.GcmManager;

/**
 * Created by briananderson on 12/12/13.
 */
public class SendMessage  extends AsyncTask<Void,Void,Void> {

    final static String TAG = "SendMessage";

    private ChatAPI.Client client;
    private String username, message;
    private SendMessageHandler handler;

    public SendMessage(ChatAPI.Client client, String username, String message, SendMessageHandler handler) {
        this.client = client;
        this.username = username;
        this.message = message;
        this.handler = handler;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            client.sendMessage(message, username, GcmManager.getInstance().chatToken);
        } catch (Exception e) {
            Log.d(TAG, "Unknown error when sending a message to "+username);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void object) {
        super.onPostExecute(object);
        handler.onMessageSent();
    }

}

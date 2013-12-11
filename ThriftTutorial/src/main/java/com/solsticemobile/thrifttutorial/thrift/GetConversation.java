package com.solsticemobile.thrifttutorial.thrift;

import android.os.AsyncTask;
import android.util.Log;

import com.chat.api.ChatAPI;
import com.chat.api.ChatMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by briananderson on 12/11/13.
 */
public class GetConversation extends AsyncTask<Void,Void,String> {

    final static String TAG = "GetConversation";

    private ChatAPI.Client client;
    private GetConversationHandler handler;
    private String username, token;
    private List<ChatMessage> conversationList;


    public GetConversation(ChatAPI.Client client, String username, String token, GetConversationHandler handler) {
        this.client = client;
        this.username = username;
        this.token = token;
        this.handler = handler;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            conversationList = client.getConversation(username, token);
        } catch (Exception e) {
            conversationList = new ArrayList<ChatMessage>();
            Log.d(TAG, "Unknown error when getting conversations");
        }
        return token;
    }

    @Override
    protected void onPostExecute(String string) {
        super.onPostExecute(string);
        handler.onConversationReceived(conversationList);
    }
}

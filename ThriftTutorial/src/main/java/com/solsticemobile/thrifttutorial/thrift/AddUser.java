package com.solsticemobile.thrifttutorial.thrift;

import android.os.AsyncTask;
import android.util.Log;

import com.chat.api.ChatAPI;

/**
 * Created by briananderson on 12/10/13.
 */

public class AddUser extends AsyncTask<Void,Void,String> {

    final static String TAG = "AddUser";

    private ChatAPI.Client client;
    private AddUserHandler handler;
    private String username, token;

    public AddUser(ChatAPI.Client client, String username, AddUserHandler handler) {
        this.client = client;
        this.username = username;
        this.handler = handler;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            token = client.addNewUser(username);
        } catch (Exception e) {
            Log.d(TAG, "Unknown error when adding user");
        }
        return token;
    }

    @Override
    protected void onPostExecute(String string) {
        super.onPostExecute(string);
        handler.onUserAdded(token);
    }
}

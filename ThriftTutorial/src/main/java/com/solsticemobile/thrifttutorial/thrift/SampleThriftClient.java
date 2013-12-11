package com.solsticemobile.thrifttutorial.thrift;

import android.util.Log;

import com.chat.api.ChatAPI;
import com.chat.api.ChatMessage;
import com.solsticemobile.thrifttutorial.gcm.GcmManager;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransportException;

import java.util.List;

/**
 * Created by briananderson on 12/10/13.
 */
public class SampleThriftClient {

    final static String TAG = "SampleThriftClient";

    final static public String host = "192.168.56.1";
    final static public int port = 8080;

    static public ChatAPI.Client Factory() {
        THttpClient transport = null;
        try {
            transport = new THttpClient("http://" + host + ":" + port);
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        TBinaryProtocol protocol = new TBinaryProtocol(transport);
        return new ChatAPI.Client(protocol);
    }

    final static public ChatAPI.Client getChatClient() {
        return SampleThriftClient.Factory();
    }

    final static public AddUser addNewUser(String username, final AddUserHandler addUserHandler) {
        AddUser addUser = new AddUser(getChatClient(),username, new AddUserHandler() {
            @Override
            public void onUserAdded(String token) {
                addUserHandler.onUserAdded(token);
                Log.d(TAG, "User Token: " + token);
            }
        });
        addUser.execute();
        return addUser;
    }

    final static public GetConversation getConversation(String username, String token, final GetConversationHandler getConversationHandler) {
        GetConversation getConversation = new GetConversation(getChatClient(), username, token, new GetConversationHandler() {
            @Override
            public void onConversationReceived(List<ChatMessage> conversationList) {
                getConversationHandler.onConversationReceived(conversationList);
                Log.d(TAG, "Conversation List: " + conversationList.toString());
            }
        });
        getConversation.execute();
        return getConversation;
    }

}

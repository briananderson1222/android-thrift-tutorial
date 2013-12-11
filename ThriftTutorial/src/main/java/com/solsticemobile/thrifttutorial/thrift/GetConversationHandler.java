package com.solsticemobile.thrifttutorial.thrift;

import com.chat.api.ChatMessage;

import java.util.List;

/**
 * Created by briananderson on 12/11/13.
 */
public interface GetConversationHandler {

    void onConversationReceived(List<ChatMessage> conversationList);

}

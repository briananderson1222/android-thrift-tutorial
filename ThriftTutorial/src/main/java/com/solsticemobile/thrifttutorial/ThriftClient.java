package com.solsticemobile.thrifttutorial;

import com.chat.api.ChatAPI;
import com.chat.api.ChatMessage;
import com.chat.api.UserAlreadyRegisteredException;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import java.util.ArrayList;

/**
 * Created by briananderson on 12/9/13.
 */
public class ThriftClient {

    TTransport transport;
    ChatAPI.Client client;


    public ThriftClient(String host, int port, int timeout) {
        try {
            transport = new TSocket(host, port, timeout);
            transport.open();
        } catch (Exception e) {
            System.out.println(host+":"+port+" > Unable to open the transport.");
            System.exit(1);
        }
        TProtocol protocol = new TBinaryProtocol(transport);
        client = new ChatAPI.Client(protocol);
        String username = "B-ri";
        String username1 = "K-Dogg";
        addUserToClient(username);
        addUserToClient(username1);
        String messageToKelton = "So Thrifty!";
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessage(messageToKelton);
        chatMessage.setImage(new ArrayList<Byte>());
        System.out.println(username + " sent a message to " + username1 + ": " + messageToKelton);
        try {
            client.sendMessage(chatMessage,username1,"token");
        } catch (Exception e) {
            System.out.println("sendMessage wasn't found on the ChatAPI::Client");
        }
        transport.close();
    }

    public ThriftClient(String host, int port) {
        this(host,port,30000);
    }

    public void addUserToClient(String username) {
        try {
            System.out.println(username + " added with token " + client.addNewUser(username));
        } catch (UserAlreadyRegisteredException e) {
            System.out.println(username + " already exists");
        } catch (Exception e) {
            System.out.println("Unknown error when adding a user to the server-client");
        }
    }

}

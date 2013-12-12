package com.solsticemobile.thrifttutorial.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.chat.api.ChatMessage;
import com.solsticemobile.thrifttutorial.MainActivity;
import com.solsticemobile.thrifttutorial.R;
import com.solsticemobile.thrifttutorial.adapters.ConversationListAdapter;
import com.solsticemobile.thrifttutorial.gcm.GcmManager;
import com.solsticemobile.thrifttutorial.gcm.NotificationHandler;
import com.solsticemobile.thrifttutorial.gcm.NotificationManager;
import com.solsticemobile.thrifttutorial.thrift.AddUserHandler;
import com.solsticemobile.thrifttutorial.thrift.GetConversationHandler;
import com.solsticemobile.thrifttutorial.thrift.SampleThriftClient;
import com.solsticemobile.thrifttutorial.thrift.SendMessageHandler;

import java.util.List;

/**
 * Created by briananderson on 12/10/13.
 */
public class ComposeMessageFragment extends Fragment implements NotificationHandler {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String ARG_SECTION_NUMBER = "section_number";

    public ListView mConversationListView;
    public TextView mConversationEditMessage;
    public Button mConversationSendButton;
    public ConversationListAdapter mConversationListAdapter;

    private String mMyUsername, mTheirUsername;
    private List<ChatMessage> mConversationList;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ComposeMessageFragment newInstance(final int sectionNumber) {
        final ComposeMessageFragment fragment = new ComposeMessageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        NotificationManager.getInstance().setHandler(fragment);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_compose_message, container, false);
        setHasOptionsMenu(true);
        mConversationListView = (ListView) rootView.findViewById(R.id.listConversation);
        mConversationSendButton = (Button) rootView.findViewById(R.id.sendButton);
        mConversationEditMessage = (TextView) rootView.findViewById(R.id.editMessage);
        mConversationListAdapter = new ConversationListAdapter(getActivity());
        mConversationListView.setAdapter(mConversationListAdapter);
        mConversationSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(mConversationEditMessage.getText().toString());
                mConversationEditMessage.setText("");
            }
        });
        fetchConversations();
        return rootView;
    }

    private void sendMessage(String message) {
        SampleThriftClient.sendMessage(message,mTheirUsername, new SendMessageHandler() {
            @Override
            public void onMessageSent() {
                fetchConversations();
            }
        });
    }

    private void fetchConversations() {
        switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
            case 1:
                mTheirUsername = getString(R.string.title_kelton);
                break;
            case 2:
                mTheirUsername = getString(R.string.title_brian);
                break;
        }
        mMyUsername = getActivity().getString(R.string.title_brian);
        fetchAndStoreConversations();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_refresh:
                fetchConversations();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchAndStoreConversations() {
        SampleThriftClient.addNewUser(mMyUsername, new AddUserHandler() {
            @Override
            public void onUserAdded(String token) {
                GcmManager.getInstance().setupForPlayServices(getActivity(), token);
                SampleThriftClient.getConversation(mTheirUsername, token, new GetConversationHandler() {
                    @Override
                    public void onConversationReceived(List<ChatMessage> conversationList) {
                        mConversationList = conversationList;
                        mConversationListAdapter.setDataSource(conversationList);
                        mConversationListAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }


    @Override
    public void onNotificationReceived(String sender, String message) {
        ChatMessage chatMessage = new ChatMessage(message,sender,null);
        mConversationList.add(chatMessage);
        mConversationListAdapter.setDataSource(mConversationList);
        Handler mainHandler = new Handler(getActivity().getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mConversationListAdapter.notifyDataSetChanged();
            }
        };
        mainHandler.post(runnable);
    }
}

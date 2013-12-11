package com.solsticemobile.thrifttutorial.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.chat.api.ChatMessage;
import com.solsticemobile.thrifttutorial.gcm.GcmManager;
import com.solsticemobile.thrifttutorial.thrift.AddUserHandler;
import com.solsticemobile.thrifttutorial.MainActivity;
import com.solsticemobile.thrifttutorial.R;
import com.solsticemobile.thrifttutorial.thrift.GetConversationHandler;
import com.solsticemobile.thrifttutorial.thrift.SampleThriftClient;
import com.solsticemobile.thrifttutorial.adapters.ConversationListAdapter;

import java.util.List;

/**
 * Created by briananderson on 12/10/13.
 */
public class ComposeMessageFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String ARG_SECTION_NUMBER = "section_number";

    private static final String ARG_CONVERSATION_USERNAME = "conversation_username";

    public ListView mConversationListView;
    public ConversationListAdapter mConversationListAdapter;

    private List<ChatMessage> mConversationList;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ComposeMessageFragment newInstance(final int sectionNumber, final String username) {
        final ComposeMessageFragment fragment = new ComposeMessageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putString(ARG_CONVERSATION_USERNAME, username);
        fragment.setArguments(args);

        SampleThriftClient.addNewUser(username, new AddUserHandler() {
            @Override
            public void onUserAdded(String token) {
                GcmManager.getInstance().setupForPlayServices(fragment.getActivity(), token);
                SampleThriftClient.getConversation(username, token, new GetConversationHandler() {
                    @Override
                    public void onConversationReceived(List<ChatMessage> conversationList) {
                        fragment.mConversationList = conversationList;
                    }
                });
            }
        });
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mConversationListView = (ListView) rootView.findViewById(R.id.listConversation);
        mConversationListAdapter = new ConversationListAdapter(getActivity(), mConversationList);
        return rootView;
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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

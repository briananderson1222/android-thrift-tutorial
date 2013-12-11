package com.solsticemobile.thrifttutorial.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chat.api.ChatMessage;
import com.solsticemobile.thrifttutorial.R;

import java.util.List;

/**
 * Created by briananderson on 12/11/13.
 */
public class ConversationListAdapter extends BaseAdapter {

    private Context mContext;
    private List<ChatMessage> mDataSource;
    private LayoutInflater mInflater;

    public ConversationListAdapter(Context c, List<ChatMessage> conversationList) {
        mContext = c;
        mDataSource = conversationList;
        mInflater = (LayoutInflater)c.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    public void setDataSource(List<ChatMessage> conversationList) {
        mDataSource = conversationList;
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public ChatMessage getItem(int i) {
        return mDataSource.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        HolderItem holder = null;
        ChatMessage chatMessage = getItem(i);

        if (view == null) {
            view = mInflater.inflate(R.layout.conversation_list_item, null);
            holder = new HolderItem(view,chatMessage);
            view.setTag(holder);
        } else {
            holder = (HolderItem) view.getTag();
            holder.populateCellWithChatMessage(chatMessage);
        }
        view.setTag(holder);

        return view;
    }

    public class HolderItem {

        TextView messageView;

        public HolderItem(View view, ChatMessage chatMessage) {
            initConversationListItemWithView(this, view);
            populateCellWithChatMessage(chatMessage);
        }

        private void initConversationListItemWithView(HolderItem holder, View view) {
            holder.messageView = (TextView) view.findViewById(R.id.message);
        }

        public void populateCellWithChatMessage(ChatMessage chatMessage) {
            setMessage(chatMessage.content);
        }

        public  void setMessage(String message) {
            messageView.setText(message);
        }
    }

}

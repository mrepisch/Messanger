package main.nerd.messenger.main.nerd.messenger.chat;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import main.nerd.messenger.R;


public class MessageIncoming extends Fragment{

    private TextView time, chatText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        time = (TextView) container.findViewById(R.id.tv_time);
        chatText = (TextView) container.findViewById(R.id.tv_chat_text);

        return inflater.inflate(R.layout.message_incoming, container, false);
    }
}

package main.nerd.messenger.main.nerd.messenger.chat;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import main.nerd.messenger.R;
import main.nerd.messenger.SocketController;

/**
 * Created by bblans on 02.05.2017.
 */

public class MessageAdapter extends ArrayAdapter<MessageModel> {
    public MessageAdapter(@NonNull Context context, ArrayList<MessageModel>t_data) {
        super(context, 0, t_data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final MessageModel a_message = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if( a_message.getFrom() == SocketController.getInstance().getuserName())
        {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.right, parent, false);
                TextView a_msg = (TextView) convertView.findViewById(R.id.msgr);
                a_msg.setText(a_message.getMessage());


            }
        }
        else
        {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.left, parent, false);
                TextView a_msg = (TextView) convertView.findViewById(R.id.msgl);
                a_msg.setText(a_message.getMessage());


            }

        }
        // Return the completed view to render on screen
        return convertView;
    }
}

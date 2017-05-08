package main.nerd.messenger.main.nerd.messenger.chat;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import main.nerd.messenger.ChatActivity;
import main.nerd.messenger.ChatListActivity;
import main.nerd.messenger.MainActivity;
import main.nerd.messenger.R;
import main.nerd.messenger.SocketController;

/**
 * Created by bblans on 01.05.2017.
 */

public class ContactAvaiableAdapter extends ArrayAdapter<ContactXmlModel> {
    private ChatListActivity m_activity;

    public ContactAvaiableAdapter(@NonNull Context context, ArrayList<ContactXmlModel>t_array) {
        super(context,0,t_array);
        m_activity = (ChatListActivity)context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final ContactXmlModel user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_avaiable, parent, false);
        }
        // Lookup view for data population
        final TextView userName = (TextView) convertView.findViewById(R.id.contactName);
        // Populate the data into the template view using the data object
        userName.setText(user.getUserName());




        Button startChatBtn = (Button) convertView.findViewById(R.id.startChatBtn);
        if( user.getIsOnline()) {
            Log.w("USER IS ONLINE:",user.getUserName());
            if( SocketController.getInstance().getHasChatAllready(user.getUserName()) == false)
            {
                SocketController.getInstance().addChat(new ChatModel(user.getUserName()));
            }
            startChatBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent a_contactListActivity = new Intent(m_activity, ChatActivity.class);
                    a_contactListActivity.putExtra("username",user.getUserName());
                    m_activity.startActivity(a_contactListActivity);

                }
            });
        }
        else
        {
            startChatBtn.setVisibility(View.INVISIBLE);
            SocketController.getInstance().removeChat(user.getUserName());
        }
        Button a_addBtn = (Button)convertView.findViewById(R.id.delBtn);
        a_addBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                m_activity.removeUser(user.getUserName());
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }
}

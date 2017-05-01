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

import main.nerd.messenger.ChatListActivity;
import main.nerd.messenger.R;

/**
 * Created by bblans on 01.05.2017.
 */

public class ContactAdapter extends ArrayAdapter<ContactXmlModel> {

    private ChatListActivity m_activity;

    public ContactAdapter(ChatListActivity t_activity, @NonNull Context context, ArrayList<ContactXmlModel>t_data) {
        super(context, 0, t_data);
        m_activity = t_activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final ContactXmlModel user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact, parent, false);
        }
        // Lookup view for data population
        TextView userName = (TextView) convertView.findViewById(R.id.contactName);
        // Populate the data into the template view using the data object
        userName.setText(user.getUserName());

        Button a_addBtn = (Button)convertView.findViewById(R.id.addbtn);
        a_addBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                m_activity.addUser(user);
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }
}

package main.nerd.messenger.main.nerd.messenger.chat;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import main.nerd.messenger.R;
import main.nerd.messenger.SocketController;

/**
 * Created by bblans on 02.05.2017.
 */

public class MessageAdapter extends BaseAdapter {

    private List<MessageModel> items;
    private Context context;
    private LayoutInflater m_inflater;

    /**
     * Constructor which calls the super constructor and sets variables there
     * @param context context for super constructor
     * @param t_data arrayList for super constructor
     */
    public MessageAdapter(@NonNull Context context, ArrayList<MessageModel> t_data) {

        this.context = context;
        items = t_data;

        m_inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     *  Sets and swaps items
     * @param items list of items
     */
    public void swapItems(List<MessageModel> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    /**
     * Overriden method that returns items size
     * @return items.size()
     */
    @Override
    public int getCount() {
        return items.size();
    }

    /**
     * Getter for item
     * @param position position of item
     * @return returns item at given position
     */
    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    /**
     * Getter for itemiD
     * @param position itemID
     * @return itemID
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Overridden method to get the View
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        final MessageModel a_message = items.get(position);
        ViewHolder holder = null;

        // Check if an existing view is being reused, otherwise inflate the view

        Log.w("ME OWN FUCKING SHIT", "");

        if (convertView == null) {
            holder = new ViewHolder();


            convertView = m_inflater.inflate(R.layout.right, null);
            holder.a_msg = (TextView) convertView.findViewById(R.id.msgr);


            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.a_msg.setText(a_message.getFrom()+":\n" + a_message.getMessage());

        // Return the completed view to render on screen
        return convertView;
    }

    private static class ViewHolder {
        public TextView a_msg;
    }
}

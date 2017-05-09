package main.nerd.messenger.main.nerd.messenger.chat;

import android.icu.text.DateFormat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by bblans on 02.05.2017.
 */

public class ChatModel {

    private String m_userNameTo;
    private ArrayList<MessageModel>m_messanges;
    boolean m_hasChanges = false;

    public ChatModel(String t_usernameTo)
    {
        m_userNameTo = t_usernameTo;
        m_messanges = new ArrayList<MessageModel>();
    }

    public  synchronized  String getUserNameTo()
    {
        return m_userNameTo;
    }


    public  synchronized   void addMessages(String t_message, String t_from)
    {
        Log.w("adding messag to list",t_message);
        m_hasChanges = true;
        m_messanges.add(new MessageModel(t_from, t_message ));
    }

    public synchronized  ArrayList<MessageModel>getMessages()
    {
        return m_messanges;
    }

    public void resetChanges()
    {
        m_hasChanges = false;
    }
}

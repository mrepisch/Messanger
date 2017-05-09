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

    /**
     * Constructor to set and initialize the variables
     * @param t_usernameTo the username to whom this chat has to be opened
     */
    public ChatModel(String t_usernameTo)
    {
        m_userNameTo = t_usernameTo;
        m_messanges = new ArrayList<MessageModel>();
    }

    /**
     * Getter for the username
     * @return m_username
     */
    public  synchronized  String getUserNameTo()
    {
        return m_userNameTo;
    }

    /**
     * Adds a Message to array list of messages
     * @param t_message the message that has to be added
     * @param t_from the username from whom this message was sent
     */
    public  synchronized   void addMessages(String t_message, String t_from)
    {
        Log.w("adding messag to list",t_message);
        m_hasChanges = true;
        m_messanges.add(new MessageModel(t_from, t_message ));
    }

    /**
     * Getter for arrayList of messages
     * @return arrayList m_messages
     */
    public synchronized  ArrayList<MessageModel>getMessages()
    {
        return m_messanges;
    }

    /**
     * Resets the m_hasChanged variable
     */
    public void resetChanges()
    {
        m_hasChanges = false;
    }
}

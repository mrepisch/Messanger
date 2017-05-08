package main.nerd.messenger.main.nerd.messenger.chat;

/**
 * Created by bblans on 02.05.2017.
 */

public class MessageModel {

    private String m_message;
    private String m_from;

    public MessageModel(String t_from, String t_message)
    {
        m_from = t_from;
        m_message = t_message;

    }

    public String getMessage()
    {
        return m_message;
    }



    public String getFrom()
    {
        return m_from;
    }




}

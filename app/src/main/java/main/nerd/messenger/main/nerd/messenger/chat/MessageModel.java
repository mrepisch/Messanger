package main.nerd.messenger.main.nerd.messenger.chat;

/**
 * Created by bblans on 02.05.2017.
 */

public class MessageModel {

    private String m_message;
    private String m_date;
    private String m_from;

    public MessageModel(String t_from, String t_message, String t_date)
    {
        m_from = t_from;
        m_message = t_message;
        m_date = t_date;
    }

    public String getMessage()
    {
        return m_message;
    }

    public String getDate()
    {
        return m_date;
    }

    public String getFrom()
    {
        return m_from;
    }




}

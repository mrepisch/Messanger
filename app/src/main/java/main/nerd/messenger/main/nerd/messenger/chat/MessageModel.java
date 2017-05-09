package main.nerd.messenger.main.nerd.messenger.chat;

/**
 * Created by bblans on 02.05.2017.
 */

public class MessageModel {

    private String m_message;
    private String m_from;

    /**
     * Constructor to set variables
     * @param t_from sets m_from with the username of the person that sent the message
     * @param t_message sets m_message with the message
     */
    public MessageModel(String t_from, String t_message)
    {
        m_from = t_from;
        m_message = t_message;

    }

    /**
     * Getter for the message
     * @return m_message
     */
    public synchronized String getMessage()
    {
        return m_message;
    }

    /**
     * Getter for the from variable
     * @return m_from
     */
    public synchronized  String getFrom()
    {
        return m_from;
    }




}

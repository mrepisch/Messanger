package main.nerd.messenger;

import java.util.ArrayList;

import main.nerd.messenger.main.nerd.messenger.chat.ChatModel;
import tcp.nerd.messenger.MessengerTcpSocket;

/**
 * Created by bblans on 25.04.2017.
 */

public class SocketController {
    private static final SocketController ourInstance = new SocketController();

    private MessengerTcpSocket m_socket;

    private boolean m_hasMessages = false;

    private String m_userID;

    private String m_userName;

    private ArrayList<ChatModel>m_chatModels = new ArrayList<ChatModel>();

    private ArrayList<TcpMessageReader>m_activitys = new ArrayList<TcpMessageReader>();

    /**
     * Singleton method to return the instance
     * @return instance
     */
    public static SocketController getInstance() {
        return ourInstance;
    }

    /**
     * Empty base constructor
     */
    private SocketController() {
    }

    /**
     * Setter for m_userName
     * @param t_username username to be set
     */
    public void setUserName( String t_username){
        m_userName = t_username;
    }

    /**
     * Getter for m_userName
     * @return m_userName variable
     */
    public String getuserName()
    {
        return m_userName;
    }

    /**
     * Getter for messages in socket
     * @return arrayList of messages
     */
    public synchronized ArrayList<String> getReceivtMessages()
    {
        return m_socket.getReceivedList();
    }

    /**
     * Setter for m_hasMessages
     * @param t_hasMessages boolean to be set
     */
    public synchronized void setHasMsgs(boolean t_hasMessages)
    {
        m_hasMessages = t_hasMessages;
    }

    /**
     * Getter for m_hasMessages
     * @return m_hasMessages
     */
    public synchronized boolean gethasMsgs()
    {
        return m_hasMessages;
    }

    /**
     * Getter for m_socket
     * @return socket m_socket
     */
    public synchronized  MessengerTcpSocket getSocket()
    {
        return m_socket;
    }

    /**
     * Setter for m_UserID
     * @param t_userID userID to be set
     */
    public synchronized void setUserID(String t_userID)
    {
        m_userID = t_userID;
    }

    /**
     * Removes message from received ones
     * @param t_msg the message that has to be deleted
     */
    public void removeMsg(String t_msg)
    {
        if( t_msg != null) {


            for (int i = 0; i < m_socket.getReceivedList().size(); i++) {
                if (t_msg.equals(m_socket.getReceivedList().get(i))) {
                    m_socket.getReceivedList().remove(i);
                }
            }
        }
    }

    /**
     * Adds {@link TcpMessageReader} to the activities
     * @param t_reader the reader to be added
     */
    public synchronized void addTcMessageReader(TcpMessageReader t_reader)
    {
        m_activitys.add(t_reader);
    }

    /**
     * Removes {@link TcpMessageReader} from activity
     * @param name of the activity from which the messageReader has to be removed
     */
    public synchronized void removeMessageReader(String name)
    {
        for( int i = 0; i < m_activitys.size(); i++)
        {
            if( m_activitys.get(i).getName().equals(name))
            {
                m_activitys.remove(i);
            }
        }
    }

    /**
     * Processes the messages
     */
    public synchronized void processMessage()
    {
        for( int i = 0; i < m_activitys.size(); i++)
        {
            if( m_activitys.get(i) != null) {
                m_activitys.get(i).readMessages(m_socket.getReceivedList());
            }
        }
    }

    /**
     * Sets the Ip and starts the socket
     * @param t_ip ip to be set
     * @param t_activity activity to which the socket has to be added
     */
    public synchronized void setIpAndStartSocket(String t_ip,MainActivity t_activity)
    {
        m_socket = new MessengerTcpSocket(t_activity);
        m_socket.start();
        m_socket.setIpAndConnect(t_ip);

    }

    /**
     * Gets chat from ChatModel by username
     * @param a_userName the username from which the chat has to be get
     * @return chatModel of the corresponding user
     */
    public synchronized ChatModel getChat(String a_userName) {
        ChatModel r_mode = null;
        for( int i = 0; i < m_chatModels.size(); i++)
        {
            if( m_chatModels.get(i).getUserNameTo().equals(a_userName))
            {
                r_mode = m_chatModels.get(i);
            }
        }
        return r_mode;
    }

    /**
     * Adds chat to m_chatModel arrayList
     * @param chatModel chatModel to be added
     */
    public void addChat(ChatModel chatModel) {
        m_chatModels.add(chatModel);
    }

    public boolean getHasChatAllready(String userName) {
        if( getChat(userName) == null)
        {
            return false;
        }
        return true;
    }

    /**
     * Removes chat from m_chatModel arrayList
     * @param userName userName of the Chat that has to be removed
     */
    public void removeChat(String userName) {
        for( int i = 0; i < m_chatModels.size(); i++)
        {
            if( m_chatModels.get(i).getUserNameTo().equals(userName))
            {
                m_chatModels.remove(i);
            }
        }
    }

}

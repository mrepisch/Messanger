package main.nerd.messenger;

import java.util.ArrayList;

import main.nerd.messenger.main.nerd.messenger.chat.ChatModel;
import tcp.nerd.messenger.ChatWorkerThreat;
import tcp.nerd.messenger.MessengerTcpSocket;

/**
 * Created by bblans on 25.04.2017.
 */

public class SocketController {
    private static final SocketController ourInstance = new SocketController();

    private MessengerTcpSocket m_socket;

    private ChatWorkerThreat m_chatWorker = new ChatWorkerThreat();

    private boolean m_hasMessages = false;

    private String m_userID;

    private String m_userName;

    private ArrayList<ChatModel>m_chatModels = new ArrayList<ChatModel>();

    private ArrayList<TcpMessageReader>m_activitys = new ArrayList<TcpMessageReader>();

    public static SocketController getInstance() {
        return ourInstance;
    }

    private SocketController() {
    }

    public void setUserName( String t_username){
        m_userName = t_username;
    }

    public String getuserName()
    {
        return m_userName;
    }

    public synchronized ArrayList<String> getReceivtMessages()
    {
        return m_socket.getReceivedList();
    }

    public synchronized void setHasMsgs(boolean t_hasMessages)
    {
        m_hasMessages = t_hasMessages;
    }

    public synchronized boolean gethasMsgs()
    {
        return m_hasMessages;
    }

    public synchronized  MessengerTcpSocket getSocket()
    {
        return m_socket;
    }

    public synchronized void setUserID(String t_userID)
    {
        m_userID = t_userID;
    }

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

    public synchronized void addTcMessageReader(TcpMessageReader t_reader)
    {
        m_activitys.add(t_reader);
    }

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

    public synchronized void processMessage()
    {
        for( int i = 0; i < m_activitys.size(); i++)
        {
            if( m_activitys.get(i) != null) {
                m_activitys.get(i).readMessages(m_socket.getReceivedList());
            }
        }
    }

    public synchronized void setIpAndStartSocket(String t_ip,MainActivity t_activity)
    {
        m_socket = new MessengerTcpSocket(t_activity);
        m_socket.start();
        m_socket.setIpAndConnect(t_ip);

    }

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

    public void removeChat(String userName) {
        for( int i = 0; i < m_chatModels.size(); i++)
        {
            if( m_chatModels.get(i).getUserNameTo().equals(userName))
            {
                m_chatModels.remove(i);
            }
        }
    }

    public void startChatWorkerThreath() {
        //m_chatWorker.start();
    }
}

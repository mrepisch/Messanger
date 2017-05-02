package main.nerd.messenger;

import java.util.ArrayList;

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

    public synchronized void setIpAndStartSocket(String t_ip,MainActivity t_activity)
    {

        m_socket = new MessengerTcpSocket(t_activity);
        m_socket.setIpAndConnect(t_ip);
        m_socket.start();


    }

}

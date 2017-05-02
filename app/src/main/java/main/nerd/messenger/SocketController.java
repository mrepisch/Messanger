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

    private String m_ipAdress;

    public static SocketController getInstance() {
        return ourInstance;
    }

    private SocketController() {
        m_socket = new MessengerTcpSocket();
        m_socket.start();
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

            for (int i = 0; i < m_socket.getReceivedList().size(); i++) {
                if (t_msg.equals(m_socket.getReceivedList().get(i))) {
                    m_socket.getReceivedList().remove(i);
                }
            }

    }

    public void setIpAdress(String ipAdress) {
        m_ipAdress = ipAdress;
    }

    public void setIpAdressIntoSocket(String ipAdress) {
        m_socket.setIp(ipAdress);
    }
}

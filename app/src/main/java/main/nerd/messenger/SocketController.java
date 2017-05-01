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

    public static SocketController getInstance() {
        return ourInstance;
    }

    private SocketController() {
        m_socket = new MessengerTcpSocket();
        m_socket.start();
    }

    public synchronized ArrayList<String> getReceivtMessages()
    {
        return m_socket.getReceivt();
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


}

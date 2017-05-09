package tcp.nerd.messenger;

import android.os.NetworkOnMainThreadException;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import main.nerd.messenger.MainActivity;
import main.nerd.messenger.SocketController;


/**
 * Created by bblans on 25.04.2017.
 */

public class MessengerTcpSocket extends Thread{

    private static String S_SERVERIP = "172.0.0.1";
    //private static String S_SERVERIP = "10.0.2.2";
    private static int S_SERVERPORT = 6699;

    private Socket m_socket;

    private BufferedReader m_reader;

    private PrintWriter m_writer;

    private boolean m_isRunning;

    ArrayList<String>m_messagesToSend;

    ArrayList<String>m_receivt;

    MainActivity m_activity;

    public MessengerTcpSocket(MainActivity t_activity)
    {
        super("socket");
        m_activity = t_activity;
        m_messagesToSend = new ArrayList<String>();
        m_receivt = new ArrayList<String>();
        m_isRunning = true;
    }


    public void connect() {
        //here you must put your computer's IP address.
        InetAddress serverAddr = null;
        try {
            serverAddr = InetAddress.getByName(S_SERVERIP);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            m_receivt.add("ConnectionFAIL");
            SocketController.getInstance().setHasMsgs(true);
            return;
        } catch( NetworkOnMainThreadException e)
        {
            m_receivt.add("ConnectionFAIL");
            SocketController.getInstance().setHasMsgs(true);
            return;
        }
        Log.e("TCP Client", "C: Connecting...");
        Log.e("SERVERIP:",serverAddr.toString());

        //create a socket to make the connection with the server
        try {
            m_socket = new Socket(serverAddr, S_SERVERPORT);
        } catch (IOException e) {
            e.printStackTrace();
        }catch( NetworkOnMainThreadException e)
        {
            m_receivt.add("ConnectionFAIL");
            SocketController.getInstance().setHasMsgs(true);
            return;
        }

        try {

            m_reader = new BufferedReader(new InputStreamReader(m_socket.getInputStream()));
            m_writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(m_socket.getOutputStream())), true);
            m_receivt.add("ConnectionOK");
            SocketController.getInstance().setHasMsgs(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void addMessage(String t_message)
    {
        m_receivt.add(t_message);
    }

    public synchronized  void sendMessage(String t_msg)
    {
        m_messagesToSend.add(t_msg);
    }

    public synchronized ArrayList<String>getReceivedList()
    {
        return m_receivt;
    }

    public synchronized void closeConnection()
    {
        try {
            m_socket.close();
            m_socket = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch( NetworkOnMainThreadException e)
        {
            m_receivt.add("ConnectionFAIL");
            SocketController.getInstance().setHasMsgs(true);
            return;
        }
    }

    public void run() {
            while (m_isRunning) {
                if( m_socket == null || m_socket.isConnected() == false) {
                    connect();
                }
                if( m_socket != null && m_socket.isConnected()) {
                    m_activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            m_activity.enableButtons(true);
                        }
                    });
                }
                else
                {
                    m_activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            m_activity.enableButtons(false);
                        }
                    });
                }
                if (m_socket != null && m_socket.isConnected()) {

                        for (int i = 0; i < m_messagesToSend.size(); i++) {
                            m_writer.println(m_messagesToSend.get(i) + "\n");
                            m_writer.flush();
                            m_messagesToSend.clear();
                        }

                        try {
                            if( m_socket.isConnected() ) {
                                while (m_socket.getInputStream().available() > 0) {
                                    String fromServer = m_reader.readLine();
                                    if (fromServer.length() > 0) {
                                        m_receivt.add(fromServer);
                                        SocketController.getInstance().setHasMsgs(true);
                                    }

                                }
                            }
                            SocketController.getInstance().processMessage();
                            sleep(10);

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            if (m_socket != null) {
                closeConnection();
            }
        }



    public synchronized void setIpAndConnect(String ip){
        S_SERVERIP = ip;
    }

    public void setIsRunning(boolean t_isRunning){
        m_isRunning = t_isRunning;
    }

    public synchronized boolean getIsConnected()
    {
        if( m_socket != null) {
            return m_socket.isConnected();
        }
        return false;
    }
}
/*

*/
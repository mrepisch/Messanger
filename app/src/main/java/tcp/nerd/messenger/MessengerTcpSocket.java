package tcp.nerd.messenger;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
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

    private static String S_SERVERIP = "172.16.2.148";
    //private static String S_SERVERIP = "10.0.2.2";
    private static int S_SERVERPORT = 6996;

    private Socket m_socket;



    private BufferedReader m_reader;

    private PrintWriter m_writer;

    private boolean m_isRunning;

    ArrayList<String>m_messagesToSend;

    ArrayList<String>m_receivt;

    public MessengerTcpSocket()
    {
        super("socket");

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
        }
        Log.e("TCP Client", "C: Connecting...");

        //create a socket to make the connection with the server
        try {
            m_socket = new Socket(serverAddr, S_SERVERPORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            m_reader = new BufferedReader(new InputStreamReader(m_socket.getInputStream()));
            m_writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(m_socket.getOutputStream())), true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized  void sendMessage(String t_msg)
    {
        m_messagesToSend.add(t_msg);
    }

    public synchronized ArrayList<String>getReceivedList()
    {
        return m_receivt;
    }

    public void closeConnection()
    {
        try {
            m_socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run()
    {
        connect();
        while(m_isRunning)
        {
            for( int i = 0; i < m_messagesToSend.size(); i++)
            {
                m_writer.println(m_messagesToSend.get(i));
                m_writer.flush();
                m_messagesToSend.clear();
            }
            try {

                while( m_socket.getInputStream().available() > 0 )
                {
                    String fromServer = m_reader.readLine();
                    if( fromServer.length() > 0) {
                        m_receivt.add(fromServer);
                        SocketController.getInstance().setHasMsgs(true);
                    }

                }

                    sleep(10);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean test = true;
        }
        closeConnection();
    }

    public void setIp(String ip){
        S_SERVERIP = ip;
    }


}
/*

*/
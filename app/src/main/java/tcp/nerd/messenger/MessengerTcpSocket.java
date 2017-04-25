package tcp.nerd.messenger;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import main.nerd.messenger.MainActivity;

import static main.nerd.messenger.R.id.msg;

/**
 * Created by bblans on 25.04.2017.
 */

public class MessengerTcpSocket extends Thread{

    private static String S_SERVERIP = "172.16.2.148";
    //private static String S_SERVERIP = "10.0.2.2";
    private static int S_SERVERPORT = 6996;

    private Socket m_socket;

    private MainActivity m_act;

    public MessengerTcpSocket(MainActivity t_act)
    {
        super("socket");
        m_act = t_act;
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

        PrintWriter a_writer = null;
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(m_socket.getInputStream()));
            a_writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(m_socket.getOutputStream())), true);
            a_writer.println("HALLO SERVER");
            a_writer.flush();
            String fromServer = null;
            while ((fromServer = input.readLine()) != null) {
                m_act.setText(fromServer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public void run()
    {
        connect();
    }


}

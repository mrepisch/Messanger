package tcp.nerd.messenger;

import java.util.ArrayList;

import main.nerd.messenger.SocketController;
import main.nerd.messenger.TcpMessageReader;
import main.nerd.messenger.main.nerd.messenger.chat.ChatModel;

/**
 * Created by bblans on 02.05.2017.
 */

public class ChatWorkerThreat extends Thread  {

    private boolean m_keepUpdating = true;

    public ChatWorkerThreat()
    {
        setName("workerthread");
    }

    public void run()
    {
        while( m_keepUpdating) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (SocketController.getInstance().gethasMsgs() == false) {
            }
            ArrayList<String> a_msgs = SocketController.getInstance().getReceivtMessages();

        }
    }
}

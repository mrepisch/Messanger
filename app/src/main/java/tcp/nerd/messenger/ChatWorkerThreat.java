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
            String a_msgToDelete = null;
            for (String a_msg : a_msgs) {
                if (a_msg.contains("Message")) {
                    a_msgToDelete = a_msg;
                    String[] a_split = a_msg.split(":");
                    if( a_split.length >= 4)
                    {
                        ChatModel a_model = SocketController.getInstance().getChat(a_split[1]);
                        if( a_model != null)
                        {
                            a_model.addMessages(a_split[2],a_split[1],a_split[3]);
                        }

                    }

                }
            }
            SocketController.getInstance().removeMsg(a_msgToDelete);
        }
    }
}

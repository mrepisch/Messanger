package main.nerd.messenger;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import main.nerd.messenger.main.nerd.messenger.chat.ChatModel;
import main.nerd.messenger.main.nerd.messenger.chat.ContactAdapter;
import main.nerd.messenger.main.nerd.messenger.chat.MessageAdapter;


public class ChatActivity extends FragmentActivity implements  TcpMessageReader{

    private  ChatModel m_model = null;
    private boolean m_keepUpdating = true;
    ListView listView;
    MessageAdapter adapter;

    /**
     * On create function is called as soon as the activity is started
     * Starts onclickListener for send button
     * Sends message to server
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        SocketController.getInstance().addTcMessageReader(this);
        String a_userName = getIntent().getStringExtra("username");

        m_model = SocketController.getInstance().getChat(a_userName);
        final EditText a_msg = (EditText)findViewById(R.id.msg);
        listView = (ListView) findViewById(R.id.msgview);
        adapter = new MessageAdapter(ChatActivity.this, m_model.getMessages());
        listView.setAdapter(adapter);


        Button a_sendBtn = (Button)findViewById(R.id.send);
        a_sendBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String a_msgStr = a_msg.getText().toString().replace("\n","");
                SocketController.getInstance().getSocket().sendMessage("Message:"+ SocketController.getInstance().getuserName()+":"+
                        m_model.getUserNameTo()+":"+a_msgStr );

                        m_model.addMessages(a_msgStr,SocketController.getInstance().getuserName());
                        a_msg.setText("");



            }
        });
        new Thread()
        {
            public void run()
            {
                while(m_keepUpdating) {
                    try {
                        sleep(5000);
                        ChatActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ChatActivity.this.loadChat();
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        loadChat();
    }

    /**
     * Loads chat from adapter
     */
    private synchronized void loadChat()
    {
        Log.w("FUNCTION CALL","LOAD CHAT");
        if( m_model != null)
        {
            Log.w("LOADING OR REFRESHING CHAT","");
            adapter.swapItems(m_model.getMessages());
        }
        else
        {
            Log.w("m_model in ChatActivity is null","YOU FUCKING FAILD" );
        }
    }

    /**
     * Reads Messages from TCP
     * Starts functions based on Message content
     * Displays received messages
     * @param t_messages Array of messages
     */
    @Override
    public void readMessages(final ArrayList<String> t_messages) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String a_msgToDelete = null;
                for (String a_msg : t_messages) {
                    if (a_msg.contains("Message")) {
                        a_msgToDelete = a_msg;
                        String[] a_split = a_msg.split(":");

                        if( a_split.length >= 3)
                        {
                            if( m_model != null)
                            {
                                Log.w("Message Receivt from Server :",a_split[2]);
                                Log.w("adding msg",a_split[2]);
                                m_model.addMessages(a_split[2],a_split[1]);



                            }else
                            {
                                Log.e("MODEL IS NULL ","YOU FUCKING FAILD");
                            }
                        }
                    }
                }
                SocketController.getInstance().removeMsg(a_msgToDelete);
                loadChat();
            }
        });

    }

    /**
     * Returns name for TcpMessageReader interface
     * @return "chat" which is the name of this Activity
     */
    @Override
    public String getName() {
        return "chat";
    }

    /**
     * Removes messageReader from SocketController
     */
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        SocketController.getInstance().removeMessageReader(getName());
    }
}

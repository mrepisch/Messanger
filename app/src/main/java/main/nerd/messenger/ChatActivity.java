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


public class ChatActivity extends FragmentActivity {

    private ChatModel m_model = null;
    private boolean m_keepUpdating = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        String a_userName = getIntent().getStringExtra("username");
        m_model = SocketController.getInstance().getChat(a_userName);
        final EditText a_msg = (EditText)findViewById(R.id.msg);
        Button a_sendBtn = (Button)findViewById(R.id.send);
        a_sendBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String a_msgStr = a_msg.getText().toString().replace("\n","");
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

    void loadChat()
    {
        if( m_model != null)
        {
            MessageAdapter adapter = new MessageAdapter(this, m_model.getMessages());

            ListView listView = (ListView) findViewById(R.id.msgview);
            listView.setAdapter(adapter);

        }
    }




}

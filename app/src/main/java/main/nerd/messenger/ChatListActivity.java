package main.nerd.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import main.nerd.messenger.main.nerd.messenger.chat.ContactAdapter;
import main.nerd.messenger.main.nerd.messenger.chat.ContactAvaiableAdapter;
import main.nerd.messenger.main.nerd.messenger.chat.ContactXmlModel;


public class ChatListActivity extends AppCompatActivity implements TcpMessageReader{

    private ArrayList<ContactXmlModel>m_contacts  = new ArrayList<ContactXmlModel>();
    private boolean m_keepUpdating = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlist);
        SocketController.getInstance().startChatWorkerThreath();
        Button a_searchBtn = (Button) findViewById(R.id.searchbtn);
        final EditText a_userNameToSearch = (EditText)findViewById(R.id.searchPerson);
        a_searchBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!a_userNameToSearch.getText().toString().isEmpty()) {
                    SocketController.getInstance().getSocket().sendMessage("Contact:search_for_username:" + a_userNameToSearch.getText().toString());
                    ArrayList<String> a_msgs = SocketController.getInstance().getReceivtMessages();
                    String a_msgToDelete = null;
                    for (String a_msg : a_msgs) {
                        if (a_msg.contains("Contact:users;")) {
                            String[] a_userSplit = a_msg.split(";");
                            ArrayList<ContactXmlModel> a_foundUsers = new ArrayList<ContactXmlModel>();
                            if (a_userSplit.length > 1) {
                                for (int i = 1; i < a_userSplit.length; i++) {
                                    String[] a_userData = a_userSplit[i].split(":");
                                    if (a_userData.length == 2) {
                                        ContactXmlModel a_model = new ContactXmlModel();
                                        a_model.setUserID(a_userData[1]);
                                        a_model.setUserName(a_userData[0]);
                                        a_foundUsers.add(a_model);
                                    }
                                }
                                ContactAdapter adapter = new ContactAdapter(ChatListActivity.this, ChatListActivity.this, a_foundUsers);

                                ListView listView = (ListView) findViewById(R.id.contactList);
                                listView.setAdapter(adapter);

                            }
                            a_msgToDelete = a_msg;
                        }
                    }
                    SocketController.getInstance().removeMsg(a_msgToDelete);
                }
            }
        });
        keepUpdated();

    }


    private void addContactToList(ContactXmlModel t_model)
    {
        m_contacts.add(t_model);
        ContactXmlModel.writeNewContact(this,m_contacts, SocketController.getInstance().getuserName());
    }

    public void addUser(ContactXmlModel t_model)
    {
        if( t_model !=null )
        {
            m_contacts.add(t_model);
            ContactXmlModel.writeNewContact(this,m_contacts,SocketController.getInstance().getuserName());
            Intent a_itent = getIntent();
            finish();
            startActivity(a_itent);
        }
    }

    public void removeUser(String userName)
    {
        for( int i= 0; i < m_contacts.size(); i++)
        {
            if( m_contacts.get(i).getUserName().equals(userName)){
                m_contacts.remove(i);
            }
        }
        ContactXmlModel.writeNewContact(this,m_contacts, SocketController.getInstance().getuserName());
    }

    private void keepUpdated(){
        final ChatListActivity activity = this;
        new Thread()
        {

            public void run()
            {
                while( m_keepUpdating) {
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    while (SocketController.getInstance().gethasMsgs() == false) {
                    }
                    ArrayList<String> a_msgs = SocketController.getInstance().getReceivtMessages();
                    String a_msgToDelete = null;
                    for (String a_msg : a_msgs) {
                        if (a_msg.contains("UpdateContacts")) {
                            a_msgToDelete = a_msg;
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                }
                            });
                        }
                    }
                    SocketController.getInstance().removeMsg(a_msgToDelete);
                }
            }
        }.start();
    }


    final boolean a_hasDataChanged = false;
    @Override
    public void readMessages(final ArrayList<String> t_messages) {


        m_contacts = ContactXmlModel.readContactXml(this,SocketController.getInstance().getuserName());
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < m_contacts.size(); i++) {
                    SocketController.getInstance().getSocket().sendMessage("Contact:search_for_userID:" + m_contacts.get(i).getUserName());
                    ArrayList<String> a_msgs = SocketController.getInstance().getReceivtMessages();
                    String a_msgToDelete = null;
                    Log.w("Size of the messa", String.valueOf(a_msgs.size()));
                    for (String a_msg : a_msgs) {
                        Log.w("Test", "TEST");
                        if (a_msg.contains("Contact:search_for_userID:")) {
                            a_msgToDelete = a_msg;
                            String[] a_split = a_msg.split(":");
                            if (a_split.length >= 3) {

                                m_contacts.get(i).setUserID(a_split[2]);

                                if (a_split[3].equals("online")) {
                                    m_contacts.get(i).setIsOnline(true);
                                } else {
                                    m_contacts.get(i).setIsOnline(false);
                                }
                            }

                        }
                    }
                    SocketController.getInstance().removeMsg(a_msgToDelete);
                }
            }
        });
        if( a_hasDataChanged )
        {
            ContactXmlModel.writeNewContact(this , m_contacts,SocketController.getInstance().getuserName());
        }
        ContactAvaiableAdapter adapter = new ContactAvaiableAdapter(ChatListActivity.this, m_contacts);
        ListView listView = (ListView) findViewById(R.id.contactList);
        listView.setAdapter(adapter);
    }

    @Override
    public String getName() {
        return "chatlist";
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        SocketController.getInstance().removeMessageReader(getName());
    }
}

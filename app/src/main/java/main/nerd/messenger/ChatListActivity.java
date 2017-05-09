package main.nerd.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.net.Socket;
import java.util.ArrayList;

import main.nerd.messenger.main.nerd.messenger.chat.ChatModel;
import main.nerd.messenger.main.nerd.messenger.chat.ContactAdapter;
import main.nerd.messenger.main.nerd.messenger.chat.ContactAvaiableAdapter;
import main.nerd.messenger.main.nerd.messenger.chat.ContactXmlModel;


public class ChatListActivity extends AppCompatActivity implements TcpMessageReader{

    private ArrayList<ContactXmlModel>m_contacts  = new ArrayList<ContactXmlModel>();
    private boolean m_keepUpdating = true;
    //Status 0 = in kontakliste
    //Status 1 = in gefunde Benutzerliste.
    private int m_state = 0;

    /**
     * On create function is called as soon as the activity is started
     * Updates ChatList
     * Adds onClickListener for when a contact is searched
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlist);


        SocketController.getInstance().addTcMessageReader(this);
        Button a_searchBtn = (Button) findViewById(R.id.searchbtn);
        final EditText a_userNameToSearch = (EditText)findViewById(R.id.searchPerson);
        readContactList();
        a_searchBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!a_userNameToSearch.getText().toString().isEmpty()) {
                    SocketController.getInstance().getSocket().sendMessage("Contact:search_for_username:" + a_userNameToSearch.getText().toString());
                    m_state = 1;

                }
            }
        });
        keepUpdated();

    }

    /**
     * Reads xml files on phone to create according contact entries
     */
    public void readContactList()
    {
        m_contacts = ContactXmlModel.readContactXml(this,SocketController.getInstance().getuserName());
        for( int i = 0; i < m_contacts.size(); i++) {
            SocketController.getInstance().getSocket().sendMessage("Contact:search_for_userID:" + m_contacts.get(i).getUserName());
        }
        updateContactList();
    }

    /**
     * Updates the contact list
     */
    private void updateContactList()
    {
        ContactAvaiableAdapter adapter = new ContactAvaiableAdapter(ChatListActivity.this, m_contacts);
        ListView listView = (ListView) findViewById(R.id.contactList);
        listView.setAdapter(adapter);
    }

    /**
     * Function to check if a username is allready in the contactlist
     * @param t_username
     * @return
     */
    private boolean checkIfAlreadyInList(String t_username)
    {
        boolean r_isFound = false;
        for(ContactXmlModel a_model : m_contacts)
        {
            if( a_model.getUserName() == t_username)
            {
                r_isFound = true;
            }
        }
        return r_isFound;
    }

    /**
     * Adds user to contact list
     * Creates xml file for that contact with ContactXmlModel
     * @param t_model the model with which the user is added
     */
    public void addUser(ContactXmlModel t_model)
    {
        if( t_model !=null )
        {
            if( !checkIfAlreadyInList(t_model.getUserName())) {
                m_contacts.add(t_model);
                ContactXmlModel.writeNewContact(this, m_contacts, SocketController.getInstance().getuserName());
                updateContactList();
            }
            m_state = 0;
        }
    }

    /**
     * Removes user from contacts
     * This is done by deleting the xml file
     * @param userName the username of the user that has to be deleted
     */
    public void removeUser(String userName)
    {
        for( int i= 0; i < m_contacts.size(); i++)
        {
            if( m_contacts.get(i).getUserName().equals(userName)){
                m_contacts.remove(i);
            }
        }
        ContactXmlModel.writeNewContact(this,m_contacts, SocketController.getInstance().getuserName());
        updateContactList();
    }



    /**
     * Keeps updating the contact list
     */
    private void keepUpdated(){
        new Thread()
        {
            public void run()
            {
                while( m_keepUpdating ) {
                    try {
                        sleep(1000);
                        for (int i = 0; i < m_contacts.size(); i++) {
                                SocketController.getInstance().getSocket().sendMessage("Contact:search_for_userID:" + m_contacts.get(i).getUserName());

                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();
    }

    /**
     * Reads Messages from TCP
     * Starts functions based on Message content
     * Shows users based on search/message
     * @param t_messages Array of messages
     */
    @Override
    public synchronized void readMessages(final ArrayList<String> t_messages) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String a_msgToDelete = null;
                for( String a_msg: t_messages)
                {

                    if (a_msg.contains("Contact:search_for_userID:") && m_state == 0) {

                        a_msgToDelete = a_msg;
                        String[] a_split = a_msg.split(":");
                        if (a_split.length >= 4) {
                            String a_userName = a_split[2];
                            for( int i = 0; i < m_contacts.size(); i++) {
                                //m_contacts.get(i).setUserID(a_split[2]);
                                ContactXmlModel a_model = m_contacts.get(i);
                                if( a_model.getUserName().equals(a_userName)) {
                                    if (a_split[4].equals("online")) {
                                        m_contacts.get(i).setIsOnline(true);
                                    } else {
                                        m_contacts.get(i).setIsOnline(false);
                                    }
                                    updateContactList();
                                }
                            }
                        }
                    }
                    if (a_msg.contains("Contact:users;") && m_state == 1) {
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


        });

    }

    /**
     * Returns name for TcpMessageReader interface
     * @return "chatlist" which is the name of this Activity
     */
    @Override
    public String getName() {
        return "chatlist";
    }

    /**
     * Removes messageReader from SocketController
     */
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.w("DESTRY CHAT LIST ACTIVITY","TRUE");
        m_keepUpdating = false;
        SocketController.getInstance().getSocket().closeConnection();
        SocketController.getInstance().removeMessageReader(getName());

    }
    /**
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {

            Log.w("KLICK ON BACK","TRUE");
            if(m_state == 1)
            {
                m_state = 0;
            }
            else {
                SocketController.getInstance().getSocket().sendMessage("Disconnect:"+SocketController.getInstance().getuserName());
                this.finish();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}

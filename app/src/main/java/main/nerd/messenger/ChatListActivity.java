package main.nerd.messenger;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

import main.nerd.messenger.main.nerd.messenger.chat.ContactAdapter;
import main.nerd.messenger.main.nerd.messenger.chat.ContactAvaiableAdapter;
import main.nerd.messenger.main.nerd.messenger.chat.ContactXmlModel;


public class ChatListActivity extends AppCompatActivity {

    private ArrayList<ContactXmlModel>m_contacts  = new ArrayList<ContactXmlModel>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlist);
        readContactList();
        Button a_searchBtn = (Button) findViewById(R.id.searchbtn);
        final EditText a_userNameToSearch = (EditText)findViewById(R.id.searchPerson);
        a_searchBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!a_userNameToSearch.getText().toString().isEmpty()) {
                    SocketController.getInstance().getSocket().sendMessage("Contact:search_for_username:" + a_userNameToSearch.getText().toString());
                    while (SocketController.getInstance().gethasMsgs() == false) {
                    }
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

    }


    private void addContactToList(ContactXmlModel t_model)
    {
        m_contacts.add(t_model);
        ContactXmlModel.writeNewContact(this,m_contacts, SocketController.getInstance().getuserName());


    }

    private void readContactList()
    {
        m_contacts = ContactXmlModel.readContactXml(this,SocketController.getInstance().getuserName());
        boolean a_hasDataChanged = false;
        for( int i = 0; i < m_contacts.size(); i++)
        {
            if( m_contacts.get(i).getUserID() == null)
            {
                SocketController.getInstance().getSocket().sendMessage("Contact:search_for_userID:"+m_contacts.get(i).getUserName());
                while (SocketController.getInstance().gethasMsgs() == false) {}
                ArrayList<String>a_msgs =  SocketController.getInstance().getReceivtMessages();
                String a_msgToDelete;
                for( String a_msg : a_msgs)
                {
                    if( a_msg.contains("Contact:search_for_userID:"))
                    {
                        a_msgToDelete = a_msg;
                        String[] a_split = a_msg.split(":");
                        if( a_split.length >= 3) {
                            if (m_contacts.get(i).getUserName().equals(a_split[2])) {
                                m_contacts.get(i).setUserID(a_split[3]);
                                a_hasDataChanged = true;
                            }
                        }
                    }
                }
            }
        }
        if( a_hasDataChanged )
        {
            ContactXmlModel.writeNewContact(this,m_contacts,SocketController.getInstance().getuserName());
        }
        ContactAvaiableAdapter adapter = new ContactAvaiableAdapter(ChatListActivity.this, m_contacts);
        ListView listView = (ListView) findViewById(R.id.contactList);
        listView.setAdapter(adapter);
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
        readContactList();
    }



    



}

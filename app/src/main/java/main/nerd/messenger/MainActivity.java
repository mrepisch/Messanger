package main.nerd.messenger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;

import tcp.nerd.messenger.MessengerTcpSocket;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button a_registerButton = (Button)findViewById(R.id.registerbtn);
        a_registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startRegisterActivity();
            }
        });

        Button ipChangeButton = (Button) findViewById(R.id.ipbtn);
        ipChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText ipAdress =  (EditText) findViewById(R.id.ip);
                String s = ipAdress.getText().toString();

                s.replace("\n", "");

                SocketController.getInstance().getSocket().setIpAndConnect(s);
                while(SocketController.getInstance().gethasMsgs() == false) {}
                ArrayList<String>a_msgs = SocketController.getInstance().getReceivtMessages();
                String a_msgToDelete = null;
                for( String a_msg : a_msgs)
                {
                    if( a_msg.equals("ConnectionOK"))
                    {
                        a_msgToDelete = a_msg;
                        enableButtons();
                    }
                }
                SocketController.getInstance().removeMsg(a_msgToDelete);
            }
        });

        final Button a_loginButton =(Button) findViewById(R.id.loginbtn);
        a_loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText a_userNameEdit = (EditText)findViewById(R.id.username);
                EditText a_passwordEdit = (EditText)findViewById(R.id.password);
                String a_username = a_userNameEdit.getText().toString();
                String a_password = a_passwordEdit.getText().toString();
                SocketController.getInstance().getSocket().sendMessage("Login:username:"+a_username+":pw:"+a_password);
                while(SocketController.getInstance().gethasMsgs() == false) {}
                ArrayList<String>a_msgs = SocketController.getInstance().getReceivtMessages();
                String a_msgToDelete = null;
                for( String a_msg : a_msgs)
                {
                    if( a_msg.contains("Login:true"))
                    {
                        String[] a_split = a_msg.split(":");
                        if( a_split.length >= 3) {
                            SocketController.getInstance().setUserID(a_split[3]);
                            SocketController.getInstance().setUserName(a_username);
                            a_msgToDelete = a_msg;
                            startContactListActivity();
                        }
                    }
                    else if( a_msg.equals("Login:false"))
                    {
                        TextView a_loginResponse = (TextView)findViewById(R.id.loginResponse);
                        a_loginResponse.setText("Falscher Benutzername oder Passwort");
                        a_msgToDelete = a_msg;
                    }
                }
                SocketController.getInstance().removeMsg(a_msgToDelete);
            }
        });
    }

    private synchronized void startContactListActivity()
    {
        Intent a_contactListActivity = new Intent( MainActivity.this,ChatListActivity.class );
        MainActivity.this.startActivity(a_contactListActivity);
    }

    private void startRegisterActivity()
    {
        Intent a_registerActivity = new Intent( MainActivity.this,RegisterActivity.class );
        MainActivity.this.startActivity(a_registerActivity);
    }

    public void enableButtons() {
        Button loginBtn = (Button) findViewById(R.id.loginbtn);
        Button registerBtn = (Button) findViewById(R.id.registerbtn);

        loginBtn.setEnabled(true);
        registerBtn.setEnabled(true);
    }

}

package main.nerd.messenger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import tcp.nerd.messenger.MessengerTcpSocket;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button a_loginButton =(Button) findViewById(R.id.loginbtn);
        a_loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText a_userNameEdit = (EditText)findViewById(R.id.username);
                EditText a_passwordEdit = (EditText)findViewById(R.id.password);
                String a_username = a_userNameEdit.getText().toString();
                String a_password = a_passwordEdit.getText().toString();
                SocketController.getInstance().getSocket().sendMessage("Login:username:"+a_username+":pw:"+a_password);
                while(SocketController.getInstance().gethasMsgs() == false)
                {

                }
                a_userNameEdit.setText(SocketController.getInstance().getReceivtMessages().get(0));
                /*for( int i = 0; i < a_response.size(); i++)
                {
                    if( a_response.get(i) == "true")
                    {
                        boolean test = false;
                        String somthign = "asd";
                    }
                }*/

            }
        });
    }

    public synchronized  void setText(String text)
    {


    }
}

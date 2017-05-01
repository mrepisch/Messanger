package main.nerd.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final TextView a_username = (TextView) findViewById(R.id.username);
        final TextView a_password = (TextView) findViewById(R.id.password);
        final TextView a_paswordRepeat = (TextView) findViewById(R.id.passwordRepeat);
        final TextView a_error = (TextView)findViewById(R.id.errors);
        Button a_registerBtn = (Button) findViewById(R.id.registerbtn);
        a_registerBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String a_pw1 = a_password.getText().toString();

                if (a_password.getText().toString().equals(a_paswordRepeat.getText().toString())) {
                    SocketController.getInstance().getSocket().sendMessage("Register:"+a_username.getText()+":"+a_password.getText());
                    while(SocketController.getInstance().gethasMsgs() == false) {}
                    ArrayList<String> a_msgs = SocketController.getInstance().getReceivtMessages();
                    String a_msgToDelete = null;
                    for( String a_msg : a_msgs)
                    {
                        if( a_msg.equals("Register:sucess"))
                        {
                            a_error.setText("SUCCES");
                            startLoginActivity();
                        }
                        else if( a_msg.equals("Register:faild:user_allready_exist"))
                        {
                            a_error.setText("Benutzername existiert schon");
                        }
                    }

                } else {
                    a_error.setText("Passwörter sind nicht gleich");
                }
            }
        });
    }

    private void startLoginActivity()
    {
        Intent a_contactListActivity = new Intent( RegisterActivity.this,MainActivity.class );
        RegisterActivity.this.startActivity(a_contactListActivity);
    }

}

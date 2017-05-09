package main.nerd.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity implements TcpMessageReader {


    /**
     * On create function is called as soon as the activity is started
     * Starts onclickListener for registration button and checks the filled out field for correctness
     * Registers you if correct, displays error message if not
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final TextView a_username = (TextView) findViewById(R.id.username);
        final TextView a_password = (TextView) findViewById(R.id.password);
        final TextView a_paswordRepeat = (TextView) findViewById(R.id.passwordRepeat);
        final TextView a_error = (TextView)findViewById(R.id.errors);
        Button a_registerBtn = (Button) findViewById(R.id.registerbtn);
        SocketController.getInstance().addTcMessageReader(this);
        a_registerBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if( !a_username.getText().toString().isEmpty() && !a_password.getText().toString().isEmpty() && !a_paswordRepeat.getText().toString().isEmpty()) {
                    if (a_password.getText().toString().equals(a_paswordRepeat.getText().toString())) {
                        SocketController.getInstance().getSocket().sendMessage("Register:" + a_username.getText() + ":" + a_password.getText());
                    } else {
                        a_error.setText("Passwörter sind nicht gleich");
                    }
                }

            }
        });
    }

    /**
     * Starts the Login Activity for when the registration was successful
     */
    private void startLoginActivity()
    {
        Intent a_contactListActivity = new Intent( RegisterActivity.this,MainActivity.class );
        finish();
        RegisterActivity.this.startActivity(a_contactListActivity);
    }

    /**
     * Reads Messages from TCP
     * Starts functions based on Message content
     * @param t_messages Array of messages
     */
    @Override
    public void readMessages(final ArrayList<String> t_messages) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                String a_msgToDelete = null;
                final TextView a_error = (TextView)findViewById(R.id.errors);
                for (String a_msg : t_messages) {
                    if (a_msg.contains("Register:succes")) {
                        a_msgToDelete = a_msg;
                        startLoginActivity();
                    } else if (a_msg.equals("Register:faild:user_allready_exist")) {
                        a_msgToDelete = a_msg;
                        a_error.setText("Benutzername existiert schon");
                    }
                }
                SocketController.getInstance().removeMsg(a_msgToDelete);
            }
        });
    }


    /**
     * Returns name for TcpMessageReader interface
     * @return "register" which is the name of this Activity
     */
    @Override
    public String getName() {
        return "register";
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

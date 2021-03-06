package main.nerd.messenger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TcpMessageReader{

    /**
     * On create function is called as soon as the activity is started
     * Starts onclickListener for login and registration button and checks the filled out field for correctness
     * Logs you in or registers you if correct, displays error message if not
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SocketController.getInstance().addTcMessageReader(this);
        final EditText ipAdressText =  (EditText) findViewById(R.id.ip);
        String a_ip = getIpFromConfig(this);
        if( a_ip != null && a_ip.length() > 0) {
            ipAdressText.setText(a_ip);
        }
        Log.w("On Create","1");
        Button ipChangeButton = (Button) findViewById(R.id.ipbtn);
        ipChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ipAdress = ipAdressText.getText().toString();
                writeIntoConfig(ipAdress, MainActivity.this);
                setIp(ipAdress);
            }
        });


        Button a_registerButton = (Button)findViewById(R.id.registerbtn);
        a_registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startRegisterActivity();
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

            }
        });
    }

    /**
     * Starts the contactListActivity when login succeeds
     */
    private synchronized void startContactListActivity()
    {
        Intent a_contactListActivity = new Intent( MainActivity.this,ChatListActivity.class );
        MainActivity.this.startActivity(a_contactListActivity);
    }

    /**
     * Starts registerActivity when register button is pressed
     */
    private void startRegisterActivity()
    {
        Intent a_registerActivity = new Intent( MainActivity.this,RegisterActivity.class );
        MainActivity.this.startActivity(a_registerActivity);
    }

    /**
     * Enables/disables the login and register buttons based on connection to server
     * @param t_enable decides if buttons should be enabled or disabled
     */
    public void enableButtons(boolean t_enable) {
        Button loginBtn = (Button) findViewById(R.id.loginbtn);
        Button registerBtn = (Button) findViewById(R.id.registerbtn);

        loginBtn.setEnabled(t_enable);
        registerBtn.setEnabled(t_enable);
    }

    /**
     * Sets ip address based on entered one
     * @param ipAdress ip address that has to be set
     */
    public void setIp(String ipAdress) {

        ipAdress.replace("\n", "");
        SocketController.getInstance().setIpAndStartSocket(ipAdress,this);
    }

    /**
     * Gets the Ip from the config.txt file
     * @param context context
     * @return ipAddress
     */
    public String getIpFromConfig(Context context) {
       String ipAdress = "";
        File file = new File(context.getFilesDir(),"config.txt");
        if( file.exists()) {
            BufferedReader a_reader = null;
            try {
                a_reader = new BufferedReader(new FileReader(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                e.printStackTrace();
            }
            try {
                ipAdress = a_reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ipAdress;
    }

    /**
     * Writes ip address into config file
     * @param ipAdress the ip address that has to be saved
     * @param context context
     */
    public void writeIntoConfig(String ipAdress, Context context) {
        try {
            File a_file = new File(context.getFilesDir(),"config.txt");
            BufferedWriter a_writer =  new BufferedWriter(new FileWriter(a_file));
            a_writer.write(ipAdress);
            a_writer.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     * Reads Messages from TCP
     * Starts functions based on Message content
     * Logs user in if message for that is received
     * @param t_messages Array of messages
     */
    @Override
    public synchronized void readMessages(final ArrayList<String> t_messages) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

            String a_msgToDelete = null;
            for( String a_msg : t_messages)
            {
                if( a_msg.contains("Login:true"))
                {
                    String[] a_split = a_msg.split(":");
                    if( a_split.length >= 3) {
                        SocketController.getInstance().setUserID(a_split[3]);
                        EditText a_userName = (EditText) MainActivity.this.findViewById(R.id.username);
                        SocketController.getInstance().setUserName(a_userName.getText().toString());
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

    /**
     * Returns name for TcpMessageReader interface
     * @return "main" which is the name of this Activity
     */
    @Override
    public String getName() {
        return "main";
    }

    /**
     * Removes messageReader from SocketController
     */
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.w("DESTROY MAINACTIVITY","TRUE");


        SocketController.getInstance().removeMessageReader(getName());
        SocketController.getInstance().getSocket().closeConnection();
        System.exit(0);
    }


    /**
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            SocketController.getInstance().getSocket().sendMessage("Disconnect:"+SocketController.getInstance().getuserName());
            Log.w("KLICK ON BACK","TRUE");
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Enables/disables login/register buttons after restart
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.w("BLUBB","");
        if( SocketController.getInstance().getSocket() != null) {
            if (SocketController.getInstance().getSocket().getIsConnected()) {
                enableButtons(true);
            } else {
                enableButtons(false);
            }
        }
    }
}

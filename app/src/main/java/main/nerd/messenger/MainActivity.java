package main.nerd.messenger;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tcp.nerd.messenger.MessengerTcpSocket;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText ipAdressText =  (EditText) findViewById(R.id.ip);
        String a_ip = getIpFromConfig(this);
        if( a_ip.length() > 0) {
            ipAdressText.setText(a_ip);
        }

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

    public void setIp(String ipAdress) {

        ipAdress.replace("\n", "");
        SocketController.getInstance().setIpAndStartSocket(ipAdress,this);



    }

    public String getIpFromConfig(Context context) {
       String ipAdress = "";
        File file = new File(context.getFilesDir(),"config.txt");
        BufferedReader a_reader = null;
        try {
            a_reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();e.printStackTrace();
        }
        try {
            ipAdress =a_reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ipAdress;
    }

    public void writeIntoConfig(String ipAdress, Context context) {
        try {
            /*utputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("ipconfig.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(ipAdress);
            outputStreamWriter.close();*/
            File a_file = new File(context.getFilesDir(),"config.txt");
            BufferedWriter a_writer =  new BufferedWriter(new FileWriter(a_file));
            a_writer.write(ipAdress);
            a_writer.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

}

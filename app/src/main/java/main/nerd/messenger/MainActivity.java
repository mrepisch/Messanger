package main.nerd.messenger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import tcp.nerd.messenger.MessengerTcpSocket;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MessengerTcpSocket a_messengerSocket = new MessengerTcpSocket(this);
        a_messengerSocket.start();
    }

    public synchronized  void setText(String text)
    {
        TextView a_text =(TextView) findViewById(R.id.msg);
        a_text.setText(text);

    }
}

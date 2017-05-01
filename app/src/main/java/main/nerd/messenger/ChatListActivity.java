package main.nerd.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class ChatListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addChatsToList();
    }

    private void addChatsToList() {
        ListView chats = (ListView) findViewById(R.id.chatliste);
        final ArrayList<Chat> allChats = new ArrayList<Chat>();
        for (Chat b : allChats) {

        }
        //hats.setAdapter(chatliste);
        //Definition einer anonymen Klicklistener Klasse
        AdapterView.OnItemClickListener mListClickedHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                String selected = parent.getItemAtPosition(position).toString();

                Toast.makeText(MainActivity.this, selected, Toast.LENGTH_SHORT).show();

                startActivity(intent);
            }
        };

        chats.setOnItemClickListener(mListClickedHandler);
    }
}

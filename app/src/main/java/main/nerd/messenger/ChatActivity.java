package main.nerd.messenger;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


public class ChatActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        /*
        Failed try to import fragment

        // Create a new Fragment to be placed in the activity layout
        MessageIncoming firstFragment = new MessageIncoming();

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        firstFragment.setArguments(getIntent().getExtras());

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.message_incoming, firstFragment).commit();
        */
    }


}

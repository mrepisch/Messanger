package main.nerd.messenger;

import java.util.ArrayList;

/**
 * Created by bblans on 02.05.2017.
 */

public interface TcpMessageReader {

    /**
     * Gets messages and calls functions based on them
     *
     * @param t_messages arrayList of messages
     */
    public void readMessages(ArrayList<String> t_messages);


    /**
     * Gets the name of the activity in which it's called
     * @return name of the activity
     */
    public String getName();
}

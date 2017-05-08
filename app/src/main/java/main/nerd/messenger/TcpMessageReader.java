package main.nerd.messenger;

import java.util.ArrayList;

/**
 * Created by bblans on 02.05.2017.
 */

public interface TcpMessageReader {

    public void readMessages(ArrayList<String> t_messages);

    public String getName();
}

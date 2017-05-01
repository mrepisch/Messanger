package main.nerd.messenger.chat;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import main.nerd.messenger.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MessageIncoming.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MessageIncoming#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageIncoming extends Fragment {

    private static final String ARG_TIME = "time";
    private static final String ARG_CHATTEXT = "chatText";


    private TextView time;
    private TextView chatText;

    private OnFragmentInteractionListener mListener;

    public MessageIncoming() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param time Parameter 1.
     * @param chatText Parameter 2.
     * @return A new instance of fragment MessageIncomingF.
     */
    public static MessageIncoming newInstance(String time, String chatText) {
        MessageIncoming fragment = new MessageIncoming();
        Bundle args = new Bundle();
        args.putString(ARG_TIME, time);
        args.putString(ARG_CHATTEXT, chatText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        time = (TextView) getView().findViewById(R.id.tv_time);
        chatText = (TextView) getView().findViewById(R.id.tv_chat_text);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.message_incoming, container, false);
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

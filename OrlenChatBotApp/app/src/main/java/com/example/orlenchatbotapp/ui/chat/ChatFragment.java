package com.example.orlenchatbotapp.ui.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orlenchatbotapp.R;
import com.example.orlenchatbotapp.adapters.MyAdapter;
import com.example.orlenchatbotapp.model.Message;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    public RecyclerView myRecylerView;
    public List<Message> MessageList;
    public MyAdapter myAdapter;
    public EditText messagetxt;
    public Button send;
    private Socket socket;

    //declare socket objectprivate Socket socket;

    public String nickname;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // get the nickame of the user (to będzie HR)
        nickname = "HR employee";
        MessageList = new ArrayList<>();

        Message m = new Message(nickname, "aaa");
        MessageList.add(m);
        Message n = new Message("Kasia", "aaa");
        MessageList.add(n);
        Message o = new Message(nickname, "aaa");
        MessageList.add(o);
        Message p = new Message("Kasia", "aaa");
        MessageList.add(p);

        // --- brzydkie ale działa ---
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        messagetxt = root.findViewById(R.id.message);
        send = root.findViewById(R.id.send);

        //setting up recyler
        myRecylerView = root.findViewById(R.id.messagelist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getActivity().getApplicationContext());
        myRecylerView.setLayoutManager(mLayoutManager);
        myRecylerView.setItemAnimator(new DefaultItemAnimator());

        // add the new updated list to the dapter
        myAdapter = new MyAdapter(MessageList);

        // notify the adapter to update the recycler view
        myAdapter.notifyDataSetChanged();

        //set the adapter for the recycler view
        myRecylerView.setAdapter(myAdapter);

//------------------- niewazne ---------------------------------------
        try {
            socket = IO.socket("http://yourlocalIPaddress:3000");
            socket.connect();
            socket.emit("join", nickname);
        } catch (URISyntaxException e) {
            e.printStackTrace();

        }

        // message send action
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //retrieve the nickname and the message content and fire the event messagedetectionif(!messagetxt.getText().toString().isEmpty()){
                socket.emit("messagedetection", nickname, messagetxt.getText().toString());

                messagetxt.setText(" ");
            }
        });

        //implementing socket listeners
        socket.on("userjoinedthechat", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];

                        Toast.makeText(getActivity().getApplicationContext(), data, Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        socket.on("userdisconnect", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];

                        Toast.makeText(getActivity().getApplicationContext(), data, Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        socket.on("message", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            //extract data from fired event

                            String nickname = data.getString("senderNickname");
                            String message = data.getString("message");

                            // make instance of message

                            Message m = new Message(nickname, message);


                            //add the message to the messageList

                            MessageList.add(m);

                            // add the new updated list to the dapter
                            myAdapter = new MyAdapter(MessageList);

                            // notify the adapter to update the recycler view

                            myAdapter.notifyDataSetChanged();

                            //set the adapter for the recycler view

                            myRecylerView.setAdapter(myAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }
}
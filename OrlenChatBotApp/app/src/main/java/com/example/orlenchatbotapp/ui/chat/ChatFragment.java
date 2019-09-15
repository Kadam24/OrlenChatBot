package com.example.orlenchatbotapp.ui.chat;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatFragment extends Fragment {

    public RecyclerView myRecylerView;
    public List<Message> MessageList;
    public MyAdapter myAdapter;
    public EditText messagetxt;
    public Button send;
    private Socket socket;

    NotificationManager notificationManager;

    //declare socket objectprivate Socket socket;

    public String nickname;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // get the nickame of the user (to będzie HR)
        nickname = "HR employee";
        MessageList = new ArrayList<>();

        notificationManager =
                (NotificationManager)
                        getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        // --- brzydkie ale działa ---
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        messagetxt = root.findViewById(R.id.message);
        send = root.findViewById(R.id.send);

        //setting up recyler
        myRecylerView = root.findViewById(R.id.messagelist);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getActivity().getApplicationContext());
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
            socket = IO.socket("https://7806b38b.ngrok.io");
            socket.connect();
            socket.emit("join", nickname);
        } catch (URISyntaxException e) {
            e.printStackTrace();

        }

        final String[] sessionId = new String[1];

        socket.on("live-agent-request", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            sessionId[0] = data.getString("sessionId");

                            JSONArray arr = data.getJSONArray("messages");
                            for(int i = 0; i < arr.length(); i++){
                                String messageType = arr.getJSONObject(i).getString("messageType");
                                System.out.println("Message type: " + messageType);
                                String message = arr.getJSONObject(i).getString("message");
                                System.out.println("Message: " + message);

                                if(messageType.equals("request")){
                                    messageType = "Rajesh";
                                }
                                else
                                    messageType = nickname;
                                Calendar cal = Calendar.getInstance();
                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                                Message m = new Message(messageType, message);
                                m.setDate(sdf.format(cal.getTime()));
                                MessageList.add(m);

                                if(i == arr.length() - 1){
                                    sendNotification(message);
                                }

                            }


                            socket.on(sessionId[0], new Emitter.Listener() {
                                @Override
                                public void call(final Object... args) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            String data = (String) args[0];
                                            System.out.println("Message: " + data);

                                            Message m = new Message("Rajesh", data);
                                            Calendar cal = Calendar.getInstance();
                                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                                            m.setDate(sdf.format(cal.getTime()));
                                            // add the new updated list to the dapter
                                            MessageList.add(m);
                                            myAdapter = new MyAdapter(MessageList);

                                            // notify the adapter to update the recycler view
                                            myAdapter.notifyDataSetChanged();

                                            //set the adapter for the recycler view
                                            myRecylerView.setAdapter(myAdapter);
                                        }
                                    });
                                }
                            });
//                            messagetxt = root.findViewById(R.id.message);
   //                         send = root.findViewById(R.id.send);

                            // add the new updated list to the dapter
                            myAdapter = new MyAdapter(MessageList);

                            // notify the adapter to update the recycler view
                            myAdapter.notifyDataSetChanged();

                            //set the adapter for the recycler view
                            myRecylerView.setAdapter(myAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //String message = data.getString("chat message");

                    }
                });
            }
        });


        // message send action
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //retrieve the nickname and the message content and fire the event messagedetectionif(!messagetxt.getText().toString().isEmpty()){
                String message = messagetxt.getText().toString();
                Message m = new Message(nickname, message);
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                m.setDate(sdf.format(cal.getTime()));
                MessageList.add(m);
                socket.emit(sessionId[0], message , nickname);

                // add the new updated list to the dapter
                myAdapter = new MyAdapter(MessageList);

                // notify the adapter to update the recycler view
                myAdapter.notifyDataSetChanged();

                //set the adapter for the recycler view
                myRecylerView.setAdapter(myAdapter);

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
                            Calendar cal = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                            m.setDate(sdf.format(cal.getTime()));

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

    protected void sendNotification(String message) {

        int notificationID = 101;

        String channelID = "com.example.orlenchatbotapp.news";

        Notification notification =
                new Notification.Builder(ChatFragment.this.getActivity(),
                        channelID)
                        .setContentTitle("New Message")
                        .setContentText(message)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setChannelId(channelID)
                        .build();

        notificationManager.notify(notificationID, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }
}
package com.example.orlenchatbotapp;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;
import androidx.legacy.content.WakefulBroadcastReceiver;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class NotifyDemoActivity extends AppCompatActivity {

    /*private Socket socket;

    NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_demo);

        Button button = (Button) findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                sendNotification("Button pressed");
            }
        });

        notificationManager =
                (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);

        createNotificationChannel(
                "com.example.orlenchatbotapp.news",
                "NotifyDemo News",
                "Example News Channel");

        try {
            socket = IO.socket("https://7806b38b.ngrok.io");
            socket.connect();
        } catch(URISyntaxException e) {
            e.printStackTrace();
        }

        socket.on("live-agent-request", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            String sessionId = data.getString("sessionId");
                            sendNotification(sessionId);

                            JSONArray arr = data.getJSONArray("messages");
                            for(int i = 0; i < arr.length(); i++){
                                System.out.println("Message type: " + arr.getJSONObject(i).getString("messageType"));
                                System.out.println("Message: " + arr.getJSONObject(i).getString("message"));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //String message = data.getString("chat message");

                    }
                });
            }
        });
    }

    protected void createNotificationChannel(String id, String name,
                                             String description) {

        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel channel =
                null;
            channel = new NotificationChannel(id, name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(
                    new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(channel);




    }


    protected void sendNotification(String message) {

        int notificationID = 101;

        String channelID = "com.example.orlenchatbotapp.news";

            Notification notification =
                    new Notification.Builder(NotifyDemoActivity.this,
                            channelID)
                            .setContentTitle("New Message")
                            .setContentText(message)
                            .setSmallIcon(android.R.drawable.ic_dialog_info)
                            .setChannelId(channelID)
                            .build();

        notificationManager.notify(notificationID, notification);
    }


    public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // Explicitly specify that GcmIntentService will handle the intent.
            ComponentName comp = new ComponentName(context.getPackageName(),
                    NotifyDemoActivity.class.getName());
            // Start the service, keeping the device awake while it is launching.
            startWakefulService(context, (intent.setComponent(comp)));
            setResultCode(Activity.RESULT_OK);
        }
    }*/
}
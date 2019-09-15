package com.example.orlenchatbotapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.orlenchatbotapp.R;
import com.example.orlenchatbotapp.model.Message;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Message> messageList;
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    class ViewHolder0 extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        public ViewHolder0(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
        }

        public void bind(Message message) {
            messageText.setText(message.getMessage());

            // Format the stored timestamp into a readable String using method.
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            timeText.setText(message.getDate());
        }
    }

    class ViewHolder2 extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ImageView profileImage;

        public ViewHolder2(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
            nameText = (itemView.findViewById(R.id.text_message_name));
            profileImage = itemView.findViewById(R.id.image_message_profile);
        }

        public void bind(Message message){
            messageText.setText(message.getMessage());

            // Format the stored timestamp into a readable String using method.
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            timeText.setText(message.getDate());

            nameText.setText(message.getNickname());

            // Insert the profile image from the URL into the ImageView.
            //toDo
            //Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);
        }
    }

    public MyAdapter(List<Message> MessagesList) {
        this.messageList = MessagesList;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);

        if (message.getNickname().equals("HR employee")) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_MESSAGE_SENT:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_sent, parent, false);
                return new ViewHolder0(view);
            case VIEW_TYPE_MESSAGE_RECEIVED:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_received, parent, false);
                return new ViewHolder2(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        Message message = messageList.get(position);
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ViewHolder0 viewHolder0 = (ViewHolder0) holder;
                viewHolder0.bind(message);
                break;

            case VIEW_TYPE_MESSAGE_RECEIVED:
                ViewHolder2 viewHolder2 = (ViewHolder2) holder;
                viewHolder2.bind(message);
                break;
        }
    }
}
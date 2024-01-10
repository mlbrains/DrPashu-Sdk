package com.drpashu.sdk.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drpashu.sdk.databinding.ItemMessageReceivedBinding;
import com.drpashu.sdk.databinding.ItemMessageSentBinding;
import com.drpashu.sdk.network.model.response.MessageListResponse;

import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter {
    private Context context;
    private Activity activity;
    private int TYPE_SENT = 1;
    private int TYPE_RECEIVED = 2;
    List<MessageListResponse.Data> messageResponseList;

    public MessageListAdapter(Context context, Activity activity, List<MessageListResponse.Data> messageResponseList) {
        this.context = context;
        this.activity = activity;
        this.messageResponseList = messageResponseList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_SENT)
            return new ViewHolderMessageSent(ItemMessageSentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        return new ViewHolderMessageReceived(ItemMessageReceivedBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (messageResponseList.get(position).getIsCurrentUser())
            ((ViewHolderMessageSent) holder).bind(position);
        else
            ((ViewHolderMessageReceived) holder).bind(position);

    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        if (messageResponseList.get(position).getIsCurrentUser()){
            return TYPE_SENT;
        }
        return TYPE_RECEIVED;
    }

    @Override
    public int getItemCount() {
        return messageResponseList.size();
    }

    public class ViewHolderMessageSent extends RecyclerView.ViewHolder {
        private ItemMessageSentBinding binding;
        public ViewHolderMessageSent(ItemMessageSentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(int position) {
            binding.messageText.setText(messageResponseList.get(position).getText());
            binding.timeText.setText(messageResponseList.get(position).getDate() + " " + messageResponseList.get(position).getTime());
        }
    }

    public class ViewHolderMessageReceived extends RecyclerView.ViewHolder {
        private ItemMessageReceivedBinding binding;
        public ViewHolderMessageReceived(ItemMessageReceivedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(int position) {
            binding.messageText.setText(messageResponseList.get(position).getText());
            binding.timeText.setText(messageResponseList.get(position).getDate() + " " + messageResponseList.get(position).getTime());
        }
    }
}
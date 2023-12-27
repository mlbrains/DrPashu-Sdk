package com.drpashu.sdk.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.drpashu.sdk.R;
import com.drpashu.sdk.adapter.MessageListAdapter;
import com.drpashu.sdk.databinding.FragmentChatBinding;
import com.drpashu.sdk.network.NetworkingInterface;
import com.drpashu.sdk.network.model.response.MessageListResponse;
import com.drpashu.sdk.services.FirebaseService;

import java.util.List;

public class ChatFragment extends BaseFragment {
    private FragmentChatBinding binding;
    private String callId = "", userName = "";
    private View view1;
    private LocalBroadcastManager localBroadcastManager;
    private FirebaseService firebaseService;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        localBroadcastManager = LocalBroadcastManager.getInstance(context);

        IntentFilter newMessageIntentFilter = new IntentFilter();
        newMessageIntentFilter.addAction("chat");
        localBroadcastManager.registerReceiver(newMessageBroadcastReceiver , newMessageIntentFilter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        localBroadcastManager.unregisterReceiver(newMessageBroadcastReceiver);
    }

    private final BroadcastReceiver newMessageBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null)
                networking.getMessageList(callId);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            callId = getArguments().getString("callId");
            userName = getArguments().getString("userName");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            activity.getSupportActionBar().setTitle(utils.getStringValue(R.string.chat));
        } catch (Exception e){
            Log.e("set screen error", e.getMessage()+"");
        }

        view1 = view;
        onClickListener();

        showLoading();
        networking.getMessageList(callId);
    }

    private void onClickListener() {
        binding.messageImg.setOnClickListener(v -> {
            if (binding.messageInput.getText().toString().length() == 0)
                utils.shortToast("Please enter message");
            else {
                showLoading();
                networking.sendMessage(callId, binding.messageInput.getText().toString());
                firebaseService.onMessageReceived(requireContext(),userName,binding.messageInput.getText().toString());
            }
        });
    }

    @Override
    public <T> void networkingRequest(@Nullable NetworkingInterface.MethodType methodType, boolean status, @Nullable T error, Object object) {
        if (methodType == MethodType.getMessageList && status) {
            dismissLoading();

            List<MessageListResponse.Data> messageResponseList = (List<MessageListResponse.Data>) object;

            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setReverseLayout(true);
            binding.messageRecyclerview.setLayoutManager(layoutManager);
            MessageListAdapter messageListAdapter = new MessageListAdapter(context, activity, messageResponseList);
            binding.messageRecyclerview.setAdapter(messageListAdapter);

            binding.messageInput.setText("");
            view1.clearFocus();
        }
        else if (methodType == MethodType.sendMessage && status)
            networking.getMessageList(callId);
        else if (methodType == MethodType.getMessageList || methodType == MethodType.sendMessage && !status)
            dismissLoading();
    }
}

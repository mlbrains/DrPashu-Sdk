package com.drpashu.sdk.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.drpashu.sdk.R;
import com.drpashu.sdk.adapter.CallHistoryInterface;
import com.drpashu.sdk.adapter.CallHistoryListAdapter;
import com.drpashu.sdk.databinding.FragmentCallHistoryBinding;
import com.drpashu.sdk.network.model.response.CallHistoryListResponse;

import java.util.List;

public class CallHistoryFragment extends BaseFragment implements CallHistoryInterface {
    private FragmentCallHistoryBinding binding;
    private View view1;
    private String notificationCallId = "",screen="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            notificationCallId = getArguments().getString("callId");
            screen = getArguments().getString("screen");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCallHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view1 = view;

        try {
            activity.getSupportActionBar().setTitle(utils.getStringValue(R.string.call_history));
        } catch (Exception e){
            Log.e("set screen error", e.getMessage()+"");
        }

        if (getArguments() != null) {
            if (getArguments().getString("callId") != null) {
                if (notificationCallId.length() != 0) {
                    activity.dismissLoader();
                    Bundle bundle = new Bundle();
                    bundle.putString("callId", notificationCallId + "");
                    bundle.putString("screen", screen + "");
                    notificationCallId = "";
                    Navigation.findNavController(view1).navigate(R.id.action_nav_history_to_callDetailFragment, bundle);
                } else
                    networking.getCallHistoryList();
            } else
                networking.getCallHistoryList();
        } else
            networking.getCallHistoryList();

        binding.consultDoctorBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_call_history_to_nav_consult_doctor));
    }

    @Override
    public <T> void networkingRequest(@Nullable MethodType methodType, boolean status, @Nullable T error, Object o) {
        if (methodType == MethodType.getCallHistoryList && status) {
            activity.dismissLoader();
            dismissLoading();

            List<CallHistoryListResponse.Data> callHistoryList = (List<CallHistoryListResponse.Data>) o;

            if (callHistoryList.size() == 0) {
                binding.noHistoryText.setVisibility(View.VISIBLE);
                binding.view.setVisibility(View.VISIBLE);
                binding.needHelpText.setVisibility(View.VISIBLE);
                binding.consultDoctorBtn.setVisibility(View.VISIBLE);
            }

            binding.historyRecyclerview.setLayoutManager(new LinearLayoutManager(context));

            CallHistoryListAdapter callHistoryListAdapter = new CallHistoryListAdapter(context, activity, (CallHistoryInterface) this, callHistoryList);
            binding.historyRecyclerview.setItemViewCacheSize(callHistoryList.size());
            binding.historyRecyclerview.setAdapter(callHistoryListAdapter);
        }
        else if (methodType == MethodType.getCallHistoryList && !status) {
            activity.dismissLoader();
            dismissLoading();
        }
    }

    @Override
    public void selectedCall(int callId) {
        Bundle bundle = new Bundle();
        bundle.putString("callId", callId+"");
        Navigation.findNavController(view1).navigate(R.id.action_nav_history_to_callDetailFragment, bundle);
    }
}
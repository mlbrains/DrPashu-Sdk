package com.drpashu.sdk.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.drpashu.sdk.R;
import com.drpashu.sdk.activity.HomeActivity;
import com.drpashu.sdk.databinding.DialogCallConnectFailedBinding;
import com.drpashu.sdk.utils.Utils;

public class CallConnectFailedDialog extends Dialog {
    private DialogCallConnectFailedBinding binding;
    private HomeActivity activity;
    private Context context;
    private String message;
    private Boolean titleVisibility = true;
    private Boolean successDialog = false;
    private Utils utils;

    public CallConnectFailedDialog(@NonNull Context context, HomeActivity activity, String message) {
        super(context);
        this.context = context;
        this.activity = activity;
        this.message = message;
    }

    public CallConnectFailedDialog(@NonNull Context context, HomeActivity activity, String message, Boolean titleVisibility, Boolean successDialog) {
        super(context);
        this.context = context;
        this.activity = activity;
        this.message = message;
        this.titleVisibility = titleVisibility;
        this.successDialog = successDialog;
        utils = new Utils(context, activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DialogCallConnectFailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (!titleVisibility)
            binding.congratsText.setVisibility(View.GONE);

        if (successDialog) {
            binding.imageView.setImageResource(R.drawable.onboarding_done_icon);
            binding.congratsText.setText(utils.getStringValue(R.string.thanks_for_calling_the_mobile));
        }

        binding.messageText.setText(message);
        binding.cancelImg.setOnClickListener(v -> dismiss());
    }
}

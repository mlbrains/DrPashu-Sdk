package com.drpashu.sdk.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.drpashu.sdk.activity.HomeActivity;
import com.drpashu.sdk.R;
import com.drpashu.sdk.network.Networking;
import com.drpashu.sdk.network.NetworkingInterface;
import com.drpashu.sdk.utils.PermissionListener;
import com.drpashu.sdk.utils.PreferenceUtils;
import com.drpashu.sdk.utils.Utils;

import java.util.Arrays;

public class BaseFragment extends Fragment implements NetworkingInterface, PermissionListener {
    protected Context context;
    protected HomeActivity activity;
    protected Networking networking;
    protected PreferenceUtils preferenceUtils;
    protected Utils utils;
    private ProgressDialog progressDialog;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<String[]> requestMultiplePermissionLauncher;
    private String singlePermission = "";
    private String[] multiplePermission = {};
    private int permissionAction = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();
        this.activity = (HomeActivity) getActivity();
        initializeLaunchers();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        networking = new Networking(context, activity, this);
        utils = new Utils(context, activity);
        preferenceUtils = new PreferenceUtils(context);

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(utils.getStringValue(R.string.loading));
        progressDialog.setCancelable(false);
    }

    protected void showLoading(){
        progressDialog.show();
    }

    protected void dismissLoading(){
        progressDialog.dismiss();
    }

    @Override
    public <T> void networkingRequest(@Nullable MethodType methodType, boolean status, @Nullable T error, Object object) {
    }
    private void initializeLaunchers() {
        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                        isGranted -> requestPermissionResult(isGranted, singlePermission, permissionAction));

        requestMultiplePermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                        isGranted -> requestMultiplePermissionResult(!isGranted.containsValue(false), null, multiplePermission, permissionAction));

    }
    public void requestPermission(String permission, int action) {
        Log.e("Permission", "BaseFragment: Permission Request " + permission + ", action- " + action);
        singlePermission = permission;
        permissionAction = action;
        requestPermissionLauncher.launch(permission);
    }
    public void requestMultiplePermission(String[] permission, int action) {
        Log.e("Permission", "BaseFragment: Multiple Permission Request " + Arrays.toString(permission) + ", action- " + action);
        multiplePermission = permission;
        permissionAction = action;
        requestMultiplePermissionLauncher.launch(permission);
    }
    @Override
    public void requestPermissionResult(Boolean isGranted, String requestedPermission, int action) {
        Log.e("Permission", "BaseFragment: Requested Permission- " + requestedPermission + ", IsGranted- " + isGranted);
    }

    @Override
    public void requestMultiplePermissionResult(Boolean isGranted, String[] deniedPermissions, String[] requestedPermissionList, int action) {
        Log.e("Permission", "BaseFragment: Requested Permission List- " + Arrays.toString(requestedPermissionList) + ", IsGranted- " + isGranted);
    }
}

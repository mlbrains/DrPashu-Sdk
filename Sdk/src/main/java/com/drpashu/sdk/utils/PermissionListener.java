package com.drpashu.sdk.utils;

public interface PermissionListener {
    void requestPermissionResult(Boolean isGranted, String requestedPermission, int action);

    void requestMultiplePermissionResult(Boolean isGranted, String[] deniedPermissions, String[] requestedPermissionList, int action);
}

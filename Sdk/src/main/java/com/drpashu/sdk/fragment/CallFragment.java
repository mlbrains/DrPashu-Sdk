package com.drpashu.sdk.fragment;

import static com.drpashu.sdk.utils.Constants.PERMISSION_CAMERA;
import static com.drpashu.sdk.utils.Constants.PERMISSION_RECORD_AUDIO;
import static com.drpashu.sdk.utils.WebConstants.STR_ANIMAL_TYPE;
import static com.drpashu.sdk.utils.WebConstants.STR_CALL_ID;
import static com.drpashu.sdk.utils.WebConstants.STR_CALL_PRICE;
import static com.drpashu.sdk.utils.WebConstants.STR_CHANNEL_ID;
import static com.drpashu.sdk.utils.WebConstants.STR_COMPANY_NAME;
import static com.drpashu.sdk.utils.WebConstants.STR_EDIT_PRESCRIPTION;
import static com.drpashu.sdk.utils.WebConstants.STR_FREE_CALL;
import static com.drpashu.sdk.utils.WebConstants.STR_HEALTH_VAL;
import static com.drpashu.sdk.utils.WebConstants.STR_LOT_ID;
import static com.drpashu.sdk.utils.WebConstants.STR_MESSAGE;
import static com.drpashu.sdk.utils.WebConstants.STR_PAYMENT_ID;
import static com.drpashu.sdk.utils.WebConstants.STR_USER_ID;
import static com.drpashu.sdk.utils.WebConstants.STR_USER_NAME;
import static com.drpashu.sdk.utils.WebConstants.STR_VET_CATEGORY;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.Navigation;

import com.drpashu.sdk.R;
import com.drpashu.sdk.databinding.FragmentCallBinding;
import com.drpashu.sdk.dialog.CallConnectFailedDialog;
import com.drpashu.sdk.network.ApiClient;
import com.drpashu.sdk.utils.WebConstants;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class CallFragment extends BaseFragment {
    private FragmentCallBinding binding;
    private String unixNotificationTime, callId, channelId, firstName, lastName,
            profileImg, language, animal, callInitiated, farmId, animalType,
            vetCategory, companyName, callAmount, paymentId, screen;
    private long countDownTimerValue = 45;
    private int notificationId = 0;
    private Boolean callIncoming, callRedial, doneAnalysis, freeCall;
    private LocalBroadcastManager localBroadcastManager;
    private CountDownTimer countDownTimer;
    private static final int PERMISSION_REQ_VOICE_VIDEO = 1;
    private View view1;
    private boolean callToParavet = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mandatory info
            callIncoming = getArguments().getBoolean("callIncoming");
            callRedial = getArguments().getBoolean("callRedial");
            callInitiated = getArguments().getString("callInitiated");
            screen = getArguments().getString("screen");
            callToParavet = getArguments().getBoolean("callToParavet");

            //incoming call required data
            callId = getArguments().getString("callId");
            channelId = getArguments().getString("channelId");
            firstName = getArguments().getString("firstName");
            lastName = getArguments().getString("lastName");
            notificationId = getArguments().getInt("notificationId");
            profileImg = getArguments().getString("profile_picture");
            unixNotificationTime = getArguments().getString("unixNotificationTime");
            language = getArguments().getString("language");
            animal = getArguments().getString("animal");

            //start call required data
            farmId = getArguments().getString("farmId");
            animalType = getArguments().getString("animalType");
            vetCategory = getArguments().getString("vetCategory");
            companyName = getArguments().getString("companyName");
            doneAnalysis = getArguments().getBoolean("doneAnalysis");
            callAmount = getArguments().getString("callAmount");
            paymentId = getArguments().getString("paymentId");
            freeCall = getArguments().getBoolean("freeCall");
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        localBroadcastManager = LocalBroadcastManager.getInstance(context);

        IntentFilter silentIntentForReconnecting = new IntentFilter();
        silentIntentForReconnecting.addAction("vet_call_decline");
        localBroadcastManager.registerReceiver(silentNotificationForReconnecting, silentIntentForReconnecting);

        IntentFilter intentForFarmerCallDecline = new IntentFilter();
        intentForFarmerCallDecline.addAction("missed_call");
        localBroadcastManager.registerReceiver(notificationForFarmerCallDecline, intentForFarmerCallDecline);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        localBroadcastManager.unregisterReceiver(silentNotificationForReconnecting);
        localBroadcastManager.unregisterReceiver(notificationForFarmerCallDecline);
    }

    private final BroadcastReceiver silentNotificationForReconnecting = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (countDownTimer != null)
                    countDownTimer.cancel();

                if (callRedial) {
                    utils.shortToast(utils.getStringValue(R.string.connecting_to_next_vet));
                    if (countDownTimer != null)
                        countDownTimer.onFinish();
                } else {
                    utils.shortToast(utils.getStringValue(R.string.user_busy));
                    preferenceUtils.setBlockNavigationStatus(false);
                    activity.onBackPressed();
                }
            }
        }
    };

    private final BroadcastReceiver notificationForFarmerCallDecline = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                utils.shortToast(utils.getStringValue(R.string.farmer_ended_call));
                preferenceUtils.setBlockNavigationStatus(false);
                activity.onBackPressed();
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCallBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        activity.updateTooblar(ContextCompat.getDrawable(activity, R.drawable.ic_consult_doctor), false);
        activity.getSupportActionBar().hide();

        view1 = view;
        preferenceUtils.setBlockNavigationStatus(true);
        onClickListeners();

        if (callIncoming)
            showIncomingCallLayout();
        else
            setWebView();
    }

    private void onClickListeners() {
        binding.callAcceptText.setOnClickListener(v -> binding.callAcceptImg.performClick());
        binding.callDeclineText.setOnClickListener(v -> binding.callDeclineText.performClick());
        binding.callAcceptImg.setOnClickListener(v -> requestMultiplePermission(new String[]{PERMISSION_CAMERA, PERMISSION_RECORD_AUDIO}, PERMISSION_REQ_VOICE_VIDEO));

        binding.callDeclineImg.setOnClickListener(v -> {
            NotificationManagerCompat.from(activity).cancel(null, notificationId);
            if (countDownTimer != null)
                countDownTimer.cancel();

            if (checkCallTime()) {
//                activity.updateFirebaseEvents("button_click", "reject_call");
                showLoading();
                networking.rejectCall(callId, notificationId + "", callInitiated);
            } else {
                utils.shortToast(utils.getStringValue(R.string.call_already_completed));
                preferenceUtils.setBlockNavigationStatus(false);
                activity.onBackPressed();
            }
        });
    }

    private void showIncomingCallLayout() {
        if (!checkCallTime()) {
            NotificationManagerCompat.from(activity).cancel(null, notificationId);
            utils.shortToast(utils.getStringValue(R.string.call_already_completed));
            preferenceUtils.setBlockNavigationStatus(false);
            activity.onBackPressed();
        } else {
            utils.hideView(binding.webView);
            utils.visibleView(binding.callLayout);

            binding.userText.setText(firstName + " " + lastName);

            if (profileImg.length() != 0)
                Picasso.get().load(ApiClient.BASE_URL_MEDIA + profileImg).into(binding.userImg);
            if (animal != null) {
                if (animal.length() != 0) {
                    utils.visibleView(binding.animalText);
                    binding.animalText.setText(utils.getStringValue(R.string.animal) + " : " + animal);
                }
            }
            if (language != null) {
                if (language.length() != 0) {
                    String[] languageList = utils.getArrayValue(R.array.language_list);
                    String languageValue = "";
                    if (language.equalsIgnoreCase("en"))
                        languageValue = languageList[0];
                    else if (language.equalsIgnoreCase("as"))
                        languageValue = languageList[1];
                    else if (language.equalsIgnoreCase("bn"))
                        languageValue = languageList[2];
                    else if (language.equalsIgnoreCase("gu"))
                        languageValue = languageList[3];
                    else if (language.equalsIgnoreCase("hi"))
                        languageValue = languageList[4];
                    else if (language.equalsIgnoreCase("kn"))
                        languageValue = languageList[5];
                    else if (language.equalsIgnoreCase("ml"))
                        languageValue = languageList[6];
                    else if (language.equalsIgnoreCase("mr"))
                        languageValue = languageList[7];
                    else if (language.equalsIgnoreCase("pa"))
                        languageValue = languageList[8];
                    else if (language.equalsIgnoreCase("ta"))
                        languageValue = languageList[9];
                    else if (language.equalsIgnoreCase("te"))
                        languageValue = languageList[10];

                    utils.visibleView(binding.languageText);
                    binding.languageText.setText(utils.getStringValue(R.string.user_language) + " : " + languageValue);
                }
            }

            startCountDown();
        }
    }

    private boolean checkCallTime() {
        long currentTime = System.currentTimeMillis();
        long notificationLongTime = Long.parseLong(unixNotificationTime);

        long differece = (currentTime - notificationLongTime) / 1000;
        countDownTimerValue = 45 - differece;
        Log.e("timeDifferenceLog", differece + " " + unixNotificationTime);

        return differece <= 45;
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(countDownTimerValue * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                NotificationManagerCompat.from(activity).cancel(null, notificationId);

                utils.shortToast(utils.getStringValue(R.string.call_already_completed));
                preferenceUtils.setBlockNavigationStatus(false);
                activity.onBackPressed();
            }
        }.start();
    }

    private void setWebView() {
        utils.hideView(binding.callLayout);
        utils.visibleView(binding.webView);

        WebSettings webSettings = binding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);

        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return false;
            }
        });

        binding.webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                request.grant(request.getResources());
            }
        });

        binding.webView.addJavascriptInterface(new WebAppInterface(), "Android");

//        binding.webView.loadUrl(ApiClient.BASE_URL_CALL + screen + !callIncoming + "&paravet_call=" + callToParavet);
//        Log.e("url", ApiClient.BASE_URL_CALL + screen + !callIncoming + "&paravet_call=" + callToParavet);

        binding.webView.loadUrl(ApiClient.BASE_URL_CALL + !callIncoming);
        Log.e("url", ApiClient.BASE_URL_CALL + !callIncoming);
    }

    private void sendDataToWeb(String data) {
        String script = "androidData(" + data + ")";
        binding.webView.post(() -> binding.webView.evaluateJavascript(script, null));
    }

    private void showCallNotConnectedDialog(String message) {
        binding.webView.post(() -> {
            CallConnectFailedDialog callConnectFailedDialog = new CallConnectFailedDialog(context, activity, message);
            callConnectFailedDialog.setCancelable(true);
            callConnectFailedDialog.show();
            callConnectFailedDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        });
    }

    private void navigate(int id, String callId) {
        binding.webView.post(() -> {
            try {
                Bundle bundle = new Bundle();
                bundle.putString("callId", callId);
                Navigation.findNavController(view1).navigate(id, bundle);
            } catch (Exception ignored){

            }
        });
    }

    private void acceptCall() {
        NotificationManagerCompat.from(activity).cancel(null, notificationId);
        if (countDownTimer != null)
            countDownTimer.cancel();

        if (checkCallTime()) {
//            activity.updateFirebaseEvents("button_click", "accept_call");
            setWebView();
        } else {
            utils.shortToast(utils.getStringValue(R.string.call_already_completed));
            preferenceUtils.setBlockNavigationStatus(false);
            activity.onBackPressed();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (preferenceUtils.getUserRole() == 2)
//            networking.updateVetStatus("Online");

        if (countDownTimer != null)
            countDownTimer.cancel();
    }

    @Override
    public <T> void networkingRequest(@Nullable MethodType methodType, boolean status, @Nullable T error, Object o) {
        if (methodType == MethodType.rejectCall) {
            dismissLoading();
            preferenceUtils.setBlockNavigationStatus(false);
            activity.onBackPressed();
        } else if (methodType == MethodType.checkCallPickAllowed && status) {
            dismissLoading();
            acceptCall();
        } else if (methodType == MethodType.checkCallPickAllowed && !status) {
            dismissLoading();
            NotificationManagerCompat.from(activity).cancel(null, notificationId);
            if (countDownTimer != null)
                countDownTimer.cancel();
            preferenceUtils.setBlockNavigationStatus(false);
            activity.onBackPressed();
        }
    }

    @Override
    public void requestMultiplePermissionResult(Boolean isGranted, String[] deniedPermissions, String[] requestedPermissionList, int action) {
        Log.e("Permission", "ConsultDoctorFragment: Requested Permission List- " + Arrays.toString(requestedPermissionList) + ", IsGranted- " + isGranted + ", action- " + action);
        if (action == PERMISSION_REQ_VOICE_VIDEO) {
            if (isGranted) {
//                if (preferenceUtils.getUserRole() == 2) {
//                    showLoading();
//                    networking.checkCallPickAllowed(callId);
//                } else
                    acceptCall();
            } else
                utils.shortToast(utils.getStringValue(R.string.permission_voice_video_allow));
        }
    }

    public class WebAppInterface {

        @JavascriptInterface
        public void webData(String data) throws JSONException {
            JSONObject jsonObject = new JSONObject(data);

            if (jsonObject.has(WebConstants.STR_REQUEST)) {
//                utils.shortToast(data);
                Log.e("WebAppInterface", "WebData Received: " + data);


                switch (jsonObject.getString(WebConstants.STR_REQUEST)) {
                    case WebConstants.ACTION_CALL_INITIATED:
                        switch (screen) {
                    case WebConstants.ACTION_START_CALL:
                        JSONObject startCallObject = new JSONObject();
                        startCallObject.put(WebConstants.STR_NAME, WebConstants.ACTION_START_CALL);

                        JSONObject startCallData = new JSONObject();
                        startCallData.put(STR_USER_ID, preferenceUtils.getUserId());
                        startCallData.put(STR_USER_NAME, preferenceUtils.getUsername());
                        startCallData.put(STR_LOT_ID, farmId);
                        startCallData.put(STR_HEALTH_VAL, doneAnalysis);
                        startCallData.put(STR_VET_CATEGORY, vetCategory);
                        startCallData.put(STR_COMPANY_NAME, companyName);
                        startCallData.put(STR_ANIMAL_TYPE, animalType);
                        startCallData.put(STR_FREE_CALL, freeCall);
                        startCallData.put(STR_CALL_PRICE, callAmount);
                        startCallData.put(STR_PAYMENT_ID, paymentId);

                        startCallObject.put("data", startCallData);
                        Log.e("WebAppInterface", "WebData: Start Call- " + startCallObject);

                        sendDataToWeb(startCallObject.toString());
                        break;
                    case WebConstants.ACTION_CALL_BACK:
                        JSONObject callBackObject = new JSONObject();
                        callBackObject.put(WebConstants.STR_NAME, WebConstants.ACTION_CALL_BACK);

                        JSONObject callBackData = new JSONObject();
                        callBackData.put(STR_USER_ID, preferenceUtils.getUserId());
                        callBackData.put(STR_USER_NAME, preferenceUtils.getUsername());
                        callBackData.put(STR_CALL_ID, callId);

                        callBackObject.put("data", callBackData);
                        Log.e("WebAppInterface", "WebData: Call Back- " + callBackObject);

                        sendDataToWeb(callBackObject.toString());
                        break;
                }
                break;
                    case WebConstants.ACTION_CALL_RECEIVED:
                    case WebConstants.ACTION_JOIN_CALL:
                        JSONObject joinCallObject = new JSONObject();
                        joinCallObject.put(WebConstants.STR_NAME, WebConstants.ACTION_JOIN_CALL);

                        JSONObject joinCallData = new JSONObject();
                        joinCallData.put(STR_USER_ID, preferenceUtils.getUserId());
                        joinCallData.put(STR_USER_NAME, preferenceUtils.getUsername());
                        joinCallData.put(STR_CALL_ID, callId);
                        joinCallData.put(STR_CHANNEL_ID, channelId);

                        joinCallObject.put("data", joinCallData);
                        Log.e("WebAppInterface", "WebData: Join Call- " + joinCallObject);

                        sendDataToWeb(joinCallObject.toString());
                        break;
                    case WebConstants.ACTION_CALL_COMPLETE:
                        String callId = "";
                        boolean editPrescription = true;
                        if (jsonObject.has(STR_CALL_ID))
                            callId = jsonObject.getString(STR_CALL_ID);
                        preferenceUtils.setBlockNavigationStatus(false);
                            navigate(R.id.action_callFragment_to_callFeedbackFragment, callId);
                        break;
                    case WebConstants.ACTION_CALL_NO_PICKUP:
                    case WebConstants.ACTION_ANOTHER_VET_PICKUP:
                    case WebConstants.ACTION_CALL_BACK_FAILED:
                        if (jsonObject.has(STR_MESSAGE))
                            showCallNotConnectedDialog(jsonObject.get(STR_MESSAGE).toString());
                    case WebConstants.ACTION_CLOSE_WEB_ERROR:
                        preferenceUtils.setBlockNavigationStatus(false);
                        binding.webView.post(() -> activity.onBackPressed());
                        break;
                }
            }
        }
    }
}
package com.drpashu.sdk.fragment;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.drpashu.sdk.R;
import com.drpashu.sdk.adapter.ProductListAdapter;
import com.drpashu.sdk.databinding.FragmentCallDetailBinding;
import com.drpashu.sdk.network.ApiClient;
import com.drpashu.sdk.network.model.response.CallDetailResponse;
import com.ortiz.touchview.TouchImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CallDetailFragment extends BaseFragment {
    private FragmentCallDetailBinding binding;
    private String callId = "", baseUrl = ApiClient.BASE_URL_MEDIA,screen = "";
    private byte[] prescription1Byte = null, prescription2Byte = null;
    private static final int RESULT_LOAD_PRESCRIPTION_1 = 1, RESULT_LOAD_PRESCRIPTION_2 = 2;
    private View view1;
    private ProductListAdapter productListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            callId = getArguments().getString("callId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCallDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            activity.getSupportActionBar().setTitle(utils.getStringValue(R.string.call_history));
        } catch (Exception e){
            Log.e("set screen error", e.getMessage()+"");
        }

        view1 = view;
        onClickListeners();

        showLoading();
        networking.getCallDetail(callId);
    }

    private void onClickListeners() {
        binding.prescription1Img.setOnClickListener(v -> previewImg(binding.prescription1Img.getDrawable()));
        binding.prescription2Img.setOnClickListener(v -> previewImg(binding.prescription2Img.getDrawable()));
//        binding.animalImg1.setOnClickListener(v -> previewImg(binding.animalImg1.getDrawable()));
//        binding.animalImg2.setOnClickListener(v -> previewImg(binding.animalImg2.getDrawable()));
        binding.imageButtonDown.setOnClickListener(view -> {
            if(binding.importantNoteText.getVisibility() == View.VISIBLE){
                TransitionManager.beginDelayedTransition(binding.cardImportantNoteCallDetails,new AutoTransition());
                utils.hideView(binding.groupCallDetails);
                binding.imageButtonDown.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);
            }
            else{
                TransitionManager.beginDelayedTransition(binding.cardImportantNoteCallDetails,new AutoTransition());
                utils.visibleView(binding.groupCallDetails);
                binding.imageButtonDown.setImageResource(R.drawable.icon_arrow_up);
            }
        });
        binding.chatIcon.setOnClickListener(v -> {
            navigateToChat(v);
        });
    }

    private void navigateToChat(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("callId", callId);
        bundle.putString("userName", binding.userText.getText().toString());
        screen = "";
        Navigation.findNavController(view).navigate(R.id.action_callDetailFragment_to_chatFragment, bundle);
    }
    private void previewImg(Drawable drawable) {
        Dialog previewImgDialog = new Dialog(getContext());
        previewImgDialog.setCancelable(false);
//        previewImgDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        previewImgDialog.setContentView(R.layout.layout_image_preview);

        TouchImageView previewImg = previewImgDialog.findViewById(R.id.previewImg);
        ImageView closeImg = previewImgDialog.findViewById(R.id.closeImg);

        previewImg.setImageDrawable(drawable);

        closeImg.setOnClickListener(dialogView -> previewImgDialog.dismiss());

        previewImgDialog.show();
    }

    @Override
    public <T> void networkingRequest(@Nullable MethodType methodType, boolean status, @Nullable T error, Object o) {
        if (methodType == MethodType.getCallDetail && status) {
            dismissLoading();
            utils.visibleView(binding.mainLayout);

            binding.descriptionInput.setEnabled(false);
            binding.symptomVetInput.setEnabled(false);
            binding.diseaseInput.setEnabled(false);
            binding.descriptionInput.setTextColor(getContext().getResources().getColor(R.color.black));
            binding.diseaseInput.setTextColor(getContext().getResources().getColor(R.color.black));
            binding.symptomVetInput.setTextColor(getContext().getResources().getColor(R.color.black));

//            binding.symptomListText.setText(utils.getStringValue(R.string.symptom_list) + " :");
//            binding.animalImgText.setText(utils.getStringValue(R.string.animal_image) + " :");
            binding.prescriptionImgText.setText(utils.getStringValue(R.string.prescription) + " :");

            CallDetailResponse.Data callDetailResponse = (CallDetailResponse.Data) o;

            callId = callDetailResponse.getId() + "";
            binding.userText.setText(callDetailResponse.getFirstName() + " " + callDetailResponse.getLastName());
            binding.dateText.setText(callDetailResponse.getDate() + " " + callDetailResponse.getTime());

            if (callDetailResponse.getProfilePicture() != null) {
                if (callDetailResponse.getProfilePicture().length() != 0)
                    Picasso.get().load(ApiClient.BASE_URL_MEDIA + callDetailResponse.getProfilePicture()).into(binding.userImg);
            }

//            if (callDetailResponse.getCallInitiated().equalsIgnoreCase("Farmer") && callDetailResponse.getCallStatusRes().equalsIgnoreCase("Missed"))
//                binding.infoIcon.setImageResource(R.drawable.call_out_miss);
//            else if (callDetailResponse.getCallInitiated().equalsIgnoreCase("Farmer") && callDetailResponse.getCallStatusRes().equalsIgnoreCase("Completed"))
//                binding.infoIcon.setImageResource(R.drawable.call_out_done);
//            else if (callDetailResponse.getCallInitiated().equalsIgnoreCase("Vet") && callDetailResponse.getCallStatusRes().equalsIgnoreCase("Missed"))
//                binding.infoIcon.setImageResource(R.drawable.call_in_miss);
//            else if (callDetailResponse.getCallInitiated().equalsIgnoreCase("Vet") && callDetailResponse.getCallStatusRes().equalsIgnoreCase("Completed"))
//                binding.infoIcon.setImageResource(R.drawable.call_in_done);
//            else if (callDetailResponse.getCallInitiated().equalsIgnoreCase("Admin") && callDetailResponse.getCallStatusRes().equalsIgnoreCase("Missed"))
//                binding.infoIcon.setImageResource(R.drawable.call_in_miss);
//            else if (callDetailResponse.getCallInitiated().equalsIgnoreCase("Admin") && callDetailResponse.getCallStatusRes().equalsIgnoreCase("Completed"))
//                binding.infoIcon.setImageResource(R.drawable.call_in_done);

            if (callDetailResponse.getCallStatusRes().equalsIgnoreCase("Completed")) {
                utils.visibleView(binding.chatIcon);
                utils.visibleView(binding.constraintCallback);
                if (callDetailResponse.getCallDuration() != null)
                    binding.dateText.setText(callDetailResponse.getDate() + " " + callDetailResponse.getTime() + " (" + callDetailResponse.getCallDuration() + ")");
                binding.cardCallStatus.setStrokeColor(getResources().getColor(R.color.green600));
                binding.cardCallStatus.getBackground().setTint(getResources().getColor(R.color.green_success));
                binding.cardCallStatusInfo.getBackground().setTint(getResources().getColor(R.color.call_success_green));
                binding.callStatusInfo.setText(R.string.success);
                binding.callStatusText.setText(R.string.you_have_completed_your_call);
                binding.callStatusText.setTextColor(getResources().getColor(R.color.call_success_green_text));
                binding.relativeLayoutCallGif.setBackgroundColor(getResources().getColor(R.color.green300));
            } else {
                utils.hideView(binding.updateText);
                utils.hideView(binding.prescriptionImgText);
                utils.hideView(binding.prescription1Img);
                utils.hideView(binding.prescription2Img);
                utils.hideView(binding.descriptionLayout);
                utils.hideView(binding.diseaseLayout);
                utils.hideView(binding.symptomVetLayout);
                utils.hideView(binding.chatIcon);
                utils.hideView(binding.constraintCallback);

                binding.cardCallStatus.setStrokeColor(getResources().getColor(R.color.red_600));
                binding.cardCallStatus.getBackground().setTint(getResources().getColor(R.color.red_300));
                binding.cardCallStatusInfo.getBackground().setTint(getResources().getColor(R.color.red_call_error));
                binding.callStatusInfo.setText(R.string.failed);
                binding.callStatusText.setText(R.string.call_unable_to_connect);
                binding.callStatusText.setTextColor(getResources().getColor(R.color.call_failed_red_text));
                binding.relativeLayoutCallGif.setBackgroundColor(getResources().getColor(R.color.red_300));
            }

            if (callDetailResponse.getPrescriptionImageFirst().length() != 0) {
                Picasso.get().load(baseUrl + "/media/" + callDetailResponse.getPrescriptionImageFirst()).into(binding.prescription1Img);
                binding.prescriptionImgText.setText(utils.getStringValue(R.string.prescription_detail) + " :");
            }
            else {
                utils.hideView(binding.prescriptionImgText);
                utils.hideView(binding.prescription1Img);
                utils.hideView(binding.prescription2Img);
            }

            if (callDetailResponse.getPrescriptionImageSecond().length() != 0) {
                Picasso.get().load(baseUrl + "/media/" + callDetailResponse.getPrescriptionImageSecond()).into(binding.prescription2Img);
                binding.prescriptionImgText.setText(utils.getStringValue(R.string.prescription_detail) + " :");
            }
            else
                utils.hideView(binding.prescription2Img);

            if (callDetailResponse.getProducts().size() == 0)
                utils.hideView(binding.recommendProductText);
            else {
                binding.recommendProductText.setText(utils.getStringValue(R.string.products_recommended_by_vet));
                utils.visibleView(binding.recommendProductText);
            }

            binding.productRecyclerview.setLayoutManager(new GridLayoutManager(context, 2));
            productListAdapter = new ProductListAdapter(context, activity, callDetailResponse.getProducts(), false);
            binding.productRecyclerview.setAdapter(productListAdapter);
            binding.productRecyclerview.setItemViewCacheSize(callDetailResponse.getProducts().size());

            if (callDetailResponse.getDetails() != null)
                binding.descriptionInput.setText(callDetailResponse.getDetails() + "");
            else {
                utils.hideView(binding.descriptionLayout);
                utils.hideView(binding.textviewTreatment);
            }

            if (callDetailResponse.getDiagnosis() != null)
                binding.diseaseInput.setText(callDetailResponse.getDiagnosis() + "");
            else {
                utils.hideView(binding.diseaseLayout);
                utils.hideView(binding.textviewDiagnosis);
            }


            if (callDetailResponse.getSymptoms() != null)
                binding.symptomVetInput.setText(callDetailResponse.getSymptoms() + "");
            else {
                utils.hideView(binding.symptomVetLayout);
                utils.hideView(binding.textviewSymptoms);
            }

            if (callDetailResponse.getHealthVal()) {
                if (callDetailResponse.getLotExists()) {
//                    utils.visibleView(binding.farmDetailLayout);
//                    utils.hideView(binding.animalTypeTitleText);
                    utils.hideView(binding.animalTypeText);

//                    binding.animalText.setText(callDetailResponse.getAnimal() + "");
//                    binding.typeText.setText(callDetailResponse.getBreed() + "");
//                    binding.quantityText.setText(callDetailResponse.getQuantity() + "");
//                    binding.dobText.setText(callDetailResponse.getDOB() + "");
                } else {
//                    utils.visibleView(binding.animalTypeTitleText);
                    utils.visibleView(binding.animalTypeText);
//                    utils.hideView(binding.farmDetailLayout);

                    binding.animalTypeText.setText(callDetailResponse.getAnimal() + "");
                }

//                binding.symptomListView.setAdapter(new ArrayAdapter<>(getContext(), R.layout.symptom_list, R.id.symptom, callDetailResponse.getSymptomsList()));
//                binding.symptomListView.setDivider(null);

//                if (callDetailResponse.getAnalysisImage() != null)
//                    Picasso.get().load(baseUrl + callDetailResponse.getAnalysisImage()).into(binding.animalImg1);
//                else {
//                    utils.hideView(binding.animalImgText);
//                    utils.hideView(binding.animalImg1);
//                    utils.hideView(binding.animalImg2);
//                }

//                if (callDetailResponse.getPostmortemImage() != null) {
//                    utils.visibleView(binding.animalImg2);
//                    Picasso.get().load(baseUrl + callDetailResponse.getPostmortemImage()).into(binding.animalImg2);
//                } else
//                    utils.hideView(binding.animalImg2);

            } else {
//                utils.hideView(binding.symptomListText);
//                utils.hideView(binding.symptomListView);
//                utils.hideView(binding.animalImgText);
//                utils.hideView(binding.animalImg1);
//                utils.hideView(binding.animalImg2);

//                utils.visibleView(binding.animalTypeTitleText);
                utils.visibleView(binding.animalTypeText);
//                utils.hideView(binding.farmDetailLayout);
                binding.animalTypeText.setText(callDetailResponse.getAnimal() + "");
            }
        } else if (methodType == MethodType.getCallDetail && !status)
            dismissLoading();
    }
}
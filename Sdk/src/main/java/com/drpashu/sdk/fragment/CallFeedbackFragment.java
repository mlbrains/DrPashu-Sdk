package com.drpashu.sdk.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.drpashu.sdk.R;
import com.drpashu.sdk.adapter.FeedbackListAdapter;
import com.drpashu.sdk.adapter.MultiSelectInterface;
import com.drpashu.sdk.databinding.FragmentCallFeedbackBinding;
import com.drpashu.sdk.network.model.response.FeedbackListResponse;

import java.util.ArrayList;
import java.util.List;

public class CallFeedbackFragment extends BaseFragment implements MultiSelectInterface {
    private FragmentCallFeedbackBinding binding;
    private FeedbackListAdapter feedbackListAdapter;
    private String feedbackId = "", rating="", callId = "";
    private ArrayList<String> selectedFeedbackList;
    private FeedbackListResponse.Data feedbackListData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            callId = getArguments().getString("callId");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCallFeedbackBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            activity.getSupportActionBar().show();
            activity.getSupportActionBar().setTitle(utils.getStringValue(R.string.call_feedback_toolbar));
        } catch (Exception e){
            Log.e("set screen error", e.getMessage()+"");
        }
        showLoading();
        networking.getFeedbackList();

        onClickListener();
    }

    private void onClickListener() {
        binding.firstStarImageView.setOnClickListener(v -> {
            binding.firstStarImageView.setImageResource(R.drawable.feedback_star_filled);
            binding.secondStarImageView.setImageResource(R.drawable.feedback_star_empty);
            binding.thirdStarImageView.setImageResource(R.drawable.feedback_star_empty);
            binding.fourthStarImageView.setImageResource(R.drawable.feedback_star_empty);
            binding.fifthStarImageView.setImageResource(R.drawable.feedback_star_empty);

            setFeedbackUi("1", utils.getStringValue(R.string.rating_1_title),
                    utils.getStringValue(R.string.rating_1), feedbackListData.getRating1());
        });

        binding.secondStarImageView.setOnClickListener(v -> {
            binding.firstStarImageView.setImageResource(R.drawable.feedback_star_filled);
            binding.secondStarImageView.setImageResource(R.drawable.feedback_star_filled);
            binding.thirdStarImageView.setImageResource(R.drawable.feedback_star_empty);
            binding.fourthStarImageView.setImageResource(R.drawable.feedback_star_empty);
            binding.fifthStarImageView.setImageResource(R.drawable.feedback_star_empty);

            setFeedbackUi("2", utils.getStringValue(R.string.rating_2_title),
                    utils.getStringValue(R.string.rating_2), feedbackListData.getRating2());
        });

        binding.thirdStarImageView.setOnClickListener(v -> {
            binding.firstStarImageView.setImageResource(R.drawable.feedback_star_filled);
            binding.secondStarImageView.setImageResource(R.drawable.feedback_star_filled);
            binding.thirdStarImageView.setImageResource(R.drawable.feedback_star_filled);
            binding.fourthStarImageView.setImageResource(R.drawable.feedback_star_empty);
            binding.fifthStarImageView.setImageResource(R.drawable.feedback_star_empty);

            setFeedbackUi("3", utils.getStringValue(R.string.rating_3_title),
                    utils.getStringValue(R.string.rating_3), feedbackListData.getRating3());
        });

        binding.fourthStarImageView.setOnClickListener(v -> {
            binding.firstStarImageView.setImageResource(R.drawable.feedback_star_filled);
            binding.secondStarImageView.setImageResource(R.drawable.feedback_star_filled);
            binding.thirdStarImageView.setImageResource(R.drawable.feedback_star_filled);
            binding.fourthStarImageView.setImageResource(R.drawable.feedback_star_filled);
            binding.fifthStarImageView.setImageResource(R.drawable.feedback_star_empty);

            setFeedbackUi("4", utils.getStringValue(R.string.rating_4_title),
                    utils.getStringValue(R.string.rating_4), feedbackListData.getRating4());
        });

        binding.fifthStarImageView.setOnClickListener(v -> {
            binding.firstStarImageView.setImageResource(R.drawable.feedback_star_filled);
            binding.secondStarImageView.setImageResource(R.drawable.feedback_star_filled);
            binding.thirdStarImageView.setImageResource(R.drawable.feedback_star_filled);
            binding.fourthStarImageView.setImageResource(R.drawable.feedback_star_filled);
            binding.fifthStarImageView.setImageResource(R.drawable.feedback_star_filled);

            setFeedbackUi("5", utils.getStringValue(R.string.rating_5_title),
                    utils.getStringValue(R.string.rating_5), feedbackListData.getRating5());
        });

        binding.submitBtn.setOnClickListener(v -> {
            if (checkInput()) {
                showLoading();
                networking.submitCallFeedback(callId, rating, feedbackId, binding.feedbackInput.getText().toString());
            }
        });
    }

    private void setFeedbackUi(String ratingCount, String ratingText, String ratingTitle, List<FeedbackListResponse.Data.Rating> ratingList){
        utils.visibleView(binding.ratingMessageText);
        utils.visibleView(binding.ratingSelectionRecyclerview);
        utils.visibleView(binding.feedbackLayout);
        utils.visibleView(binding.submitBtn);

        for (int i = 0; i < ratingList.size(); i++)
            ratingList.get(i).setFeedbackSelection(false);

        rating = ratingCount;
        binding.ratingText.setText(ratingText);
        binding.ratingMessageText.setText(ratingTitle);

        selectedFeedbackList = new ArrayList<>();
        feedbackId = "";

        binding.ratingSelectionRecyclerview.setLayoutManager(new GridLayoutManager(context, 2));
        feedbackListAdapter = new FeedbackListAdapter(context, activity, ratingList, this);
        binding.ratingSelectionRecyclerview.setAdapter(feedbackListAdapter);
    }

    private boolean checkInput() {
        if (selectedFeedbackList.size() == 0) {
            utils.shortToast(utils.getStringValue(R.string.select_atleast_one_feedback));
            return false;
        }
        return true;
    }

    @Override
    public void selectLanguageLayout(int position, String value) {
    }

    @Override
    public void selectAnimalLayout(int position, String value) {
        feedbackListAdapter.notifyItemChanged(position);
        if (selectedFeedbackList.contains(value))
            selectedFeedbackList.remove(value);
        else
            selectedFeedbackList.add(value);
        feedbackId = selectedFeedbackList.toString().replace("[", "").replace("]", "").replace(" ", "").trim();
    }

    @Override
    public <T> void networkingRequest(@Nullable MethodType methodType, boolean status, @Nullable T error, Object object) {
        if (methodType == MethodType.getFeedbackList && status) {
            dismissLoading();

            selectedFeedbackList = new ArrayList<>();
            feedbackListData = (FeedbackListResponse.Data) object;
            utils.visibleView(binding.mainLayout);
        } else if (methodType == MethodType.submitFeedback && status) {
            dismissLoading();
            utils.shortToast(utils.getStringValue(R.string.thanks_for_giving_feedback));
            activity.onBackPressed();
        } else if (methodType == MethodType.getFeedbackList || methodType == MethodType.submitFeedback && !status)
            dismissLoading();
    }
}
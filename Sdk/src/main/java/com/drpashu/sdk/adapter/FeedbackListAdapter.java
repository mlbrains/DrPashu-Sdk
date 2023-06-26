package com.drpashu.sdk.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.drpashu.sdk.R;
import com.drpashu.sdk.network.model.response.FeedbackListResponse;

import java.util.List;

public class FeedbackListAdapter extends RecyclerView.Adapter<FeedbackListAdapter.Viewholder> {
    Context context;
    Activity activity;
    MultiSelectInterface multiSelectInterface;
    List<FeedbackListResponse.Data.Rating> feedbackList;

    public FeedbackListAdapter(Context context, Activity activity, List<FeedbackListResponse.Data.Rating> feedbackList, MultiSelectInterface multiSelectInterface){
        this.context = context;
        this.activity = activity;
        this.feedbackList = feedbackList;
        this.multiSelectInterface = multiSelectInterface;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view= inflater.inflate(R.layout.item_multi_selection, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        holder.feedbackText.setText(feedbackList.get(position).getReview());
        if (feedbackList.get(position).getFeedbackSelection() == null)
            feedbackList.get(position).setFeedbackSelection(false);
        if (feedbackList.get(position).getFeedbackSelection()) {
            holder.feedbackText.setTextColor(context.getResources().getColor(R.color.white));
            holder.feedbackCardView.setCardBackgroundColor(context.getResources().getColor(R.color.base));
        } else {
            holder.feedbackText.setTextColor(context.getResources().getColor(R.color.base));
            holder.feedbackCardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
        }

        holder.itemView.setOnClickListener(view -> {
            feedbackList.get(position).setFeedbackSelection(!feedbackList.get(position).getFeedbackSelection());
            multiSelectInterface.selectAnimalLayout(position, feedbackList.get(position).getId()+"");
        });
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        private TextView feedbackText;
        private CardView feedbackCardView;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            feedbackText = itemView.findViewById(R.id.textview);
            feedbackCardView = itemView.findViewById(R.id.cardview);
        }
    }
}

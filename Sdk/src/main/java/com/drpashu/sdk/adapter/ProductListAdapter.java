package com.drpashu.sdk.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drpashu.sdk.databinding.ItemRecommendProductBinding;
import com.drpashu.sdk.network.model.response.ProductListResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
    Context context;
    Activity activity;
    List<ProductListResponse.Data> productList;
    Boolean editable;

    public ProductListAdapter(Context context, Activity activity, List<ProductListResponse.Data> productList,
                              Boolean editable) {
        this.context = context;
        this.activity = activity;
        this.productList = productList;
        this.editable = editable;
    }

    @NonNull
    @Override
    public ProductListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemRecommendProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListAdapter.ViewHolder holder, int position) {
        if (productList.get(position).getChecked() == null)
            productList.get(position).setChecked(false);

        if (editable)
            holder.binding.productCheckbox.setVisibility(View.VISIBLE);
        else
            holder.binding.productCheckbox.setVisibility(View.GONE);

        if (productList.get(position).getImage().length() != 0)
            Picasso.get().load(productList.get(position).getImage()).into(holder.binding.productImage);
        holder.binding.productNameText.setText(productList.get(position).getTitle());
        holder.binding.descriptionText.setText(productList.get(position).getBenefit());
        holder.binding.productCheckbox.setChecked(productList.get(position).getChecked());

        holder.binding.productNameText.setOnClickListener(v -> {
            Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(productList.get(position).getDeep_link()));
            activity.startActivity(urlIntent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemRecommendProductBinding binding;

        public ViewHolder(ItemRecommendProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

package com.drpashu.sdk.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drpashu.sdk.R;
import com.drpashu.sdk.databinding.ItemServiceBinding;
import com.drpashu.sdk.network.ApiClient;
import com.drpashu.sdk.network.model.response.ServiceListResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.ViewHolder> {

    Context context;
    Activity activity;
    List<ServiceListResponse.Data> serviceListResponse;
    ServiceListInterface serviceListInterface;
    private int checkedPosition = 0;

    public ServiceListAdapter(Context context, Activity activity, List<ServiceListResponse.Data> serviceListResponse, ServiceListInterface serviceListInterface) {
        this.context = context;
        this.activity = activity;
        this.serviceListResponse = serviceListResponse;
        this.serviceListInterface = serviceListInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemServiceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind();

        if (serviceListResponse.get(position).getService().equalsIgnoreCase("Vet"))
            holder.binding.itemName.setText(context.getResources().getString(R.string.vet));
        else if (serviceListResponse.get(position).getService().equalsIgnoreCase("Artificial Insemination"))
            holder.binding.itemName.setText(context.getResources().getString(R.string.artificial_insemination));
        else if (serviceListResponse.get(position).getService().equalsIgnoreCase("Vaccinations"))
            holder.binding.itemName.setText(context.getResources().getString(R.string.vaccinations));
        else if (serviceListResponse.get(position).getService().equalsIgnoreCase("First Aid"))
            holder.binding.itemName.setText(context.getResources().getString(R.string.first_aid));
        else if (serviceListResponse.get(position).getService().equalsIgnoreCase("Others"))
            holder.binding.itemName.setText(context.getResources().getString(R.string.others));
        else
            holder.binding.itemName.setText(serviceListResponse.get(position).getService());

        if (serviceListResponse.get(position).getImage() != null)
            Picasso.get().load(ApiClient.BASE_URL_MEDIA+serviceListResponse.get(position).getImage()).into(holder.binding.itemImage);

        if (!serviceListResponse.get(position).getActive())
            holder.binding.itemLayout.setBackgroundColor(context.getResources().getColor(R.color.light_grey));
    }

    @Override
    public int getItemCount() {
        return serviceListResponse.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemServiceBinding binding;

        public ViewHolder(ItemServiceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind() {
            if (checkedPosition == -1) {
                binding.itemLayout.setSelected(false);
                binding.itemName.setSelected(false);
            } else {
                if (checkedPosition == getAdapterPosition()) {
                    binding.itemLayout.setSelected(true);
                    binding.itemName.setSelected(true);
                } else {
                    binding.itemLayout.setSelected(false);
                    binding.itemName.setSelected(false);
                }
            }

            itemView.setOnClickListener(v -> {
                if (serviceListResponse.get(getAdapterPosition()).getActive()) {
                    binding.itemLayout.setSelected(true);
                    binding.itemName.setSelected(true);
                    if (checkedPosition != getAdapterPosition()) {
                        notifyItemChanged(checkedPosition);
                        checkedPosition = getAdapterPosition();
                    }
                    serviceListInterface.selectedServicePosition(checkedPosition, serviceListResponse.get(checkedPosition).getId());
                } else
                    Toast.makeText(context, "Coming Soon", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
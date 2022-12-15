package com.chamarw.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chamarw.android.R;
import com.chamarw.android.listeners.ListingItemClickListener;
import com.chamarw.android.models.Listing;

import java.util.List;

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ViewHolder>{

    private final Context mContext;
    private final List<Listing> listingList;
    private final ListingItemClickListener listingItemClickListener;

    public ListingAdapter(Context mContext, List<Listing> listingList, ListingItemClickListener listingItemClickListener){
        this.mContext = mContext;
        this.listingList = listingList;
        this.listingItemClickListener = listingItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.listing_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ((ViewHolder) holder).bind(position);
    }

    @Override
    public int getItemCount() {
        return listingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final ImageView imageView;
        private final TextView textView;
        private final TextView availabilityTextView;
        private final TextView subTextView;
        private final TextView subItemTextView;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            availabilityTextView = itemView.findViewById(R.id.availabilityTextView);
            textView = itemView.findViewById(R.id.textView);
            subTextView = itemView.findViewById(R.id.subTextView);
            subItemTextView = itemView.findViewById(R.id.subItemTextView);
        }

        void bind(int position) {

            Listing listing = listingList.get(position);
            imageView.setTransitionName(listing.getId());
            Glide.with(mContext.getApplicationContext()).load(listing.getPic()).placeholder(R.drawable.placeholder).into(imageView);
            textView.setText(listing.getTitle());
            subTextView.setText(mContext.getString(R.string.value,listing.getValue()));
            subItemTextView.setText(listing.getSummary());
            if (listing.isAvailability()){
                availabilityTextView.setText(R.string.available);
            }else {
                availabilityTextView.setText(R.string.bought);
            }
            itemView.setOnClickListener(v -> listingItemClickListener.onListingItemClick(listing,imageView));

        }
    }


}

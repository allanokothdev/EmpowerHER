package com.chamarw.android.listeners;

import android.widget.ImageView;

import com.chamarw.android.models.Listing;

public interface ListingItemClickListener {

    void onListingItemClick(Listing listing, ImageView imageView);
}

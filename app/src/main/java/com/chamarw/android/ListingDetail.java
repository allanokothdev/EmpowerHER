package com.chamarw.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.chamarw.android.constants.Constants;
import com.chamarw.android.models.Listing;

import java.util.Objects;

public class ListingDetail extends AppCompatActivity implements View.OnClickListener {

    private final Context mContext = ListingDetail.this;
    private Listing listing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_detail);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        listing = (Listing) bundle.getSerializable("object");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(listing.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(v -> finishAfterTransition());

        ImageView imageView = findViewById(R.id.imageView);
        ImageView coverImageView = findViewById(R.id.coverImageView);
        TextView textView = findViewById(R.id.textView);
        TextView subTextView = findViewById(R.id.subTextView);
        TextView locationTextView = findViewById(R.id.locationTextView);
        TextView subItemTextView = findViewById(R.id.subItemTextView);
        Button button = findViewById(R.id.button);

        button.setOnClickListener(this);
        imageView.setTransitionName(listing.getId());
        Glide.with(mContext.getApplicationContext()).load(listing.getPic()).placeholder(R.drawable.placeholder).into(imageView);
        Glide.with(mContext.getApplicationContext()).load(listing.getCertification()).placeholder(R.drawable.placeholder).into(coverImageView);

        textView.setText(listing.getTitle());
        subTextView.setText(String.valueOf(listing.getValue()));
        locationTextView.setText(listing.getLocation());
        subItemTextView.setText(listing.getSummary());

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.button){
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.OBJECT,listing);
            startActivity(new Intent(mContext, CreateChama.class).putExtras(bundle));
        }
    }
}
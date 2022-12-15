package com.chamarw.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chamarw.android.adapters.ListingAdapter;
import com.chamarw.android.constants.Constants;
import com.chamarw.android.listeners.ListingItemClickListener;
import com.chamarw.android.models.Category;
import com.chamarw.android.models.Listing;
import com.chamarw.android.models.Wallet;
import com.chamarw.android.utils.GetUser;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.functions.FirebaseFunctions;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Categories extends AppCompatActivity implements ListingItemClickListener,View.OnClickListener {

    private final Context mContext = Categories.this;
    private final FirebaseFunctions firebaseFunctions = FirebaseFunctions.getInstance();
    private AVLoadingIndicatorView progressBar;
    private final List<Listing> objectList = new ArrayList<>();
    private ListingAdapter adapter;
    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        category = (Category) bundle.getSerializable("object");

        progressBar = findViewById(R.id.progressBar);
        ImageView imageView = findViewById(R.id.imageView);
        ImageView finishImageView = findViewById(R.id.finishImageView);
        TextView textView = findViewById(R.id.textView);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        ExtendedFloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(this);

        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(mContext, R.drawable.posts_divider)));
        recyclerView.addItemDecoration(divider);
        adapter = new ListingAdapter(mContext, objectList,  this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        fetchListings(GetUser.getWallet(mContext));

        imageView.setTransitionName(category.getId());
        textView.setText(category.getTitle());
        finishImageView.setOnClickListener(this);

    }

    private void fetchListings(Wallet wallet){
        Map<String, Object> data = new HashMap<>();
        data.put("key", wallet.getPrivateKey());
        data.put("address", wallet.getAddress());
        data.put("category", category.getTitle());
        firebaseFunctions.getHttpsCallable(Constants.FETCH_LISTINGS).call(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                ArrayList<HashMap> result = (ArrayList<HashMap>) task.getResult().getData();
                //Toast.makeText(mContext, result.toString(), Toast.LENGTH_SHORT).show();

                if (result != null){
                    for (HashMap hashMap: result){
                        String id = Objects.requireNonNull(hashMap.get("id")).toString();
                        String pic = Objects.requireNonNull(hashMap.get("pic")).toString();
                        String certification = Objects.requireNonNull(hashMap.get("certification")).toString();
                        String title = Objects.requireNonNull(hashMap.get("title")).toString();
                        String summary = Objects.requireNonNull(hashMap.get("summary")).toString();
                        String location = Objects.requireNonNull(hashMap.get("location")).toString();
                        int value = Integer.parseInt((Objects.requireNonNull(hashMap.get("value")).toString()));
                        String publisher = Objects.requireNonNull(hashMap.get("publisher")).toString();
                        boolean availability = Boolean.parseBoolean(Objects.requireNonNull(hashMap.get("availability")).toString());
                        String token = Objects.requireNonNull(hashMap.get("token")).toString();
                        ArrayList<String> tags = (ArrayList<String>) hashMap.get("tags");

                        Listing listing = new Listing(id, pic, certification, title, summary, value, location, publisher, availability, token, tags);
                        if (!objectList.contains(listing)){
                            objectList.add(listing);
                            adapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                } else {
                    Toast.makeText(mContext, "Sorry, you don not belong to any Chamas", Toast.LENGTH_SHORT).show();
                }


                progressBar.setVisibility(View.INVISIBLE);

            } else {
                Toast.makeText(mContext, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchListings(GetUser.getWallet(mContext));

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.finishImageView){
            finishAfterTransition();
        } else if (v.getId()==R.id.floatingActionButton){
            startActivity(new Intent(mContext, CreateListing.class));
        }
    }

    @Override
    public void onListingItemClick(Listing listing, ImageView imageView) {
        Intent intent = new Intent(mContext, ListingDetail.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object",listing);
        intent.putExtras(bundle);
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, Pair.create(imageView, listing.getId()));
        startActivity(intent,activityOptionsCompat.toBundle());
    }
}
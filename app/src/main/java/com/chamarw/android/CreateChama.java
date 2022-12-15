package com.chamarw.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.chamarw.android.constants.Constants;
import com.chamarw.android.models.Listing;
import com.chamarw.android.models.Wallet;
import com.chamarw.android.utils.GetUser;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.functions.FirebaseFunctions;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class CreateChama extends AppCompatActivity implements View.OnClickListener {

    private final Context mContext = CreateChama.this;
    private final FirebaseFunctions firebaseFunctions = FirebaseFunctions.getInstance();
    private Button button;
    private TextInputEditText titleTextInputEditText;
    private TextInputEditText summaryTextInputEditText;
    private TextInputEditText goalTextInputEditText;
    private AVLoadingIndicatorView progressBar;
    private Listing listing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chama);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        listing = (Listing) bundle.getSerializable(Constants.OBJECT);

        ImageView imageView = findViewById(R.id.imageView);
        titleTextInputEditText = findViewById(R.id.titleTextInputEditText);
        summaryTextInputEditText = findViewById(R.id.summaryTextInputEditText);
        goalTextInputEditText = findViewById(R.id.valueTextInputEditText);

        Glide.with(mContext.getApplicationContext()).load(listing.getPic()).into(imageView);
        goalTextInputEditText.setText(String.valueOf(listing.getValue()));
        titleTextInputEditText.setText(String.format("%s Chama", listing.getTitle()));
        summaryTextInputEditText.setText(String.format("We created this Chama to invest in %s : %s", listing.getTitle(), listing.getSummary()));
        button = findViewById(R.id.button);
        progressBar = findViewById(R.id.progressBar);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            String title = titleTextInputEditText.getText().toString();
            String summary = summaryTextInputEditText.getText().toString();
            String goal = goalTextInputEditText.getText().toString();

            progressBar.setVisibility(View.VISIBLE);

            if (TextUtils.isEmpty(title)) {
                Toast.makeText(mContext, "Enter Chama Title", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            } else if (TextUtils.isEmpty(summary)) {
                Toast.makeText(mContext, "Enter Chama Summary", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            } else if (TextUtils.isEmpty(goal)) {
                Toast.makeText(mContext, "Enter Chama Total Contribution", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            } else {
                createChama(GetUser.getWallet(mContext),title, summary, goal);
                progressBar.setVisibility(View.VISIBLE);
                button.setEnabled(false);
                finish();
            }
        }
    }


    private void createChama(Wallet wallet, String title, String summary, String goal) {

        Map<String, Object> data = new HashMap<>();
        data.put("id",UUID.randomUUID().toString().substring(0,9));
        data.put("pic", listing.getPic());
        data.put("listingID", listing.getId());
        data.put("title", title);
        data.put("summary", summary);
        data.put("goal", goal);
        data.put("key", wallet.getPrivateKey());
        data.put("address", wallet.getAddress());
        firebaseFunctions.getHttpsCallable(Constants.CREATE_CHAMA).call(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                progressBar.setVisibility(View.GONE);
                Toast.makeText(mContext,"Success", Toast.LENGTH_SHORT).show();

            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(mContext, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
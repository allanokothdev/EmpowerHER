package com.chamarw.android;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.chamarw.android.constants.Constants;
import com.chamarw.android.models.Wallet;
import com.chamarw.android.utils.GetUser;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.functions.FirebaseFunctions;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CreateListing extends AppCompatActivity implements View.OnClickListener {

    public final static int PICK_IMAGE = 1;
    public final static int PICK_CERT = 2;
    private final FirebaseFunctions firebaseFunctions = FirebaseFunctions.getInstance();
    private final Context mContext = CreateListing.this;
    private Uri photoUri = null;
    private Uri certUri = null;
    private ImageView imageView;
    private ImageView certImageView;
    private Button button;
    private TextInputEditText titleTextInputEditText;
    private TextInputEditText summaryTextInputEditText;
    private TextInputEditText valueTextInputEditText;
    private TextInputEditText locationTextInputEditText;
    private Spinner spinner;
    private AVLoadingIndicatorView progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_listing);

        titleTextInputEditText = findViewById(R.id.titleTextInputEditText);
        summaryTextInputEditText = findViewById(R.id.summaryTextInputEditText);
        valueTextInputEditText = findViewById(R.id.valueTextInputEditText);
        locationTextInputEditText = findViewById(R.id.locationTextInputEditText);
        progressBar = findViewById(R.id.progressBar);
        imageView = findViewById(R.id.imageView);
        certImageView = findViewById(R.id.certImageView);
        spinner = findViewById(R.id.spinner);
        button = findViewById(R.id.button);
        Button uploadButton = findViewById(R.id.uploadButton);
        Button certButton = findViewById(R.id.certButton);
        button.setOnClickListener(this);
        uploadButton.setOnClickListener(this);
        certButton.setOnClickListener(this);
        imageView.setOnClickListener(this);

        Places.initialize(mContext, Constants.MAPS_KEY);
        locationTextInputEditText.setFocusable(false);
        locationTextInputEditText.setOnClickListener(v -> {
            List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
            Intent intent1 = new com.google.android.libraries.places.widget.Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(mContext);
            startActivityForResult(intent1,100);
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:

                String title = titleTextInputEditText.getText().toString();
                String summary = summaryTextInputEditText.getText().toString();
                String goal = valueTextInputEditText.getText().toString();
                String location = locationTextInputEditText.getText().toString();
                String category = spinner.getSelectedItem().toString();
                progressBar.setVisibility(View.VISIBLE);

                if (TextUtils.isEmpty(title)){
                    Toast.makeText(mContext, "Enter Asset Title", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    break;
                }else if (TextUtils.isEmpty(summary)){
                    Toast.makeText(mContext, "Enter Asset Summary", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    break;
                }else if (TextUtils.isEmpty(goal)){
                    Toast.makeText(mContext, "Select Asset Value", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    break;
                }else if (TextUtils.isEmpty(location)){
                    Toast.makeText(mContext, "Select Asset Location", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    break;
                } else if (TextUtils.isEmpty(category)) {
                    Toast.makeText(mContext, "Select Asset Category", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    break;
                } else if (certUri == null) {
                    Toast.makeText(mContext, "Upload Asset Ownership Certificate", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    break;
                } else if (photoUri == null) {
                    Toast.makeText(mContext, "Upload Asset Photo", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    break;
                }else {
                    uploadObject(GetUser.getWallet(mContext),title,summary,goal,location,category);
                    progressBar.setVisibility(View.VISIBLE);
                    button.setEnabled(false);
                    break;
                }
            case R.id.uploadButton:
                try {
                    if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CreateListing.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else { pickProfileImage();break; }
                }catch (Exception e){ e.printStackTrace();}
                break;
            case R.id.certButton:
                try {
                    if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CreateListing.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else { pickCertificate();break; }
                }catch (Exception e){ e.printStackTrace();}
                break;
        }
    }


    private void uploadObject(Wallet wallet, String title, String summary, String goal, String location, String category) {

        Map<String, Object> data = new HashMap<>();
        data.put("id",UUID.randomUUID().toString().substring(0,9));
        data.put("photoUri", photoUri);
        data.put("certUri", certUri);
        data.put("title", title);
        data.put("summary", summary);
        data.put("goal", goal);
        data.put("location", location);
        data.put("category", category);
        data.put("key", wallet.getPrivateKey());
        data.put("address", wallet.getAddress());
        firebaseFunctions.getHttpsCallable(Constants.CREATE_LISTING).call(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                progressBar.setVisibility(View.GONE);
                Toast.makeText(mContext,"Success", Toast.LENGTH_SHORT).show();

            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(mContext,task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void pickProfileImage() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Profile Pic"),PICK_IMAGE);
    }

    private void pickCertificate() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Certificate"),PICK_CERT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 100 && resultCode == RESULT_OK && data != null){
                Place place = com.google.android.libraries.places.widget.Autocomplete.getPlaceFromIntent(data);
                locationTextInputEditText.setText(place.getAddress());
            }else if (resultCode == AutocompleteActivity.RESULT_ERROR){
                assert data != null;
                Status status = com.google.android.libraries.places.widget.Autocomplete.getStatusFromIntent(data);
                assert status.getStatusMessage() != null;
                Toast.makeText(mContext, status.getStatusMessage(),Toast.LENGTH_SHORT).show();
            }else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {
                photoUri = data.getData();
                imageView.setImageURI(photoUri);
            }else if (requestCode == PICK_CERT && resultCode == RESULT_OK && null != data) {
                certUri = data.getData();
                certImageView.setImageURI(certUri);
            }
        }catch (Exception e){ e.printStackTrace();}
    }
}
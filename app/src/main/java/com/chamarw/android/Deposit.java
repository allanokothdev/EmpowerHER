package com.chamarw.android;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chamarw.android.constants.Constants;
import com.chamarw.android.models.Chama;
import com.chamarw.android.models.Wallet;
import com.chamarw.android.utils.GetUser;
import com.chamarw.android.utils.ScreenUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.functions.FirebaseFunctions;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Deposit extends AppCompatActivity implements View.OnClickListener {

    private Chama chama;
    private TextInputEditText fromAmountTextInputEditText;
    private TextInputEditText toAmountTextInputEditText;
    private final Context mContext = Deposit.this;
    private AVLoadingIndicatorView progressBar;
    private int balance = 0;
    private Button button;
    private final FirebaseFunctions firebaseFunctions = FirebaseFunctions.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        chama = (Chama) bundle.getSerializable(Constants.OBJECT);

        progressBar = findViewById(R.id.progressBar);
        ImageView closeImageView = findViewById(R.id.closeImageView);
        fromAmountTextInputEditText = findViewById(R.id.fromAmountTextInputEditText);
        TextView fromBalanceTextView = findViewById(R.id.fromBalanceTextView);
        TextView toBalanceTextView = findViewById(R.id.toBalanceTextView);
        toAmountTextInputEditText = findViewById(R.id.toAmountTextInputEditText);
        button = findViewById(R.id.button);
        closeImageView.setOnClickListener(this);
        button.setOnClickListener(this);

        toBalanceTextView.setText(mContext.getString(R.string.savings,Double.parseDouble(GetUser.fetchObject(mContext,chama.getId())),"MATIC"));
        balance = (int) Double.parseDouble(GetUser.fetchObject(mContext,Constants.MATIC_BALANCE));
        fromBalanceTextView.setText(mContext.getString(R.string.balanced,Double.parseDouble(GetUser.fetchObject(mContext,Constants.MATIC_BALANCE)),"MATIC"));
        String savings = GetUser.fetchObject(mContext,chama.getId());

        fromAmountTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (Integer.parseInt(s.toString())>balance){
                        fromAmountTextInputEditText.setError("Insufficient Balance");
                        fromAmountTextInputEditText.requestFocus();
                        button.setEnabled(false);
                    } else {
                        toAmountTextInputEditText.setText(String.format("%s + %s", savings, s.toString()));
                        button.setEnabled(true);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }@Override public void afterTextChanged(Editable s) { }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.closeImageView){
            finishAfterTransition();
        } else if (v.getId()==R.id.button){
            button.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
            depositMoney(GetUser.getWallet(mContext),fromAmountTextInputEditText.getText().toString());
        }
    }

    private void depositMoney(Wallet wallet, String value) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", chama.getId());
        data.put("value", value);
        data.put("key", wallet.getPrivateKey());
        firebaseFunctions.getHttpsCallable(Constants.DEPOSIT_MONEY).call(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                progressBar.setVisibility(View.GONE);
                button.setEnabled(true);
                Toast.makeText(mContext,"Success", Toast.LENGTH_SHORT).show();

                Map<String, Object> result = (Map<String, Object>) task.getResult().getData();
                assert result != null;
                String responses = Objects.requireNonNull(result.get("link")).toString();
                String savings = Objects.requireNonNull(result.get("totalSavings")).toString();
                GetUser.saveObject(mContext,chama.getId(),savings);
                progressBar.setVisibility(View.INVISIBLE);
                confirmTransaction(responses);

            } else {
                button.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(mContext,task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmTransaction(String url){
        @SuppressLint("InflateParams") View view = LayoutInflater.from(mContext).inflate(R.layout.bottom_sheet_confirm, null);
        ImageView imageView = view.findViewById(R.id.imageView);
        TextView textView = view.findViewById(R.id.textView);
        TextView subTextView = view.findViewById(R.id.subTextView);

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) view.getParent());
        ScreenUtils screenUtils = new ScreenUtils(mContext);
        behavior.setPeekHeight(screenUtils.getHeight());

        imageView.setImageResource(R.drawable.ic_done_gr);
        textView.setText("Transaction was completed successfully");
        subTextView.setOnClickListener(view12 -> {
            try {
                Intent indie = new Intent(Intent.ACTION_VIEW);
                indie.setData(Uri.parse(url));
                mContext.startActivity(indie);
                bottomSheetDialog.dismiss();
            } catch (Exception e){
                e.printStackTrace();
            }
        });
    }
}
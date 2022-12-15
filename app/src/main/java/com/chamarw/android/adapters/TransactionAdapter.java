package com.chamarw.android.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chamarw.android.R;
import com.chamarw.android.constants.Constants;
import com.chamarw.android.models.Transaction;
import com.chamarw.android.utils.ScreenUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class TransactionAdapter extends RecyclerView.Adapter{

    private final Context mContext;
    private final List<Transaction> stringList;

    public TransactionAdapter(Context mContext, List<Transaction> stringList) {
        this.stringList = stringList;
        this.mContext = mContext;
    }

    @Override
    public int getItemViewType(int position) {
        return Constants.DEPOSIT_TX;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DepositViewHolder(LayoutInflater.from(mContext).inflate(R.layout.transaction_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        ((DepositViewHolder) holder).bind(position);
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public class DepositViewHolder extends RecyclerView.ViewHolder{

        private final CircleImageView imageView;
        private final TextView textView;
        private final TextView subTextView;
        private final TextView subItemTextView;

        private DepositViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            subTextView = itemView.findViewById(R.id.subTextView);
            subItemTextView = itemView.findViewById(R.id.subItemTextView);
        }

        void bind(int position) {

            Transaction transaction = stringList.get(position);
            Glide.with(mContext.getApplicationContext()).load(R.drawable.logo).placeholder(R.drawable.logo).into(imageView);
            textView.setText(R.string.deposit);
            subTextView.setText(transaction.getValue());
            subItemTextView.setText(transaction.getValue());
            itemView.setOnClickListener(v -> viewTransaction(transaction));

        }
    }

    private void viewTransaction(Transaction transaction){

        @SuppressLint("InflateParams") View view = LayoutInflater.from(mContext).inflate(R.layout.bottom_sheet_view, null);
        Button button = view.findViewById(R.id.button);
        ImageView closeImageView = view.findViewById(R.id.closeImageView);
        ImageView optionsImageView = view.findViewById(R.id.optionsImageView);
        ImageView imageView = view.findViewById(R.id.imageView);
        TextView textView = view.findViewById(R.id.textView);
        TextView subTextView = view.findViewById(R.id.subTextView);
        TextView subItemTextView = view.findViewById(R.id.subItemTextView);
        TextView receiverTextView = view.findViewById(R.id.receiverTextView);
        TextView senderTextView = view.findViewById(R.id.senderTextView);

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) view.getParent());
        ScreenUtils screenUtils = new ScreenUtils(mContext);
        behavior.setPeekHeight(screenUtils.getHeight());

        Glide.with(mContext.getApplicationContext()).load(R.drawable.sendmoney).into(imageView);
        textView.setText(transaction.getId());
        subTextView.setText(transaction.getTimestamp());
        String formattedValue = String.format(Locale.ENGLISH,"%.2f",Double.parseDouble(transaction.getValue()));
        subItemTextView.setText(mContext.getString(R.string.token_price,formattedValue,"MATIC"));
        senderTextView.setText(transaction.getSender());
        receiverTextView.setText(transaction.getReceiver());
        closeImageView.setOnClickListener(v -> bottomSheetDialog.dismiss());
        optionsImageView.setOnClickListener(v -> {
            Intent indie = new Intent(Intent.ACTION_VIEW);
            indie.setData(Uri.parse(transaction.getLink()));
            mContext.startActivity(indie);
        });
        button.setOnClickListener(v -> {
            Intent indie = new Intent(Intent.ACTION_VIEW);
            indie.setData(Uri.parse(transaction.getLink()));
            mContext.startActivity(indie);
        });
    }
}

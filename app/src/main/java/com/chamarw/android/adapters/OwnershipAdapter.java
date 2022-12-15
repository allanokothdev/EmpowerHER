package com.chamarw.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chamarw.android.R;
import com.chamarw.android.models.Ownership;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class OwnershipAdapter extends RecyclerView.Adapter<OwnershipAdapter.ViewHolder>{

    private final Context mContext;
    private final List<Ownership> stringList;
    public OwnershipAdapter(Context mContext, List<Ownership> stringList) {
        this.stringList = stringList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final CircleImageView imageView;
        private final TextView textView;
        private final TextView subTextView;
        private final TextView subItemTextView;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            subTextView = itemView.findViewById(R.id.subTextView);
            subItemTextView = itemView.findViewById(R.id.subItemTextView);
        }

        void bind(int position) {

            Ownership ownership = stringList.get(position);
            Glide.with(mContext.getApplicationContext()).load(ownership.getPic()).placeholder(R.drawable.logo).into(imageView);
            textView.setText(ownership.getAddress());
            subTextView.setText(ownership.getAddress());
            subItemTextView.setText(ownership.getAddress());
        }
    }

}

package com.chamarw.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chamarw.android.R;
import com.chamarw.android.listeners.ChamaItemClickListener;
import com.chamarw.android.models.Chama;

import java.util.List;

public class ChamaAdapter extends RecyclerView.Adapter<ChamaAdapter.ViewHolder>{

    private final Context mContext;
    private final List<Chama> objectList;
    private final ChamaItemClickListener chamaItemClickListener;

    public ChamaAdapter(Context mContext, List<Chama> objectList, ChamaItemClickListener chamaItemClickListener){
        this.mContext = mContext;
        this.objectList = objectList;
        this.chamaItemClickListener = chamaItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chama_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final ImageView imageView;
        private final TextView textView;
        private final TextView subItemTextView;
        private final SeekBar seekBar;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            seekBar = itemView.findViewById(R.id.seekbar);
            textView = itemView.findViewById(R.id.textView);
            subItemTextView = itemView.findViewById(R.id.subItemTextView);
        }

        void bind(int position) {
            Chama chama = objectList.get(position);
            imageView.setTransitionName(chama.getId());
            Glide.with(mContext.getApplicationContext()).load(chama.getPic()).placeholder(R.drawable.placeholder).into(imageView);
            textView.setText(chama.getSummary());
            subItemTextView.setText(chama.getSummary());
            seekBar.setProgress(chama.getSavings());
            seekBar.setMax(chama.getGoal());
            itemView.setOnClickListener(v -> chamaItemClickListener.onChamaItemClick(chama,imageView));

        }
    }

}

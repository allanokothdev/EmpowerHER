package com.chamarw.android.listeners;

import android.widget.ImageView;

import com.chamarw.android.models.User;

public interface UserItemClickListener {
    void onUserItemClick(User user, ImageView imageView);
}

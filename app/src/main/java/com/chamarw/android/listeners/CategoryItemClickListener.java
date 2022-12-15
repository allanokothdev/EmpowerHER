package com.chamarw.android.listeners;


import androidx.cardview.widget.CardView;

import com.chamarw.android.models.Category;

public interface CategoryItemClickListener {

    void onCategoryItemClick(Category category, CardView cardView);
}

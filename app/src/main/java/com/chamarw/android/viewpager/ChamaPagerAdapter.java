package com.chamarw.android.viewpager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.chamarw.android.fragments.MemberFragment;
import com.chamarw.android.fragments.OwnershipFragment;
import com.chamarw.android.fragments.TransactionFragment;
import com.chamarw.android.models.Chama;

public class ChamaPagerAdapter extends FragmentStateAdapter  {

    private final Chama chama;

    public ChamaPagerAdapter(@NonNull FragmentActivity fragmentActivity, Chama chama) {
        super(fragmentActivity);
        this.chama = chama;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return TransactionFragment.getInstance(chama);
            case 1:
                return MemberFragment.getInstance(chama);
            case 2:
                return OwnershipFragment.getInstance(chama);
        }
        return null;
    }



    @Override
    public int getItemCount() {
        return 3;
    }


}

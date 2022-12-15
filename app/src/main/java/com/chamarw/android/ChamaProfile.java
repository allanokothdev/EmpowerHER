package com.chamarw.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.chamarw.android.constants.Constants;
import com.chamarw.android.models.Chama;
import com.chamarw.android.viewpager.ChamaPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

public class ChamaProfile extends AppCompatActivity implements View.OnClickListener {

    private final Context mContext = ChamaProfile.this;
    private Chama chama;
    public static String[] tabList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chama_profile);

        try {
            tabList = getResources().getStringArray(R.array.tabs);
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            chama = (Chama) bundle.getSerializable(Constants.OBJECT);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            Objects.requireNonNull(getSupportActionBar()).setTitle(chama.getTitle());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setNavigationOnClickListener(v -> finishAfterTransition());

            ImageView imageView = findViewById(R.id.imageView);
            TextView textView = findViewById(R.id.textView);
            ImageView qrImageView = findViewById(R.id.qrimageView);
            TextView subTextView = findViewById(R.id.subTextView);
            SeekBar seekBar = findViewById(R.id.seekbar);
            TabLayout tabLayout = findViewById(R.id.tabLayout);
            ViewPager2 viewPager = findViewById(R.id.viewPager);
            FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
            imageView.setTransitionName(chama.getId());

            Glide.with(mContext.getApplicationContext()).load(chama.getPic()).into(imageView);
            textView.setText(chama.getPic());
            subTextView.setText(chama.getSummary());
            seekBar.setProgress(chama.getSavings());
            seekBar.setPressed(false);
            seekBar.setMax(chama.getGoal());
            setUpViewPager(viewPager,tabLayout);

            qrImageView.setOnClickListener(this);
            floatingActionButton.setOnClickListener(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.OBJECT,chama);
        if (v.getId()==R.id.qrimageView){
            startActivity(new Intent(mContext, QRDetail.class).putExtras(bundle));
        }else if (v.getId()==R.id.floatingActionButton){
            startActivity(new Intent(mContext, Deposit.class).putExtras(bundle));
        }
    }


    private void setUpViewPager(ViewPager2 viewPager, TabLayout tabLayout) {
        ChamaPagerAdapter adapter = new ChamaPagerAdapter(this, chama);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(5);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(tabList[position])).attach();
        for (int i = 0; i < tabLayout.getTabCount(); i++){
            TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.custom_tab, null);
            tabLayout.getTabAt(i).setCustomView(tv);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}






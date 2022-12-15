package com.chamarw.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chamarw.android.adapters.CategoryAdapter;
import com.chamarw.android.adapters.ChamaAdapter;
import com.chamarw.android.constants.Constants;
import com.chamarw.android.listeners.CategoryItemClickListener;
import com.chamarw.android.listeners.ChamaItemClickListener;
import com.chamarw.android.models.Category;
import com.chamarw.android.models.Chama;
import com.chamarw.android.models.Token;
import com.chamarw.android.models.User;
import com.chamarw.android.models.Wallet;
import com.chamarw.android.utils.GetUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ChamaItemClickListener, CategoryItemClickListener {

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final FirebaseFunctions firebaseFunctions = FirebaseFunctions.getInstance();

    private final String TAG = this.getClass().getSimpleName();
    private long pressedTime;
    private final Context mContext = MainActivity.this;
    private ImageView moreImageView;
    private User user;

    private final String currentUserID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    private final List<Chama> objectList = new ArrayList<>();
    private ChamaAdapter adapter;
    private AVLoadingIndicatorView progressBar;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = GetUser.getUser(mContext, currentUserID);
        getCategories();

        progressBar = findViewById(R.id.progressBar);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.HORIZONTAL));
        adapter = new ChamaAdapter(mContext, objectList,  this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.VISIBLE);
        fetchChamas(GetUser.getWallet(mContext));
        moreImageView = findViewById(R.id.moreImageView);
        moreImageView.setOnClickListener(this);
        fetchUserInfo(currentUserID);
    }

    private void getCategories(){
        ArrayList<Category> categoryArrayList = new ArrayList<>();
        RecyclerView categoryRecyclerView = findViewById(R.id.categoriesRecycleView);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(mContext,RecyclerView.HORIZONTAL,false));
        categoryArrayList.add(new Category("1","Real Estate"));
        categoryArrayList.add(new Category("2","Land"));
        categoryArrayList.add(new Category("3","Automobile"));
        categoryArrayList.add(new Category("4","Apartment"));
        categoryArrayList.add(new Category("5","Business"));
        CategoryAdapter categoryAdapter = new CategoryAdapter(mContext, categoryArrayList, this);
        categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.setAdapter(categoryAdapter);

    }


    private void fetchChamas(Wallet wallet){
        Map<String, Object> data = new HashMap<>();
        data.put("key", wallet.getPrivateKey());
        data.put("address", wallet.getAddress());
        firebaseFunctions.getHttpsCallable(Constants.FETCH_CHAMAS).call(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                ArrayList<HashMap> result = (ArrayList<HashMap>) task.getResult().getData();
                //Toast.makeText(mContext, result.toString(), Toast.LENGTH_SHORT).show();

                if (result != null){
                    for (HashMap hashMap: result){
                        String id = Objects.requireNonNull(hashMap.get("id")).toString();
                        String pic = Objects.requireNonNull(hashMap.get("pic")).toString();
                        String title = Objects.requireNonNull(hashMap.get("title")).toString();
                        String summary = Objects.requireNonNull(hashMap.get("summary")).toString();
                        String publisher = Objects.requireNonNull(hashMap.get("publisher")).toString();
                        int savings = Integer.parseInt((Objects.requireNonNull(hashMap.get("savings")).toString()));
                        int goal = Integer.parseInt((Objects.requireNonNull(hashMap.get("goal")).toString()));
                        boolean open = Boolean.parseBoolean(Objects.requireNonNull(hashMap.get("open")).toString());
                        ArrayList<String> tags = (ArrayList<String>) hashMap.get("tags");

                        Chama chama = new Chama(id, pic, title, summary, publisher, savings, goal, tags ,open);
                        if (!objectList.contains(chama)){
                            objectList.add(chama);
                            adapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                } else {
                    Toast.makeText(mContext, "Sorry, you don not belong to any Chamas", Toast.LENGTH_SHORT).show();
                }


                progressBar.setVisibility(View.INVISIBLE);

            } else {
                Toast.makeText(mContext, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchChamas(GetUser.getWallet(mContext));
        getCategories();
    }


    @Override public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else {
            Toast.makeText(mContext, "Press Back Again to Exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();

    }

    private void fetchUserInfo(String currentUserID){
        FirebaseFirestore.getInstance().collection(Constants.USERS).document(currentUserID).get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()){
                user = documentSnapshot.toObject(User.class);
                assert user != null;
                GetUser.saveUser(mContext,user);
                fetchToken(user);
            }
        });
    }


    private void fetchToken(User user){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                String token = task.getResult();
                String msg = getString(R.string.msg_token_fmt, token);
                Timber.d(msg);

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("token",token);
                FirebaseDatabase.getInstance().getReference(Constants.TOKEN).child(currentUserID).updateChildren(hashMap);

                Token groupToken = new Token(token);
                FirebaseDatabase.getInstance().getReference(Constants.GROUP_TOKENS).child("j").child(user.getId()).setValue(groupToken);

                SharedPreferences.Editor editor = getSharedPreferences(Constants.USERS,Context.MODE_PRIVATE).edit();
                editor.putString(Constants.TOKEN, token);
                editor.apply();
                GetUser.saveObject(mContext,Constants.TOKEN,token);

                HashMap<String, Object> userMap = new HashMap<>();
                userMap.put("token",token);
                FirebaseFirestore.getInstance().collection(Constants.USERS).document(currentUserID).update(userMap);

            }else {
                Timber.tag(TAG).w(task.getException(), "Fetching FCM registration token failed");
            }
        });
    }

    private void subscribeTopic(String topic){
        FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnCompleteListener(task -> { });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.moreImageView) {
            PopupMenu popupMen = new PopupMenu(mContext, moreImageView);
            popupMen.getMenuInflater().inflate(R.menu.menu_main, popupMen.getMenu());
            popupMen.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_signout) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Sign Out");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Yes", (dialog, which) -> {
                        firebaseAuth.signOut();
                        finishAfterTransition();
                    });
                    builder.setNegativeButton("No", (dialog, which) -> {
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                return true;
            });
            popupMen.show();
        }
    }


    @Override
    public void onChamaItemClick(Chama chama, ImageView imageView) {
        Intent intent = new Intent(mContext, ChamaProfile.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object",chama);
        intent.putExtras(bundle);
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, Pair.create(imageView, chama.getId()));
        startActivity(intent,activityOptionsCompat.toBundle());
    }

    @Override
    public void onCategoryItemClick(Category category, CardView cardView) {
        Intent intent = new Intent(mContext, Categories.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object",category);
        intent.putExtras(bundle);
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, Pair.create(cardView, category.getId()));
        startActivity(intent,activityOptionsCompat.toBundle());
    }
}
package com.chamarw.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chamarw.android.R;
import com.chamarw.android.adapters.UserAdapter;
import com.chamarw.android.constants.Constants;
import com.chamarw.android.models.Chama;
import com.chamarw.android.models.User;
import com.chamarw.android.models.Wallet;
import com.chamarw.android.utils.GetUser;
import com.chamarw.android.utils.RecyclerItemDecoration;
import com.google.firebase.functions.FirebaseFunctions;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MemberFragment extends Fragment {

    private UserAdapter adapter;
    private Chama chama;
    private final FirebaseFunctions firebaseFunctions = FirebaseFunctions.getInstance();
    private final List<User> objectList = new ArrayList<>();
    private AVLoadingIndicatorView progressBar;

    public MemberFragment() {
        // Required empty public constructor
    }

    public static MemberFragment getInstance(Chama chama){
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.OBJECT, chama);
        MemberFragment fragment = new MemberFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        chama = (Chama) getArguments().getSerializable(Constants.OBJECT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        adapter = new UserAdapter(getContext(), objectList);
        recyclerView.setAdapter(adapter);
        fetchMembers(GetUser.getWallet(requireContext()));
        return view;
    }

    private void fetchMembers(Wallet wallet){
        Map<String, Object> data = new HashMap<>();
        data.put("key", wallet.getPrivateKey());
        data.put("address", wallet.getAddress());
        data.put("chama",chama.getId());
        firebaseFunctions.getHttpsCallable(Constants.FETCH_MEMBERS).call(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                ArrayList<HashMap> result = (ArrayList<HashMap>) task.getResult().getData();
                //Toast.makeText(mContext, result.toString(), Toast.LENGTH_SHORT).show();

                if (result != null){
                    for (HashMap hashMap: result){
                        String id = Objects.requireNonNull(hashMap.get("id")).toString();
                        String pic = Objects.requireNonNull(hashMap.get("pic")).toString();
                        String name = Objects.requireNonNull(hashMap.get("name")).toString();
                        String address = Objects.requireNonNull(hashMap.get("address")).toString();
                        String token = Objects.requireNonNull(hashMap.get("token")).toString();
                        User user = new User(id,address,pic,name,token);
                        if (!objectList.contains(user)){
                            objectList.add(user);
                            adapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Sorry, There are no members in this Chama", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
            } else {
                Toast.makeText(requireContext(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchMembers(GetUser.getWallet(requireContext()));
    }


}
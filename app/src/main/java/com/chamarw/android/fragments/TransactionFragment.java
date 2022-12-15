package com.chamarw.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chamarw.android.R;
import com.chamarw.android.adapters.TransactionAdapter;
import com.chamarw.android.constants.Constants;
import com.chamarw.android.models.Chama;
import com.chamarw.android.models.Transaction;
import com.chamarw.android.utils.RecyclerItemDecoration;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class TransactionFragment extends Fragment {

    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private ListenerRegistration registration;
    private TransactionAdapter adapter;
    private Chama chama;
    private final List<Transaction> objectList = new ArrayList<>();

    public TransactionFragment() {
        // Required empty public constructor
    }

    public static TransactionFragment getInstance(Chama chama){
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.OBJECT, chama);
        TransactionFragment fragment = new TransactionFragment();
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
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerItemDecoration recyclerItemDecoration = new RecyclerItemDecoration(requireContext(),getResources().getDimensionPixelSize(R.dimen.header_height),true,getSectionCallback(objectList));
        recyclerView.addItemDecoration(recyclerItemDecoration);
        adapter = new TransactionAdapter(getContext(), objectList);
        recyclerView.setAdapter(adapter);
        fetchObjects(chama.getId());
        return view;
    }

    private RecyclerItemDecoration.SectionCallback getSectionCallback(final List<Transaction> objectList) {
        return new RecyclerItemDecoration.SectionCallback() {
            @Override public boolean isSection(int pos) {
                return pos==0 || !objectList.get(pos).getTimestamp().equals(objectList.get(pos - 1).getTimestamp());
            }
            @Override public String getSectionHeaderName(int pos) {
                Transaction transaction = objectList.get(pos);
                return transaction.getTimestamp();
            }
        };
    }

    private void fetchObjects(String objectID){
        Query query = firebaseFirestore.collection(Constants.TRANSACTIONS).orderBy("timestamp", Query.Direction.DESCENDING).whereArrayContains("tags",objectID);
        registration = query.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (queryDocumentSnapshots != null){
                for (DocumentChange documentChange: queryDocumentSnapshots.getDocumentChanges()){
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        Transaction object = documentChange.getDocument().toObject(Transaction.class);
                        if (!objectList.contains(object)){
                            objectList.add(object);
                            adapter.notifyDataSetChanged();
                        }
                    }else if (documentChange.getType()==DocumentChange.Type.MODIFIED){
                        Transaction object = documentChange.getDocument().toObject(Transaction.class);
                        if (objectList.contains(object)){
                            objectList.set(objectList.indexOf(object),object);
                            adapter.notifyItemChanged(objectList.indexOf(object));
                        }
                    }else if (documentChange.getType()==DocumentChange.Type.REMOVED){
                        Transaction object = documentChange.getDocument().toObject(Transaction.class);
                        if (objectList.contains(object)){
                            objectList.remove(object);
                            adapter.notifyItemRemoved(objectList.indexOf(object));
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchObjects(chama.getId());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (registration != null){
            registration.remove();
        }
    }
}
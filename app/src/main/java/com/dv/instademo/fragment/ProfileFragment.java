package com.dv.instademo.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dv.instademo.R;
import com.dv.instademo.adapter.FeedListAdapter;
import com.dv.instademo.helper.ItemClickInterface;
import com.dv.instademo.model.SampleFeed;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment implements View.OnClickListener, ItemClickInterface {
    private View view;
    private FeedListAdapter mAdapter;
    RecyclerView mRecyclerView;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private ArrayList<SampleFeed> sampleFeedList;
    private ProgressDialog dialog;
    private FirebaseStorage mStorage;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        dialog=new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        mRecyclerView  = view.findViewById(R.id.recyclerView);
        ImageView imageViewAdd = view.findViewById(R.id.imageAdd);
        imageViewAdd.setOnClickListener(this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setUpData();

    }

    private void setUpData() {
        dialog.show();
        sampleFeedList = new ArrayList<>();
        mAdapter = new FeedListAdapter(sampleFeedList, getActivity(),this);
        mRecyclerView.setAdapter(mAdapter);
        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("insta_uploads");

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                sampleFeedList.clear();

                for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
                    SampleFeed upload = teacherSnapshot.getValue(SampleFeed.class);
                    if (upload != null) {
                        upload.setKey(teacherSnapshot.getKey());
                    }
                    sampleFeedList.add(upload);
                }
                mAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageAdd:

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                CreateFeedFragment createFeedFragment = new CreateFeedFragment();
                fragmentTransaction.addToBackStack(null);
                Bundle bundle = new Bundle();
                bundle.putString("flag", "create");
                createFeedFragment.setArguments(bundle);
                fragmentTransaction.add(R.id.frameContainer, createFeedFragment);
                fragmentTransaction.commit();

                break;

            default:
                break;
        }

    }

    @Override
    public void passData(int position,View view) {
        PopupMenu popup = new PopupMenu(getActivity(),view);

        popup.inflate(R.menu.options_menu);

        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu1:
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    CreateFeedFragment createFeedFragment = new CreateFeedFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("flag", "edit");
                    bundle.putString("title", sampleFeedList.get(position).getTitle());
                    bundle.putString("description", sampleFeedList.get(position).getDescription());
                    bundle.putString("imagepath", sampleFeedList.get(position).getImage());
                    bundle.putString("id", sampleFeedList.get(position).getKey());
                    createFeedFragment.setArguments(bundle);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.add(R.id.frameContainer, createFeedFragment);
                    fragmentTransaction.commit();
                    break;
                case R.id.menu2:
                    SampleFeed sampleFeed = sampleFeedList.get(position);
                    final String selectedKey = sampleFeed.getKey();


                        mDatabaseRef.child(selectedKey).removeValue();
                        Toast.makeText(getActivity(), "Item deleted", Toast.LENGTH_SHORT).show();

                    break;

            }
            return false;
        });
        popup.show();
    }
}

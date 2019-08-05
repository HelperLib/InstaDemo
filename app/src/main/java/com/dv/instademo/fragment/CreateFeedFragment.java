package com.dv.instademo.fragment;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dv.instademo.R;
import com.dv.instademo.activity.MainActivity;
import com.dv.instademo.helper.ImagePickerHelper;
import com.dv.instademo.model.SampleFeed;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CreateFeedFragment extends Fragment implements ImagePickerHelper.ImageAttachmentListener {
    private View view;
    private EditText editTextTitle, editTextDescription;
    private ImageView imageViewProfile;
    private ImagePickerHelper imagePickerHelper;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private Uri filePath;
    List<SampleFeed> sampleFeedList;
    String imagePath;
    private String flags, title, description,id;
    Button buttonSubmit;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.create_feed, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            flags = bundle.getString("flag");
            title = bundle.getString("title");
            id = bundle.getString("id");
            description = bundle.getString("description");
            imagePath = bundle.getString("imagepath");
        }

        imagePickerHelper = new ImagePickerHelper(getActivity(), CreateFeedFragment.this, true);
        mStorageRef = FirebaseStorage.getInstance().getReference("insta_uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("insta_uploads");
        sampleFeedList = new ArrayList<>();
        initView();
        if (flags != null) {
            if (flags.equals("edit")) {
                setUpData();
            }else {
                buttonSubmit.setText("Submit");
            }
        }
    }

    private void setUpData() {

        editTextTitle.setText(title);
        editTextDescription.setText(description);
        Glide.with(this)
                .asBitmap()
                .load(imagePath)
                .into(imageViewProfile);

        buttonSubmit.setText("Update");
    }

    private void initView() {
        buttonSubmit  = view.findViewById(R.id.buttonSubmit);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        editTextTitle = view.findViewById(R.id.editTextTitle);
        imageViewProfile = view.findViewById(R.id.imageViewProfile);
        buttonSubmit.setOnClickListener(v -> {
            if(flags.equals("edit")){
                editFeed(id,editTextTitle.getText().toString(),imagePath,editTextDescription.getText().toString());
            }else {
                uploadFile();
            }

        });
        imageViewProfile.setOnClickListener(v -> imagePickerHelper.imagepicker(1));
    }

    private boolean editFeed(String id, String name,String imagePath, String genre) {


        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("insta_uploads").child(id);

        //updating artist
        SampleFeed artist = new SampleFeed(id, name,imagePath, genre);
        dR.setValue(artist);
        Toast.makeText(getActivity(), "Artist Updated", Toast.LENGTH_LONG).show();
        return true;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePickerHelper.onActivityResult(requestCode, resultCode, data);
        filePath = data.getData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePickerHelper.request_permission_result(requestCode, permissions, grantResults);
    }

    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
        try {


            String path = Environment.getExternalStorageDirectory() + File.separator + "ImageAttach" + File.separator;

            String image;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();


            image = System.currentTimeMillis() + "_back.jpg";
            imagePickerHelper.createImage(file, image, path, false);
            imageViewProfile.setImageBitmap(file);
            imagePath = path + image;
            file.compress(Bitmap.CompressFormat.PNG, 130, stream);
            Glide.with(this)
                    .asBitmap()
                    .load(stream.toByteArray())
                    .into(imageViewProfile);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private void uploadFile() {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            String key = database.getReference("quiz").push().getKey();
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(filePath));

            fileReference.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        SampleFeed upload = new SampleFeed(key, editTextTitle.getText().toString(), imagePath, editTextDescription.getText().toString());
                        String uploadId = mDatabaseRef.push().getKey();
                        if (uploadId != null) {
                            mDatabaseRef.child(uploadId).setValue(upload);
                        }
                        progressDialog.dismiss();


                        Toast.makeText(getActivity(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        ((MainActivity) getActivity()).onBack();
                    })
                    .addOnFailureListener(exception -> {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    });
        } else {
            Toast.makeText(getActivity(), "You haven't Selected Any file selected", Toast.LENGTH_SHORT).show();

        }
    }

}

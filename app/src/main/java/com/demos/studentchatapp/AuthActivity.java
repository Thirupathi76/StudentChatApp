package com.demos.studentchatapp;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AuthActivity extends AppCompatActivity {

    TextInputEditText mName, mCollege, mBranch;
    Button mRegBtn;
    FirebaseFirestore firestore;
    ImageView prof_image;
    private String photo_path = "";
    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;
    private Uri mImageUri;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);


        getSupportActionBar().hide();
        mName = findViewById(R.id.txt_name);
        mCollege = findViewById(R.id.txt_college);
        mBranch = findViewById(R.id.txt_branch);
        mRegBtn = findViewById(R.id.reg_btn);

        mStorage = FirebaseStorage.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("prof_uploads");

        prof_image = findViewById(R.id.prof_image);
        firestore = FirebaseFirestore.getInstance();
        prof_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(AuthActivity.this);
            }
        });


        String name = Utilities.getPreference(AuthActivity.this, Utilities.USER_NAME);
        String branch = Utilities.getPreference(AuthActivity.this, Utilities.BRANCH);

        if (!name.isEmpty()) {
            startActivity(new Intent(this, Chat_Room.class));
        }
        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utilities.checkConnection(AuthActivity.this)) {
                    boolean isValid = true;
                    String name = mName.getText().toString().trim();
                    String college = mCollege.getText().toString().trim();
                    final String branch = mBranch.getText().toString().trim();

                    if (name.isEmpty()) {
                        isValid = false;
                        mName.setError("Name should not empty");
                    }
                    if (college.isEmpty()) {
                        isValid = false;
                        mCollege.setError("College/ University should not empty");

                    }
                    if (branch.isEmpty()) {
                        isValid = false;
                        mBranch.setError("Branch should not empty");

                    }
                    if (photo_path.isEmpty()) {
                        isValid = false;
//                    mBranch.setError("Select Image");
                        Toast.makeText(AuthActivity.this, "Capture Your image", Toast.LENGTH_SHORT).show();

                    }
                    if (isValid) {
                        StorageReference fileReference = mStorageRef.child(new Date(System.currentTimeMillis())
                                + "." + getFileExtension(mImageUri));
                        dialog = new ProgressDialog(AuthActivity.this);
                        dialog.setMessage("loading .....");
                        dialog.show();
                        mUploadTask = fileReference.putFile(mImageUri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        if (dialog.isShowing()) {
                                            dialog.dismiss();
                                        }

                                        String name = mName.getText().toString().trim();
                                        String college = mCollege.getText().toString().trim();
                                        final String branch = mBranch.getText().toString().trim();

                                        Map<String, Object> map2 = new HashMap<String, Object>();
                                        map2.put("user_name", name);
                                        map2.put("college", college);
                                        map2.put("branch", branch);
                                        map2.put("reg_date", getDateTime());
                                        map2.put("prof_image", taskSnapshot.getDownloadUrl().toString());
                                        Utilities.setPreference(AuthActivity.this, Utilities.USER_NAME, name);
                                        Utilities.setPreference(AuthActivity.this, Utilities.BRANCH, branch);

                                        firestore.collection(name)/*.document(name).collection("")*/.add(map2)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {

                                                        startActivity(new Intent(AuthActivity.this, Chat_Room.class));
                                                        Toast.makeText(AuthActivity.this, "Registration Success", Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(AuthActivity.this, "Failure " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AuthActivity.this, "Failed to upload", Toast.LENGTH_SHORT).show();
                                        if (dialog.isShowing()) {
                                            dialog.dismiss();
                                        }
                                    }
                                });

                    }
                } else {
                    Toast.makeText(AuthActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                mImageUri = resultUri;
                prof_image.setImageURI(resultUri);
                photo_path = resultUri.getPath();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    String getDateTime() {
        Date date = new Date();
        String stringDate = DateFormat.getDateTimeInstance().format(date);
        Log.e("Date and Time", stringDate);
        return stringDate;
    }

    @Override
    protected void onResume() {
        super.onResume();
        String name = Utilities.getPreference(AuthActivity.this, Utilities.USER_NAME);
        String branch = Utilities.getPreference(AuthActivity.this, Utilities.BRANCH);

        if (!name.isEmpty()) {
            startActivity(new Intent(this, Chat_Room.class));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}

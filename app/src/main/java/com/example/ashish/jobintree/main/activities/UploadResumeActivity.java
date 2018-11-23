package com.example.ashish.jobintree.main.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ashish.jobintree.R;
import com.example.ashish.jobintree.main.SharedPrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class UploadResumeActivity extends AppCompatActivity {

    private static final String TAG = UploadResumeActivity.class.getSimpleName();
    private static final int REQUEST_CODE_PDF = 123;

    private Button btSelectResume,btUploadResume;
    Uri fileuri;

    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_resume);

        btSelectResume = findViewById(R.id.bt_select_resume);
        btUploadResume = findViewById(R.id.bt_upload_resume);

        storageReference = FirebaseStorage.getInstance().getReference()
                .child("users")
                .child(SharedPrefManager.getInstance(UploadResumeActivity.this).getPhoneNumber())
                .child("RESUME");

        /*btSelectResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDocument();
            }
        });*/

       btUploadResume.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
              // uploadToFirebase();
               getDocument();
           }

       });
    }

    private void uploadToFirebase() {

        if(fileuri != null){
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Uploading Resume...");
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            Log.i(TAG, "uploadToFirebase: 111");

            storageReference.putFile(fileuri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            SharedPrefManager.getInstance(UploadResumeActivity.this).uploadResume("Yes");
                            dialog.dismiss();
                            Toast.makeText(UploadResumeActivity.this, "Resume Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(UploadResumeActivity.this);
                            builder.setCancelable(false);
                            builder.setMessage("Welcome to the JobInTree!\nDestination for your bright future");
                            builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(UploadResumeActivity.this,HomeActivity.class));
                                    finish();
                                }
                            });
                            builder.show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(UploadResumeActivity.this, "Uploading failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Please select your resume before uploading", Toast.LENGTH_SHORT).show();
        }
    }


    private void getDocument()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // Only the system receives the ACTION_OPEN_DOCUMENT, so no need to test.
        startActivityForResult(intent, REQUEST_CODE_PDF);
    }



    @Override
    protected void onActivityResult(int req, int result, Intent data)
    {
        if (req == REQUEST_CODE_PDF && result == RESULT_OK)
        {
            fileuri = data.getData();
            //String docFilePath = getFileNameByUri(this, fileuri);
            uploadToFirebase();
            Log.i(TAG, "onActivityResult: filename :" + fileuri);
        }
    }

}

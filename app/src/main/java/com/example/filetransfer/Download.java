package com.example.filetransfer;

import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class Download extends AppCompatActivity {

    public Download(){ }

    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageRef = storage.getReference();

    StorageReference pathReference = storageRef.child("images/*");

    StorageReference gsReference = storage.getReferenceFromUrl("gs://bucket/images/*");

    StorageReference httpsReference = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/b/bucket/o/images%*.jpg");

    StorageReference exampleRef = storageRef.child("images/example_picture.jpg");

    private void download_from_firebase()
    {
        final long ONE_MEGABYTE = 1024 * 1024;
        exampleRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Download.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void download_to_local()
    {
        exampleRef = storageRef.child("images/example_picture.jpg");

        File localFile = null;
        try {
            localFile = File.createTempFile("images", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        exampleRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Download.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void download_file_URL()
    {
        storageRef.child("example_picture.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Download.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void download_into_imageView()
//    {
//        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
//        ImageView imageView = findViewById(R.id.imageView);
//        Glide.with(this /* context */)
//                .load(storageReference)
//                .into(imageView);
//    }

}

                package com.example.filetransfer;

                import android.Manifest;
                import android.app.ProgressDialog;
                import android.content.Intent;
                import android.content.pm.PackageManager;
                import android.net.Uri;
                import android.os.Bundle;
                import android.view.View;
                import android.widget.Button;
                import android.widget.TextView;
                import android.widget.Toast;

                import androidx.annotation.NonNull;
                import androidx.annotation.Nullable;
                import androidx.appcompat.app.AppCompatActivity;
                import androidx.core.app.ActivityCompat;
                import androidx.core.content.ContextCompat;

                import com.google.android.gms.tasks.OnCompleteListener;
                import com.google.android.gms.tasks.OnFailureListener;
                import com.google.android.gms.tasks.OnSuccessListener;
                import com.google.android.gms.tasks.Task;
                import com.google.firebase.database.DatabaseReference;
                import com.google.firebase.database.FirebaseDatabase;
                import com.google.firebase.storage.FirebaseStorage;
                import com.google.firebase.storage.OnProgressListener;
                import com.google.firebase.storage.StorageReference;
                import com.google.firebase.storage.UploadTask;

                public class NewUpload extends AppCompatActivity {

                    Button selectFile, upload;
                    TextView notification;
                    Uri pdfUri;

                    FirebaseStorage storage; //upload files
                    FirebaseDatabase database; //store URLS of uploaded files
                    ProgressDialog progressDialog;


                    @Override
                    protected void onCreate(Bundle savedInstanceState) {
                        super.onCreate(savedInstanceState);
                        setContentView(R.layout.new_upload);

                        storage = FirebaseStorage.getInstance();
                        database = FirebaseDatabase.getInstance();

                        selectFile = findViewById(R.id.selectFile);
                        upload = findViewById(R.id.upload);
                        notification = findViewById(R.id.notification);

                        selectFile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (ContextCompat.checkSelfPermission(NewUpload.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                    selectPdf();
                                } else
                                    ActivityCompat.requestPermissions(NewUpload.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                            }
                        });


                        upload.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (pdfUri != null) {
                                    fileUpload(pdfUri);
                                } else Toast.makeText(NewUpload.this, "Select a file", Toast.LENGTH_SHORT).show();


                            }
                        });
                    }

                            private void fileUpload(Uri pdfUri) {
                                progressDialog=new ProgressDialog(this);
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                progressDialog.setTitle("Uploading file...");
                                progressDialog.setProgress(0);
                                progressDialog.show();



                                final String fileName=System.currentTimeMillis()+"";
                                StorageReference storageReference = storage.getReference();

                                storageReference.child("Uploads").child(fileName).putFile(pdfUri)
                                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){
                                                    String url = taskSnapshot.getStorage().getDownloadUrl().toString();
                                                            DatabaseReference reference = database.getReference();

                                                            reference.child(fileName).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful())

                                                                        Toast.makeText(NewUpload.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                                                                    else
                                                                        Toast.makeText(NewUpload.this, "File Unsuccessful To Upload", Toast.LENGTH_SHORT).show();

                                                                }
                                                            });
                                                }
                                            })


                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(NewUpload.this,"File Unsuccessful To Upload",Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                                    int currentProgress= (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                                                            progressDialog.setProgress(currentProgress);
                                                }


                                            });
                                                        }





                    @Override
                    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

                        if(requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                        {
                            selectPdf();
                        }
                        else
                            Toast.makeText(NewUpload.this,"please provide permission",Toast.LENGTH_SHORT).show();

                    }


                    private void selectPdf(){

                        Intent intent = new Intent();
                        intent.setType(("application/pdf"));
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent,86);
                    }



                    @Override
                    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
                        super.onActivityResult(requestCode, resultCode, data);
                        if (requestCode==86 && resultCode==RESULT_OK && data!=null)
                        {
                            pdfUri=data.getData();
                            notification.setText("File Selected:" + data.getData().getLastPathSegment());
                        }
                        else
                        {
                            Toast.makeText(NewUpload.this,"Please select a file",Toast.LENGTH_SHORT).show();
                        }
                }
                }
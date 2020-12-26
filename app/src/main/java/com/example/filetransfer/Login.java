package com.example.filetransfer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Login extends AppCompatActivity
{

    private FirebaseAuth mAuth;

    private ImageView bgapp, fileLogo;
    private Button loginButton;
    private EditText editUserEmail, editUserPassword;
    private TextView signupTextView;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();

        bgapp = (ImageView) findViewById(R.id.bgapp);
        fileLogo = (ImageView) findViewById(R.id.fileLogo);
        loginButton = (Button) findViewById(R.id.loginButton);
        editUserEmail = (EditText) findViewById(R.id.editUserEmail);
        editUserPassword = (EditText) findViewById(R.id.editUserPassword);
        signupTextView = (TextView) findViewById(R.id.signupTextView);

        loadingBar = new ProgressDialog(this);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadingBar.setMessage("Logging into your account...");
                loadingBar.setCanceledOnTouchOutside(true);
                loadingBar.show();

                UserLogin();

            }
        });

        signupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
            }
        });
    }

    private void UserLogin()
    {
        String email = editUserEmail.getText().toString();
        String password = editUserPassword.getText().toString();

        if (email.isEmpty())
        {
            editUserEmail.setError("Email is Required");
            editUserEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            editUserEmail.setError("Please enter a valid email");
            editUserEmail.requestFocus();
            return;
        }
        if (password.isEmpty())
        {
            editUserPassword.setError("Password is required");
            editUserPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            loadingBar.dismiss();
//                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                                    .setDisplayName("Saashiimee").build();
//
//                            user.updateProfile(profileUpdates);
                            finish();
                        } else {
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                });
    }

}

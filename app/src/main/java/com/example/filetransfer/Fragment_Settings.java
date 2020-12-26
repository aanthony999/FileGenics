package com.example.filetransfer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Fragment_Settings extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    private EditText editDisplayName;
    private Button updateUserProfile;
    private String new_DisplayName;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        editDisplayName = (EditText) view.findViewById(R.id.editDisplayName);
        updateUserProfile = (Button) view.findViewById(R.id.updateUserProfile);

        updateUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new_DisplayName = editDisplayName.getText().toString();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(new_DisplayName).build();

                user.updateProfile(profileUpdates);
            }
        });
    }

}

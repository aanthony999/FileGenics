package com.example.filetransfer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Fragment_Transfer extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_upload, container, false);
    }

    private ImageView upload_icon, wifi_icon, zipfile_icon;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        wifi_icon = (ImageView) view.findViewById(R.id.wifi_icon);

        upload_icon = (ImageView) view.findViewById(R.id.upload_icon);


        upload_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NewUpload.class);
                view.getContext().startActivity(intent);
            }
        });
        zipfile_icon = (ImageView) view.findViewById(R.id.zipfile_icon);
        zipfile_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), UploadMenu.class);
                view.getContext().startActivity(intent);
            }
        });
    }




}

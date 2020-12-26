package com.example.filetransfer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Fragment_QR extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_qr, container, false);
    }

    public static TextView resulttextview;
    private ImageView scanbutton, buttontoast;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        resulttextview = view.findViewById(R.id.barcodetextview);
        scanbutton = view.findViewById(R.id.buttonscan);
        buttontoast = view.findViewById(R.id.buttontoast);

        scanbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ScanCodeActivity.class);
                startActivity(intent);
            }
        });

        buttontoast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), resulttextview.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

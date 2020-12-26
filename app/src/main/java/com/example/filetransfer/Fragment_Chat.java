package com.example.filetransfer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Fragment_Chat extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    ListView lvDiscussionTopics;
    ArrayList<String> listOfDiscussion = new ArrayList<String>();
    ArrayAdapter arrayAdpt;
    Button addtopic;
    String UserName;
    private FirebaseAnalytics mFirebaseAnalytics;
    private DatabaseReference dbr = FirebaseDatabase.getInstance().getReference().getRoot();
    private Button ChatButton;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        lvDiscussionTopics = (ListView) view.findViewById(R.id.lvDiscussionTopics);
        arrayAdpt = new ArrayAdapter(getContext(),
                android.R.layout.simple_list_item_1, listOfDiscussion);
        lvDiscussionTopics.setAdapter(arrayAdpt);

        getUserName();

        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();

                while (i.hasNext()) {
                    set.add(((DataSnapshot) i.next()).getKey());
                }

                arrayAdpt.clear();
                arrayAdpt.addAll(set);
                arrayAdpt.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        lvDiscussionTopics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getContext(), Fragment_Chat2.class);
                i.putExtra("selected_topic", ((TextView) view).getText().toString());
                i.putExtra("user_name", UserName);
                startActivity(i);
            }
        });

        addtopic = (Button) view.findViewById(R.id.Addchannel);

        addtopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtopic();
            }
        });
    }

    private void getUserName() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        final EditText userName = new EditText(this);

//        builder.setMessage("Welcome " + name);
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                UserName = name.getText().toString();
//            }
//        });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                getUserName();
//            }
//        });
//        builder.show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String name = user.getDisplayName();
        UserName = name;
    }

    private void addtopic() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();

            if (email != "same_7410@hotmail.com") {
                Toast.makeText(getContext(), "permission denied", Toast.LENGTH_SHORT).show();
            } else if (email == "same_7410@hotmail.com") {
                Toast.makeText(getContext(), "permission granted", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
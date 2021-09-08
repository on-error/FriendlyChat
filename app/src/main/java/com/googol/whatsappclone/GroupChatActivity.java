package com.googol.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.googol.whatsappclone.Adapters.ChatAdapter;
import com.googol.whatsappclone.ChatDetailActivity;
import com.googol.whatsappclone.MainActivity;
import com.googol.whatsappclone.Models.MessageModel;
import com.googol.whatsappclone.R;
import com.googol.whatsappclone.databinding.ActivityChatDetailBinding;
import com.googol.whatsappclone.databinding.ActivityGroupChatBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private ActivityGroupChatBinding binding;
    private ArrayList<MessageModel> messageModels = new ArrayList<>();
    private ChatAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        String senderId = mAuth.getUid();
//        String receiverId = getIntent().getStringExtra("userId");
//        String username = getIntent().getStringExtra("username");
//        String profilePic = getIntent().getStringExtra("profilePic");


        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupChatActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        adapter = new ChatAdapter(messageModels, this);
        binding.recView.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        binding.recView.setLayoutManager(manager);

        String uid = mAuth.getUid();
        binding.userName.setText("Broadcast Chat");

        mDatabase.getReference().child("chats")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageModels.clear();
                        for (DataSnapshot sp : snapshot.getChildren()){
                            MessageModel model = sp.getValue(MessageModel.class);
                            messageModels.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = binding.etmessage.getText().toString();
                final MessageModel model = new MessageModel(uid, message);
                model.setTimeStamp(new Date().getTime());
                binding.etmessage.setText("");

                mDatabase.getReference().child("chats")
                        .push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
            }
        });

    }
}
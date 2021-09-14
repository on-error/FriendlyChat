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
import com.googol.whatsappclone.Models.MessageModel;
import com.googol.whatsappclone.databinding.ActivityChatDetailBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetailActivity extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private ArrayList<MessageModel> messageModels = new ArrayList<>();
    private ChatAdapter adapter;

    ActivityChatDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        String senderId = mAuth.getUid();
        String receiverId = getIntent().getStringExtra("userId");
        String username = getIntent().getStringExtra("username");
        String profilePic = getIntent().getStringExtra("profilePic");

        binding.userName.setText(username);
        Picasso.get().load(profilePic).placeholder(R.drawable.common_google_signin_btn_icon_dark).into(binding.profileImage);

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatDetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        adapter = new ChatAdapter(messageModels, this, receiverId);
        binding.recView.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        binding.recView.setLayoutManager(manager);

        final String senderRoom = senderId + receiverId;
        final String receiverRoom = receiverId + senderId;

        mDatabase.getReference().child("chats").child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageModels.clear();
                        for (DataSnapshot sp : snapshot.getChildren()){
                            MessageModel model = sp.getValue(MessageModel.class);
                            model.setMessageId(sp.getKey());
                            messageModels.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        //TODO : Implement AES ENCRYPT here
        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = binding.etmessage.getText().toString();
                try {
                    message = AES.encrypt(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                final MessageModel model = new MessageModel(senderId, message);
                model.setTimeStamp(new Date().getTime());
                binding.etmessage.setText("");

                mDatabase.getReference().child("chats")
                        .child(senderRoom)
                        .push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mDatabase.getReference().child("chats").child(receiverRoom)
                                .push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                    }
                });
            }
        });


    }
}
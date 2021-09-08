package com.googol.whatsappclone.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.googol.whatsappclone.MainActivity;
import com.googol.whatsappclone.Models.Users;
import com.googol.whatsappclone.R;
import com.googol.whatsappclone.databinding.ActivityAllSettingsBinding;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AllSettingsActivity extends AppCompatActivity {

    private ActivityAllSettingsBinding binding;
    private FirebaseStorage storage;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllSettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        storage = FirebaseStorage.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = binding.newUsername.getText().toString().trim();
                String about = binding.newAbout.getText().toString().trim();

                HashMap<String, Object> obj = new HashMap<>();
                obj.put("username", username);
                obj.put("about", about);
                mDatabase.getReference().child("Users").child(mAuth.getUid()).updateChildren(obj);
                Toast.makeText(AllSettingsActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
            }
        });

        mDatabase.getReference().child("Users").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                Picasso.get().load(user.getProfilePic()).placeholder(R.drawable.ic_google).into(binding.profileImage);
                binding.newAbout.setText(user.getAbout());
                binding.newUsername.setText(user.getUsername());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 33);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 33){
            if (data.getData() != null){
                Uri sfile = data.getData();
                binding.profileImage.setImageURI(sfile);

                final StorageReference reference = storage.getReference().child("profile_pictures")
                        .child(mAuth.getUid());

                reference.putFile(sfile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                mDatabase.getReference().child("Users").child(mAuth.getUid()).child("profilePic")
                                        .setValue(uri.toString());

                                Toast.makeText(AllSettingsActivity.this, "Profile Picture Updated", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });

            }
        }

    }
}
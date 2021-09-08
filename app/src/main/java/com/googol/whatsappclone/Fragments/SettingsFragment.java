package com.googol.whatsappclone.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.googol.whatsappclone.Adapters.AllSettingsActivity;
import com.googol.whatsappclone.GroupChatActivity;
import com.googol.whatsappclone.MainActivity;
import com.googol.whatsappclone.R;
import com.googol.whatsappclone.SignInActivity;
import com.googol.whatsappclone.databinding.FragmentChatBinding;
import com.googol.whatsappclone.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    public SettingsFragment(){

    }

    private FragmentSettingsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), SignInActivity.class);
                startActivity(intent);
            }
        });

        binding.groupchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GroupChatActivity.class);
                startActivity(intent);
            }
        });

        binding.msettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AllSettingsActivity.class);
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }
}

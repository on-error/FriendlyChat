package com.googol.whatsappclone.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.googol.whatsappclone.Fragments.CallsFragment;
import com.googol.whatsappclone.Fragments.ChatFragment;
import com.googol.whatsappclone.Fragments.SettingsFragment;
import com.googol.whatsappclone.Fragments.StatusFragment;

public class FragmentsAdapter extends FragmentPagerAdapter {
    public FragmentsAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1: return new StatusFragment();
            case 2: return new CallsFragment();
            case 3: return new SettingsFragment() ;
            default: return new ChatFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        String title = null;
        switch (position){
            case 0: title = "CHATS";
            case 2: title = "STATUS";
            case 3 : title = "SETTINGS";
            default: title = "CALLS";
        }

        return title;
    }
}

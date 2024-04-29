package com.example.gproject.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.gproject.fragment.AiTeacherFragment;
import com.example.gproject.fragment.LearnFragment;
import com.example.gproject.fragment.WordFragment;
import com.example.gproject.fragment.WrongFragment;

public class PagesAdapter extends FragmentStateAdapter {

    public PagesAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new LearnFragment();
            case 1:
                return new AiTeacherFragment();
            case 2:
                return new WordFragment();
            default:
                return new WrongFragment();


        }

    }


    @Override
    public int getItemCount() {
        return 4;
    }
}

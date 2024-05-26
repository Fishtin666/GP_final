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

    private String testKey;
    private String spinner;
    private String testSubKey;

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
                //R
            case 3:
                // 如果有传递参数，则传递给 WrongFragment
                if (testKey != null && spinner != null) {
                    return WrongFragment.newInstance(testKey, spinner, testSubKey);
                } else if (testKey == null && spinner != null) {
                    return WrongFragment.newInstance(testKey, spinner, testSubKey);
                } else {
                    return new WrongFragment();
                }
            default:
                return new WrongFragment();


        }

    }


    @Override
    public int getItemCount() {
        return 4;
    }
    public void setWrongFragmentArgs(String testKey, String spinner, String testSubKey) {
        this.testKey = testKey;
        this.spinner = spinner;
        this.testSubKey = testSubKey;
        notifyDataSetChanged();
    }
//    public void speakWrongFragmentArgs(String testKey, String testSubKey ,String spinner) {
//        this.testKey = testKey;
//        this.spinner = spinner;
//        this.testSubKey = testSubKey;
//        notifyDataSetChanged();
//    }
}

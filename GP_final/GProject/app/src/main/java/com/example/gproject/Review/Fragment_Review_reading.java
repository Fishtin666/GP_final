package com.example.gproject.Review;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.gproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Review_reading#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Review_reading extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_Review_reading() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Review_choose_listening.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Review_reading newInstance(String param1, String param2) {
        Fragment_Review_reading fragment = new Fragment_Review_reading();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void setLreviewTestKey(String key) {
        this.testsel = key;
        Log.d("testKey ff set",testsel);
    }
    public String getLreviewTestKey() {
        Log.d("testKey ff get",":"+selectedTest);
        return selectedTest;
    }

    Button all,chose,blank,match,judge,mutliple;
    private String selectedTest; // 存储选定的按钮名称
    private String testkey;
    private String testsel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__review_reading, container, false);
        chose = view.findViewById(R.id.R_chose);
        blank = view.findViewById(R.id.R_blank);
        match = view.findViewById(R.id.R_match);
        judge = view.findViewById(R.id.R_judge);
        mutliple = view.findViewById(R.id.R_multiple);

        setToggleClickListener(chose);
        setToggleClickListener(blank);
        setToggleClickListener(match);
        setToggleClickListener(judge);
        setToggleClickListener(mutliple);

        return view;
    }

    private void setToggleClickListener(final Button button) {
        Log.d("TAG", "setToggleClickListener: Setting toggle click listener");
        final boolean[] isClicked = {false}; // 使用数组来跟踪状态，以便在匿名内部类中修改

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: Button clicked");
                if (isClicked[0]) {
                    // 恢复到未点击状态的颜色
                    button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gray)));
                    selectedTest=null;
                } else {
                    // 遍历所有按钮，将它们的状态还原为未点击状态的颜色
                    for (Button btn : new Button[]{chose, blank, match, judge, mutliple}) {
                        btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gray)));
                    }
                    // 设置为点击状态的颜色
                    button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.darkGrey)));


                    selectedTest = getResources().getResourceEntryName(button.getId()); // 获取按钮的XML ID名称
                    Log.d("TAG", "select testkey"+selectedTest);
                    setLreviewTestKey(selectedTest);
                }
                isClicked[0] = !isClicked[0]; // 切换按钮状态
            }
        });
    }
}
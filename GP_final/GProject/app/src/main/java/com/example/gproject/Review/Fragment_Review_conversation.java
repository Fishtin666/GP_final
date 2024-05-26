package com.example.gproject.Review;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.gproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Review_conversation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Review_conversation extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_Review_conversation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Review_conversation.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Review_conversation newInstance(String param1, String param2) {
        Fragment_Review_conversation fragment = new Fragment_Review_conversation();
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

    // 设置 testkey 的方法
    public void setLreviewTestKey(String key) {
        this.testsel = key;
        Log.d("testKey ff set",testsel);
    }
    public String getLreviewTestKey() {
        Log.d("testKey ff get",":"+selectedTest);
        return selectedTest;
    }

    private String selectedTest; // 存储选定的按钮名称
    private String testkey;
    private String testsel;
    Button travel,hobby,movie,sport,weather,country,food,pet;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment__review_conversation, container, false);
        travel = root.findViewById(R.id.travel);
        hobby = root.findViewById(R.id.hobby);
        movie = root.findViewById(R.id.movie);
        sport = root.findViewById(R.id.sport);
        weather = root.findViewById(R.id.weather);
        country = root.findViewById(R.id.country);
        food = root.findViewById(R.id.food);
        pet = root.findViewById(R.id.pet);

        setToggleClickListener(travel);
        setToggleClickListener(hobby);
        setToggleClickListener(movie);
        setToggleClickListener(sport);
        setToggleClickListener(weather);
        setToggleClickListener(country);
        setToggleClickListener(food);
        setToggleClickListener(pet);



        return root;
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
                    testkey=null;
                } else {
                    // 遍历所有按钮，将它们的状态还原为未点击状态的颜色
                    for (Button btn : new Button[]{travel,hobby,movie,sport,weather,country,food,pet}) {
                        btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gray)));
                    }
                    // 设置为点击状态的颜色
                    button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.darkGrey)));


                    selectedTest = button.getText().toString(); // 保存选定的按钮名称
                    Log.d("TAG", "select testkey"+selectedTest);
                    setLreviewTestKey(selectedTest);
                }
                isClicked[0] = !isClicked[0]; // 切换按钮状态
            }
        });
    }
}
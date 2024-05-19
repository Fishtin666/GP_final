package com.example.gproject.Review;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.gproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Review_speaking#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Review_speaking extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_Review_speaking() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Review_speaking.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Review_speaking newInstance(String param1, String param2) {
        Fragment_Review_speaking fragment = new Fragment_Review_speaking();
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

    private Button myButton;
    private boolean isButtonClicked;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__review_speaking, container, false);
        myButton = view.findViewById(R.id.r_all3);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 切换按钮的背景状态
                if (isButtonClicked) {
                    myButton.setBackgroundResource(R.color.dark_gray);
                } else {
                    myButton.setBackgroundResource(R.color.gray);
                }
                isButtonClicked = !isButtonClicked;
            }
        });

        return view;
    }
}
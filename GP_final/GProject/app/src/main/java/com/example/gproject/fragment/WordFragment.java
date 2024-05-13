package com.example.gproject.fragment;

import android.os.Bundle;
import android.content.Intent;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gproject.Adapters.QuestionNumberAdapter;
import com.example.gproject.Models.QuestionNumberModel;
import com.example.gproject.R;
import com.example.gproject.WordQuiz.WordListActivity;
import com.example.gproject.WordQuiz.LevelAQuizActivity;
import com.example.gproject.meaning.ShowMeaning;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WordFragment newInstance(String param1, String param2) {
        WordFragment fragment = new WordFragment();
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
    ArrayList<QuestionNumberModel> list;
    QuestionNumberAdapter adapter;
    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.word_topic, container, false);
        CardView dic = rootView.findViewById(R.id.Cross_Topic);
        CardView wordTest = rootView.findViewById(R.id.wordTest);
        CardView wordSelect = rootView.findViewById(R.id.AI_chat);

        dic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dic_click(view);
            }
        });
        wordTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                word_test_click(view);
            }
        });
        wordSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                word_collect_click(view);
            }
        });
        return rootView;

    }
    public void dic_click(View view){
        Intent intent = new Intent(getActivity(), ShowMeaning.class);
        startActivity(intent);
    }
    public void word_test_click(View view){
        Intent intent = new Intent(getActivity(), LevelAQuizActivity.class);
        startActivity(intent);
    }
    public void word_collect_click(View view){
        Intent intent = new Intent(getActivity(), WordListActivity.class);
        startActivity(intent);
    }
}
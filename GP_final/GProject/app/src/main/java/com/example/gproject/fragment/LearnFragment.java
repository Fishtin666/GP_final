package com.example.gproject.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.gproject.Listening.listen;
import com.example.gproject.PronunciationAssessment;
import com.example.gproject.R;

import com.example.gproject.Speaking.Speaking;
import com.example.gproject.Writing.W_topic;
import com.example.gproject.reading.R_topic;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LearnFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class LearnFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public LearnFragment() {
        // Required empty public constructor
    }


    public static LearnFragment newInstance(String param1, String param2) {
        LearnFragment fragment = new LearnFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_learn, container, false);

        CardView speakingCardView = rootView.findViewById(R.id.Speaking);
        CardView PACardView = rootView.findViewById(R.id.P_A);
        CardView listeningCardView = rootView.findViewById(R.id.Listening);
        CardView readingCardView = rootView.findViewById(R.id.Reading);
        CardView writingCardView = rootView.findViewById(R.id.Writing);

        speakingCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SpeakingClick(view);

            }
        });
        PACardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PAClick(view);
            }
        });
        listeningCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ListeningClick(view);
            }
        });
        readingCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ReadingClick(view);
            }
        });
        writingCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                WritingClick(view);
            }
        });

        return rootView;
    }
    public void SpeakingClick(View view) {
        Intent intent = new Intent(getActivity(), Speaking.class);
        startActivity(intent);
    }

    public void PAClick(View view) {
        Intent intent = new Intent(getActivity(), PronunciationAssessment.class);
        startActivity(intent);
    }

    public void ReadingClick(View view) {
        Intent intent = new Intent(getActivity(), R_topic.class);
        startActivity(intent);
    }

    public void ListeningClick(View view) {
        Intent intent = new Intent(getActivity(), listen.class);
        startActivity(intent);
    }

    public void WritingClick(View view) {
        Intent intent = new Intent(getActivity(), W_topic.class);
        startActivity(intent);
    }
}
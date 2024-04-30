package com.example.gproject.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.gproject.Adapters.QuestionNumberAdapter;
import com.example.gproject.Models.QuestionNumberModel;
import com.example.gproject.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TestFragment() {
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
    public static TestFragment newInstance(String param1, String param2) {
        TestFragment fragment = new TestFragment();
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

//        View rootView= inflater.inflate(R.layout.ques_num_add, container, false);
//
//        recyclerView = rootView.findViewById(R.id.QuesNum_Recy);
//
//        list =new ArrayList<>();
//
//        LinearLayoutManager manager =new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(manager);
//
//
//
//
//        for(int i=1;i<13;i++)
//            list.add(new QuestionNumberModel("考古題"+i,1));
//
//
//
//        adapter = new QuestionNumberAdapter(getContext(),list);
//        recyclerView.setAdapter(adapter);

        return inflater.inflate(R.layout.fragment_test, container, false);
        //return rootView;

    }
}
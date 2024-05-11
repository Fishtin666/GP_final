package com.example.gproject.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gproject.R;
import com.example.gproject.Review.Fragment_Review_VR;
import com.example.gproject.Review.Fragment_Review_listening;
import com.example.gproject.Review.Fragment_Review_reading;
import com.example.gproject.Review.Fragment_Review_speaking;
import com.example.gproject.Review.Fragment_Review_writing;
import com.example.gproject.Review.RReview;
import com.example.gproject.Review.Review_choose;
import com.example.gproject.getDb;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WrongFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WrongFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WrongFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WrongFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WrongFragment newInstance(String param1, String param2) {
        WrongFragment fragment = new WrongFragment();
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

    FirebaseAuth auth;
    private DatabaseReference databaseReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_wrong, container, false);

        ImageButton pencil = rootview.findViewById(R.id.pencil1);
        Button button = rootview.findViewById(R.id.button);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        Spinner spinner1 = rootview.findViewById(R.id.spinner1);
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(getContext(),R.array.Review_array, android.R.layout.simple_dropdown_item_1line);
        spinner1.setAdapter(adapter1);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0: //R
                        break;
                    case 1: //L
                        break;
                    case 2: //W
                        getWriting();
                        break;
                    case 3: //S
                        break;
                    case 4: //VR
                        break;
                    default: //R
                        break;
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        pencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Review_choose.class);
                startActivity(intent);
            }
        });
//        button.setOnClickListener(new View.OnClickListener(){
//            public  void onClick(View v){
//                Intent intent = new Intent(getContext(), RReview.class);
//                startActivity(intent);
//            }
//        });

    return rootview;
    }

    @NonNull
    public void getWriting(){
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference targetRef = databaseReference
                    .child("users")
                    .child(userId)
                    .child("Writing");

            targetRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            String key = childSnapshot.getKey();
                            // 创建一个按钮
                            Button button = new Button(getContext());
                            // 设置按钮的文本为子节点的键名
                            button.setText(key);
                        }
                    } else {


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
        }
    }
}
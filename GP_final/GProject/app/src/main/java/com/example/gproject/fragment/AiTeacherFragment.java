package com.example.gproject.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gproject.Adapters.TopicAdapter;
import com.example.gproject.AiTeacher.AiChat;
import com.example.gproject.AiTeacher.CrossTopic_db;
import com.example.gproject.AiTeacher.Cross_Topic;
import com.example.gproject.ChooseCrossTopic;
import com.example.gproject.Models.TopicModel;
import com.example.gproject.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AiTeacherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AiTeacherFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AiTeacherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AiTeacherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AiTeacherFragment newInstance(String param1, String param2) {
        AiTeacherFragment fragment = new AiTeacherFragment();
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

    RecyclerView recyclerView;
    ArrayList<TopicModel> list;
    TopicAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_ai_teacher2, container, false);
        CardView crossTopic = rootView.findViewById(R.id.Cross_Topic);
        CardView aiChat = rootView.findViewById(R.id.AI_chat);
        TextView tip=rootView.findViewById(R.id.tip);
        CardView db=rootView.findViewById(R.id.db);

        tip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("什麼是串題?");
                builder.setMessage("在雅思口說考試中，每個題庫加總起來有好幾百道題。 " +
                        "串題法就是挖掘每個題目之間的聯繫，用一個回答盡可能串得足夠多的題目。" +
                        "如此一來不僅效率提升，答案的內容豐富度也提高了!" +
                        "現在就來使用看看我們的AI串題，讓AI幫你進行串題吧!");

                builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        crossTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), Cross_Topic.class);
                startActivity(intent);

            }
        });

        aiChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AiChat.class);
                startActivity(intent);

            }
        });

        db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ChooseCrossTopic.class));
            }
        });


//        Button situation = rootView.findViewById(R.id.situation);
//        Button role = rootView.findViewById(R.id.role);
//        Button chat = rootView.findViewById(R.id.chat);
//
//        recyclerView = rootView.findViewById(R.id.aiteacher_recy);
/*        list=new ArrayList<>();

        GridLayoutManager layoutManager =new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);

        list.add(new TopicModel("Catch up with a friend",R.drawable.with_friend,2));
        list.add(new TopicModel("Ask for direction",R.drawable.direction,1));
        list.add(new TopicModel("Making a Restaurant Reservation",R.drawable.reservation,1));
        list.add(new TopicModel("Shopping at the Grocery Store",R.drawable.grocery,1));
        list.add(new TopicModel("Greeting a Neighbor",R.drawable.neighbor,1));
        list.add(new TopicModel("Meeting Someone at the park ",R.drawable.meet_park,1));
        list.add(new TopicModel("Daily life",R.drawable.daily_life,1));
        list.add(new TopicModel("Daily life",R.drawable.daily_life,1));


        adapter =new TopicAdapter(getContext(),list);
        recyclerView.setAdapter(adapter);




        situation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                situation.setBackgroundColor(Color.parseColor("#A73534"));
                role.setBackgroundColor(Color.WHITE);
                chat.setBackgroundColor(Color.WHITE);
                situation.setTextColor(Color.WHITE);
                role.setTextColor(Color.BLACK);
                chat.setTextColor(Color.BLACK);
                list.clear();
                list.add(new TopicModel("Catch up with a friend",R.drawable.with_friend,2));
                list.add(new TopicModel("Ask for direction",R.drawable.direction,1));
                list.add(new TopicModel("Making a Restaurant Reservation",R.drawable.reservation,1));
                list.add(new TopicModel("Shopping at the Grocery Store",R.drawable.grocery,1));
                list.add(new TopicModel("Greeting a Neighbor",R.drawable.neighbor,1));
                list.add(new TopicModel("Meeting Someone at the park ",R.drawable.meet_park,1));
                list.add(new TopicModel("Daily life",R.drawable.daily_life,1));
                list.add(new TopicModel("Daily life",R.drawable.daily_life,1));
                adapter.notifyDataSetChanged();
            }
        });

        role.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                situation.setBackgroundColor(Color.WHITE);
                role.setBackgroundColor(Color.parseColor("#A73534"));
                chat.setBackgroundColor(Color.WHITE);
                situation.setTextColor(Color.BLACK);
                role.setTextColor(Color.WHITE);
                chat.setTextColor(Color.BLACK);
                list.clear();
                list.add(new TopicModel("Restaurant",R.drawable.restaurant,3));
                list.add(new TopicModel("Cafe",R.drawable.cafe,3));
                list.add(new TopicModel("Airport",R.drawable.airport,3));
                list.add(new TopicModel("Zoo",R.drawable.zoo,3));
                list.add(new TopicModel("Park",R.drawable.park,3));
                list.add(new TopicModel("Clothing store",R.drawable.clothing_store,3));
                list.add(new TopicModel("Daily life",R.drawable.daily_life,3));
                list.add(new TopicModel("Daily life",R.drawable.daily_life,3));
                adapter.notifyDataSetChanged();

            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                situation.setBackgroundColor(Color.WHITE);
                role.setBackgroundColor(Color.WHITE);
                chat.setBackgroundColor(Color.parseColor("#A73534"));
                situation.setTextColor(Color.BLACK);
                role.setTextColor(Color.BLACK);
                chat.setTextColor(Color.WHITE);
                list.clear();
                list.add(new TopicModel("Travel",R.drawable.travel,1));
                list.add(new TopicModel("Hobby",R.drawable.hobby,1));
                list.add(new TopicModel("Movie",R.drawable.movie,1));
                list.add(new TopicModel("Sport",R.drawable.sport,1));
                list.add(new TopicModel("Weather",R.drawable.weather,1));
                list.add(new TopicModel("Country",R.drawable.country,1));
                list.add(new TopicModel("Food",R.drawable.food,1));
                list.add(new TopicModel("Pet",R.drawable.pet,1));

                adapter.notifyDataSetChanged();
            }
        });*/

        return  rootView;
    }

}
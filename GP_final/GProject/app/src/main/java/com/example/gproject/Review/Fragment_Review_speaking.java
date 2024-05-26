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

    public void setSreviewPartKey(String key) {
        this.partsel = key;
        Log.d("testKey fpart set",partsel);
    }
    public void setSreviewTopickey(String key){
        this.topicsel = key;
        Log.d("testKey ftopic set",topicsel);
    }
    public String getSreviewTopickey() {
        Log.d("testKey ftopic get",":"+selectedTopic);
        return selectedTopic;
    }
    public String getSreviewPartkey() {
        Log.d("testKey fpart get",":"+partkey);
        return partkey;
    }
    Button part1,part2;
    Button study,work,hometown,accommadation,family,friends,entertainment,childhood,dailylife;
    Button people,place,item,experience;
    private String selectedTopic; // 存储选定的按钮名称
    private String partkey;
    private String topicsel;
    private String partsel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__review_speaking, container, false);
        part1 = view.findViewById(R.id.part1);
        part2 = view.findViewById(R.id.part2);

        study = view.findViewById(R.id.Study);
        work = view.findViewById(R.id.Work);
        hometown = view.findViewById(R.id.Hometown);
        accommadation = view.findViewById(R.id.Accommodation);
        family = view.findViewById(R.id.Family);
        friends = view.findViewById(R.id.Friends);
        entertainment = view.findViewById(R.id.Entertainment);
        childhood = view.findViewById(R.id.Childhood);
        dailylife = view.findViewById(R.id.Dailylife);

        people = view.findViewById(R.id.People);
        place = view.findViewById(R.id.Place);
        item = view.findViewById(R.id.Item);
        experience = view.findViewById(R.id.Experience);

        setPartClickListener(part1);
        setPartClickListener(part2);

        setToggleClickListener(study);
        setToggleClickListener(work);
        setToggleClickListener(hometown);
        setToggleClickListener(accommadation);
        setToggleClickListener(family);
        setToggleClickListener(friends);
        setToggleClickListener(entertainment);
        setToggleClickListener(childhood);
        setToggleClickListener(dailylife);

        setToggleClickListener(people);
        setToggleClickListener(place);
        setToggleClickListener(item);
        setToggleClickListener(experience);

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
                    selectedTopic=null;
                } else {
                    // 遍历所有按钮，将它们的状态还原为未点击状态的颜色
                    for (Button btn : new Button[]{study,work,hometown,accommadation,family,friends,entertainment,childhood,dailylife,people,place,item,experience}) {
                        btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gray)));
                    }
                    // 设置为点击状态的颜色
                    button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.darkGrey)));


                    selectedTopic =  button.getText().toString();
                    Log.d("TAG", "select testkey"+selectedTopic);
                    setSreviewPartKey(selectedTopic);
                }
                isClicked[0] = !isClicked[0]; // 切换按钮状态
            }
        });
    }

    private void  setPartClickListener(final Button button){
        final boolean[] isClicked = {false};
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: Button clicked");
                if (isClicked[0]) {
                    // 恢复到未点击状态的颜色
                    button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gray)));
                    partkey=null;
                } else {
                    // 遍历所有按钮，将它们的状态还原为未点击状态的颜色
                    for (Button btn : new Button[]{part1,part2}) {
                        btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gray)));
                    }
                    // 设置为点击状态的颜色
                    button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.darkGrey)));


                    partkey = getResources().getResourceEntryName(button.getId()); // 获取按钮的XML ID名称
                    Log.d("TAG", "select testkey"+partkey);
                    setSreviewTopickey(partkey);

                    // 根据点击的 part 按钮隐藏和显示相应的按钮组
                    if (button == part1) {
                        VisibilityButton(new Button[]{study, work, hometown, accommadation, family, friends, entertainment, childhood, dailylife}, View.VISIBLE);
                        VisibilityButton(new Button[]{people, place, item, experience}, View.GONE);
                    } else if (button == part2) {
                        VisibilityButton(new Button[]{study, work, hometown, accommadation, family, friends, entertainment, childhood, dailylife}, View.GONE);
                        VisibilityButton(new Button[]{people, place, item, experience}, View.VISIBLE);
                    }
                }
                isClicked[0] = !isClicked[0]; // 切换按钮状态
            }
        });

    }

    private void VisibilityButton(Button[] buttons, int visibility) {
        for (Button button : buttons) {
            button.setVisibility(visibility);
        }
    }
}
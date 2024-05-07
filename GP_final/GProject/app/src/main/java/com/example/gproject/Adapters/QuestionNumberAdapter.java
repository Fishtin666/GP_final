package com.example.gproject.Adapters;

import static com.example.gproject.Message.SENT_BY_ME;
import static com.example.gproject.Speaking.Speaking_part1_answer.star_show;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gproject.AiTeacher.AiTeacher2;
import com.example.gproject.AiTeacher.AiTeacherHelp;
import com.example.gproject.Models.QuestionNumberModel;
import com.example.gproject.R;
import com.example.gproject.Writing.Writing_T1answer1;
import com.example.gproject.Writing.Writing_T2answer1;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuestionNumberAdapter extends RecyclerView.Adapter<QuestionNumberAdapter.viewHolder>{

    Context context;
    ArrayList<QuestionNumberModel> list;
    public static String sharedString;
    public static boolean sharedBoolean=false;

    Boolean star_yellow=false;
    public QuestionNumberAdapter(Context context, ArrayList<QuestionNumberModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_question_number,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        QuestionNumberModel model =list.get(position);
        holder.ques_num.setText(model.getNum());

        if(star_show!=true){
            holder.star.setVisibility(View.INVISIBLE);

        }else
            holder.star.setVisibility(View.VISIBLE);

        holder.star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(star_yellow){
                    holder.star.setImageResource(R.drawable.star_black);
                    star_yellow=false;
                    //刪除資料庫
                }else {
                    holder.star.setImageResource(R.drawable.star_yellow);
                    star_yellow=true;
                    //存進資料庫
                }


            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (model.getTask()==1){
                    intent = new Intent(context, Writing_T1answer1.class);
                    String numString = model.getNum();
                    Pattern pattern = Pattern.compile("\\d+");
                    Matcher matcher = pattern.matcher(numString);
                    if(matcher.find()){
                        String numberString = matcher.group();
                        int number = Integer.parseInt(numberString);
                        intent.putExtra("num",number);

                    }
                    context.startActivity(intent);
                } else if (model.getTask()==-1) {
                    //ai teacher help
                    sharedString=model.getNum();
                    sharedBoolean=true;
                    star_show=true;
                    ((AiTeacherHelp) context).finishCurrentActivity();

                } else if (model.getTask()==2){
                    intent = new Intent(context, Writing_T2answer1.class);
                    String numString = model.getNum();
                    Pattern pattern = Pattern.compile("\\d+");
                    Matcher matcher = pattern.matcher(numString);
                    if(matcher.find()){
                        String numberString = matcher.group();
                        int number = Integer.parseInt(numberString);
                        intent.putExtra("num",number);
                    }
                    context.startActivity(intent);
                }



            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView ques_num;
        ImageView star;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ques_num = itemView.findViewById(R.id.Ques_num);
            star = itemView.findViewById(R.id.star);
        }
    }
}

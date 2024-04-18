package com.example.gproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gproject.Models.QuestionModel;
import com.example.gproject.R;
import com.example.gproject.Speaking.Speaking_part1_answer;
import com.example.gproject.Speaking.Speaking_part2_answer;

import java.util.ArrayList;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.viewholder> {

    Context context;
    ArrayList<QuestionModel>list;

    Boolean star_yellow=false;

    public QuestionAdapter(Context context, ArrayList<QuestionModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_question,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        final QuestionModel model=list.get(position);
        holder.question.setText(model.getQuestion());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(model.getPart()==1){
                    Intent intent =new Intent(context, Speaking_part1_answer.class);
                    intent.putExtra("question",model.getQuestion());
                    context.startActivity(intent);
                } else if (model.getPart()==2) {
                    int position = holder.getAdapterPosition();
                    Bundle bundle=new Bundle();
                    Intent intent =new Intent(context, Speaking_part2_answer.class);
                    bundle.putString("question",model.getQuestion());
                    bundle.putString("num",String.valueOf(position+1));
                    bundle.putString("topic",model.getTopic());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }

            }
        });
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

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        ImageView star,light_bulb;
        TextView question;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            star = itemView.findViewById(R.id.star);
            question= itemView.findViewById(R.id.item_question);
        }

    }
}

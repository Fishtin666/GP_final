package com.example.gproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gproject.AiTeacher.AiTeacher2;
import com.example.gproject.Cross_Topic_qusetion;
import com.example.gproject.Models.TopicModel;
import com.example.gproject.R;

import com.example.gproject.Speaking.Speaking_questionAdd;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.viewholder>{

    Context context;
    ArrayList<TopicModel>list;

    public TopicAdapter(Context context, ArrayList<TopicModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_topic, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        final TopicModel model=list.get(position);

        holder.imageView.setImageResource(model.getImage());
        holder.TopicName.setText(model.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(model.getPart()==0){
                    Bundle bundle=new Bundle();
                    Intent intent =new Intent(context, Speaking_questionAdd.class);
                    bundle.putString("topic",model.getName());
                    bundle.putString("part",String.valueOf(model.getPart()));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
               }  else if (model.getPart()==1) {
                    Bundle bundle=new Bundle();
                    Intent intent=new Intent(context, AiTeacher2.class);
                    bundle.putString("topic",model.getName());
                    bundle.putString("part",String.valueOf(model.getPart()));
                    intent.putExtras(bundle);
                    context.startActivity(intent);

                }else if (model.getPart()==2){
                    Bundle bundle=new Bundle();
                    Intent intent=new Intent(context, Cross_Topic_qusetion.class);
                    bundle.putString("topic",model.getName());
                    bundle.putString("part",String.valueOf(model.getPart()));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }

//                }else if (model.getPart()==3){
//                    Bundle bundle=new Bundle();
//                    Intent intent=new Intent(context, AiTeacher2.class);
//                    bundle.putString("topic",model.getName());
//                    bundle.putString("part",String.valueOf(model.getPart()));
//                    intent.putExtras(bundle);
//                    context.startActivity(intent);
//                }

            }

            private void startActivity(Intent intent) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        CircleImageView imageView;
        TextView TopicName;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            imageView =itemView.findViewById(R.id.listenP);
            TopicName =itemView.findViewById(R.id.listening);

        }
    }
}

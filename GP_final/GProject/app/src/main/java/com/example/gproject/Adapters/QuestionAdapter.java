package com.example.gproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gproject.JustifyTextView4;
import com.example.gproject.Models.QuestionModel;
import com.example.gproject.Question_DataHolder;
import com.example.gproject.R;
import com.example.gproject.Speaking.Speaking_part1_answer;
import com.example.gproject.Speaking.Speaking_part2_answer;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.viewholder> {

    Context context;
    ArrayList<QuestionModel>list;

    Boolean star_yellow=false;
    private DatabaseReference databaseReference;
    FirebaseAuth auth;
    String topic;
    int part;

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
        topic=model.getTopic();
        part=model.getPart();


        InDb(new OnDataReadyListener() {
            int position = holder.getAdapterPosition();
            @Override
            public void onDataReady(ArrayList<String> keyList) {
                // 根据 keyList 判断是否包含当前位置的数据
                if (keyList.contains(String.valueOf(position+1))) {
                    holder.star.setImageResource(R.drawable.star_yellow);
                } else {
                    holder.star.setImageResource(R.drawable.star_black);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(model.getPart()==1){
                    //Intent intent =new Intent(context, Speaking_part1_answer.class);
                    int position = holder.getAdapterPosition();
                    Bundle bundle=new Bundle();
                    Intent intent =new Intent(context, Speaking_part1_answer.class);
                    bundle.putString("question",model.getQuestion());
                    bundle.putString("num",String.valueOf(position+1));
                    bundle.putString("topic",model.getTopic());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    //intent.putExtra("question",model.getQuestion());

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
                databaseReference = FirebaseDatabase.getInstance().getReference();
                auth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = auth.getCurrentUser();

                int position = holder.getAdapterPosition();
                int part=model.getPart();
                String question=model.getQuestion();
                String topic=model.getTopic();

                if(star_yellow){
                    holder.star.setImageResource(R.drawable.star_black);
                    star_yellow=false;

                    //刪除資料庫
                    if (currentUser != null) {
                        String userId = currentUser.getUid();
                        DatabaseReference userAnswersRef = databaseReference
                                .child("empty_question")
                                .child(userId)
                                .child(String.valueOf(part))
                                .child(topic)
                                .child(String.valueOf(position+1));


                        userAnswersRef.removeValue();

                    }
                }else {
                    holder.star.setImageResource(R.drawable.star_yellow);
                    star_yellow=true;
                    //存進資料庫
                    if (currentUser != null) {
                        String userId = currentUser.getUid();

                        DatabaseReference userAnswersRef = databaseReference
                                .child("empty_question")
                                .child(userId)
                                .child(String.valueOf(part))
                                .child(topic)
                                .child(String.valueOf(position+1));

                        userAnswersRef.setValue(question);

                    }

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
            question= itemView.findViewById(R.id.item_question);
            star = itemView.findViewById(R.id.star);


        }

    }

    public void InDb(final OnDataReadyListener listener) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();
        ArrayList<String> keyList=new ArrayList<>();
        DatabaseReference Ref = databaseReference
                .child("empty_question")
                .child(userID)
                .child(String.valueOf(part))
                .child(topic);
        //.child(String.valueOf(position+1))
        Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 遍历每个子节点
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // 获取子节点的键值
                    String key= snapshot.getKey();
                    keyList.add(key);
                }
                listener.onDataReady(keyList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 读取数据失败时的处理
                Log.e("Firebase", "读取数据失败：" + databaseError.getMessage());
            }
        });

       // return keyList;
    }
    public interface OnDataReadyListener {
        void onDataReady(ArrayList<String> keyList);

    }






}

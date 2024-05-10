package com.example.gproject.Adapters;

import static com.example.gproject.Message.SENT_BY_ME;
import static com.example.gproject.Speaking.Speaking_part1_answer.star_show;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
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
import com.example.gproject.Question_DataHolder;
import com.example.gproject.R;
import com.example.gproject.Writing.Writing_T1answer1;
import com.example.gproject.Writing.Writing_T2answer1;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuestionNumberAdapter extends RecyclerView.Adapter<QuestionNumberAdapter.viewHolder>{

    Context context;
    ArrayList<QuestionNumberModel> list;
    public static String sharedString;
    public static boolean sharedBoolean=false;
    String topic;
    int part;

    private DatabaseReference databaseReference;
    FirebaseAuth auth;

    Boolean star_yellow=false;

    ArrayList<String> keyList=new ArrayList<>();

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
        part=model.getTask();

        if(star_show==false){
            holder.star.setVisibility(View.INVISIBLE);

        }else{
            holder.star.setVisibility(View.VISIBLE);

            InDb(new QuestionNumberAdapter.OnDataReadyListener() {
                int position = holder.getAdapterPosition();

                @Override
                public void onDataReady(ArrayList<String> keyList) {
                    System.out.println("keylist:"+keyList);
                    // 根据 keyList 判断是否包含当前位置的数据
                    if (keyList.contains(String.valueOf(position+1))) {
                        holder.star.setImageResource(R.drawable.star_yellow);
                    } else {
                        holder.star.setImageResource(R.drawable.star_black);
                    }
                }
            });
        }


        holder.star.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                databaseReference = FirebaseDatabase.getInstance().getReference();
                auth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = auth.getCurrentUser();

                int position = holder.getAdapterPosition();
                int part=model.getTask();

                if(star_yellow){
                    holder.star.setImageResource(R.drawable.star_black);
                    star_yellow=false;

                    //刪除資料庫
                    if (currentUser != null) {
                        String userId = currentUser.getUid();
                        DatabaseReference userAnswersRef = databaseReference
                                .child("empty_question")
                                .child(userId)
                                .child("Writing")
                                .child(String.valueOf(part))
                                .child(String.valueOf(position + 1));


                        userAnswersRef.removeValue();
                    }
                    Iterator<String> iterator = keyList.iterator();
                    while (iterator.hasNext()) {
                        String element = iterator.next();
                        if (element.equals(String.valueOf(position+1))) {
                            iterator.remove(); // 移除当前元素
                        }
                    }
                    System.out.println("刪除後keylist"+keyList);
                    //notifyDataSetChanged();
                }else {
                    holder.star.setImageResource(R.drawable.star_yellow);
                    star_yellow=true;

                    //存進資料庫
                    if (currentUser != null) {
                        String userId = currentUser.getUid();

                        getQues(position + 1, new FirestoreCallback() {
                            @Override
                            public void onCallback(String question) {
                                if (question != null) {
                                    // Use the retrieved question here
                                    DatabaseReference userAnswersRef = databaseReference
                                            .child("empty_question")
                                            .child(userId)
                                            .child("Writing")
                                            .child(String.valueOf(part))
                                            .child(String.valueOf(position + 1));

                                    userAnswersRef.setValue(question);
                                } else {
                                    // Handle case where question is not found or retrieval fails
                                }
                            }
                        });
                    }
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

    public void InDb(final QuestionNumberAdapter.OnDataReadyListener listener) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();


        DatabaseReference Ref = databaseReference
                .child("empty_question")
                .child(userID)
                .child("Writing")
                .child(String.valueOf(part));

        Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 遍历每个子节点
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // 获取子节点的键值
                        String key= snapshot.getKey();
                        if(!keyList.contains(key))
                            keyList.add(key);
                        System.out.println("keylist初次");
                    }

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

    public interface FirestoreCallback {
        void onCallback(String question);
    }

    public void getQues(int QuesNum, final FirestoreCallback callback){
        final String[] ques = new String[1];
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collection = db.collection("Writing");
        DocumentReference doc = collection.document(String.valueOf(QuesNum));
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    ques[0] = documentSnapshot.getString("Question");
                    callback.onCallback(ques[0]);

                } else {


                }


        }
        });

        //return ques[0];
    }



}

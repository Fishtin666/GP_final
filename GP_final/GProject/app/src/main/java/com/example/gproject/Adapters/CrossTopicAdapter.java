package com.example.gproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.gproject.Models.QuestionModel;
import com.example.gproject.R;

import java.util.ArrayList;

public class CrossTopicAdapter extends RecyclerView.Adapter<CrossTopicAdapter.viewholder>{
    Context context;
    ArrayList<String>list;
    private ArrayList<String> crossQuestions;
    private ArrayList<String> selectedQuestions;

    public CrossTopicAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
        this.crossQuestions = crossQuestions;
        this.selectedQuestions = new ArrayList<>();
    }

    public ArrayList<String> getCrossQuestions() {
        return crossQuestions;
    }

    public void setCrossQuestions(ArrayList<String> crossQuestions) {
        this.crossQuestions = crossQuestions;
    }

    public ArrayList<String> getSelectedQuestions() {
        return selectedQuestions;
    }

    public void setSelectedQuestions(ArrayList<String> selectedQuestions) {
        this.selectedQuestions = selectedQuestions;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cross_topic_question,parent,false);
        return new CrossTopicAdapter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        String question = list.get(position);
        holder.checkBox.setText(question);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        CheckBox checkBox;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String question = checkBox.getText().toString();

                    if (isChecked) {
                        // 如果复选框被选中，则将问题文本添加到选中列表中
                        selectedQuestions.add(question);
                    } else {
                        // 如果复选框取消选中，则从选中列表中移除问题文本
                        selectedQuestions.remove(question);
                    }
                }
            });
        }
    }


}



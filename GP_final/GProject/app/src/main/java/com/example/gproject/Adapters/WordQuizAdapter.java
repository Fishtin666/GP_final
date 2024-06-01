package com.example.gproject.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gproject.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordQuizAdapter extends RecyclerView.Adapter<WordQuizAdapter.ViewHolder> {
    private List<WordQuizData> questions;
    private Context context;
    private boolean answerSubmitted = false;
    private Map<String, String> wordIds;

    public WordQuizAdapter(Context context, List<WordQuizData> questions) {
        this.context = context;
        this.questions = questions;
        this.wordIds = new HashMap<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WordQuizData data = questions.get(position);
        holder.questionTextView.setText(data.getDefinition());
        holder.opt1RadioButton.setText(data.getCorrectWord());
        holder.opt2RadioButton.setText(data.getIncorrectWord());
        holder.itemView.setTag(data.getDocumentId());

        // Remove previous listener to avoid interference
        holder.radioGroup.setOnCheckedChangeListener(null);

        // Restore previous selection
        if (data.getSelectedOption() == 1) {
            holder.radioGroup.check(holder.opt1RadioButton.getId());
        } else if (data.getSelectedOption() == 2) {
            holder.radioGroup.check(holder.opt2RadioButton.getId());
        } else {
            holder.radioGroup.clearCheck();
        }

        // Set listener to update selected option
        holder.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == holder.opt1RadioButton.getId()) {
                data.setSelectedOption(1);
            } else if (checkedId == holder.opt2RadioButton.getId()) {
                data.setSelectedOption(2);
            }
        });

        // If answers have been submitted, disable options and set color
        if (answerSubmitted) {
            holder.opt1RadioButton.setEnabled(false);
            holder.opt2RadioButton.setEnabled(false);

            if (data.getSelectedOption() == 1) {
                if (!data.getDocumentId().equals(data.getCorrectWord())) {
                    holder.opt1RadioButton.setTextColor(Color.RED);
                } else {
                    holder.opt1RadioButton.setTextColor(Color.BLACK);
                }
            } else if (data.getSelectedOption() == 2) {
                if (!data.getDocumentId().equals(data.getIncorrectWord())) {
                    holder.opt2RadioButton.setTextColor(Color.RED);
                } else {
                    holder.opt2RadioButton.setTextColor(Color.BLACK);
                }
            }


        } else {
            holder.opt1RadioButton.setEnabled(true);
            holder.opt2RadioButton.setEnabled(true);
            holder.opt1RadioButton.setTextColor(Color.BLACK);
            holder.opt2RadioButton.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }
    // 添加单词及其对应的 ID
    public void addWordId(String word, String id) {
        wordIds.put(word, id);
    }
    public void setAnswerSubmitted(boolean submitted) {
        this.answerSubmitted = submitted;
        notifyDataSetChanged(); // 更新列表以反映更改
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView questionTextView;
        RadioButton opt1RadioButton;
        RadioButton opt2RadioButton;
        RadioGroup radioGroup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.Que);
            opt1RadioButton = itemView.findViewById(R.id.A1);
            opt2RadioButton = itemView.findViewById(R.id.A2);
            radioGroup = itemView.findViewById(R.id.radioGroup);
        }

        public void setOptionTextColor(int option, int color) {
            if (option == 1) {
                opt1RadioButton.setTextColor(color);
            } else if (option == 2) {
                opt2RadioButton.setTextColor(color);
            }
        }
    }


    public List<WordQuizData> getQuestions() {
        return questions;
    }
}


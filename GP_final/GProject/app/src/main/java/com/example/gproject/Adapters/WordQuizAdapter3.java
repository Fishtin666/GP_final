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

public class WordQuizAdapter3 extends RecyclerView.Adapter<WordQuizAdapter3.ViewHolder> {
    private List<WordQuizData> questions;
    private Context context;
    private WordQuizData selectedWord;
    private RecyclerView recyclerView;
    private Map<String, String> wordIds;
    private boolean answerSubmitted = false;

    public WordQuizAdapter3(Context context, List<WordQuizData> questions) {
        this.context = context;
        this.questions = questions;
        this.recyclerView = recyclerView;
        this.wordIds = new HashMap<>();
        this.selectedWord = new WordQuizData("", "","", "","");  // 初始化 selectedWord
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WordQuizData question = questions.get(position);
        String questionText = context.getString(R.string.question_text_format, question.getDefinition(), question.getPartOfSpeech());
        holder.questionTextView.setText(questionText);
        holder.opt1RadioButton.setText(question.getCorrectWord());
        holder.opt2RadioButton.setText(question.getIncorrectWord());
        holder.itemView.setTag(question.getDocumentId());

//        if (answerSubmitted) {
//            holder.opt1RadioButton.setEnabled(false);
//            holder.opt2RadioButton.setEnabled(false);
//        } else {
//            holder.opt1RadioButton.setEnabled(true);
//            holder.opt2RadioButton.setEnabled(true);
//        }

        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (answerSubmitted) return; // 如果答案已提交，不再響應點擊事件

                int currentPosition = holder.getAdapterPosition();

                if (checkedId == R.id.A1) {
                    questions.get(currentPosition).setSelectedOption(1);
                } else if (checkedId == R.id.A2) {
                    questions.get(currentPosition).setSelectedOption(2);
                } else {
                    questions.get(currentPosition).setSelectedOption(-1);
                }

                selectedWord = questions.get(currentPosition);
            }
        });
//        holder.opt1RadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                question.setSelected(isChecked);
//            }
//        });
//
//        holder.opt2RadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                question.setSelected(isChecked);
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return questions.size();
    }
    public void markCorrectAnswer2(String correctAnswer) {
        for (int i = 0; i < getItemCount(); i++) {
            WordQuizData word = questions.get(i);
            if (word.getCorrectWord().equals(correctAnswer)) {
                // 找到正确答案，将相应的选项按钮标记为红色
                ViewHolder viewHolder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
                if (viewHolder != null) {
                    viewHolder.radioButton.setTextColor(Color.RED);
                }
                break; // 找到正确答案后，停止循环
            }
        }
    }
    public void markCorrectAnswer(int position) {
        ViewHolder viewHolder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
        if (viewHolder != null) {
            viewHolder.radioButton.setTextColor(Color.RED);
        }
    }

    public void setAnswerSubmitted(boolean submitted) {
        this.answerSubmitted = submitted;
    }
    public RadioButton getSelectedRadioButton(int position, RecyclerView recyclerView) {
        ViewHolder viewHolder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
        if (viewHolder != null) {
            if (viewHolder.opt1RadioButton.isChecked()) {
                return viewHolder.opt1RadioButton;
            } else if (viewHolder.opt2RadioButton.isChecked()) {
                return viewHolder.opt2RadioButton;
            }
        }
        return null;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView questionTextView;
        RadioButton opt1RadioButton;
        RadioButton opt2RadioButton;
        RadioGroup radioGroup;
        public RadioButton radioButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.Que);
            opt1RadioButton = itemView.findViewById(R.id.A1);
            opt2RadioButton = itemView.findViewById(R.id.A2);
            radioGroup = itemView.findViewById(R.id.radioGroup);

        }
    }
    public RadioButton getRadioButtonAtPosition(int position, RecyclerView recyclerView) {
        if (position >= 0 && position < questions.size()) {
            ViewHolder viewHolder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
            if (viewHolder != null) {
                return viewHolder.opt1RadioButton.isChecked() ? viewHolder.opt1RadioButton :
                        viewHolder.opt2RadioButton.isChecked() ? viewHolder.opt2RadioButton : null;
            }
        }
        return null;
    }
    // 添加单词及其对应的 ID
    public void addWordId(String word, String id) {
        wordIds.put(word, id);
    }
//    public RadioButton getSelectedRadioButton(int position) {
//        ViewHolder viewHolder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
//        if (viewHolder != null) {
//            return viewHolder.radioButton;
//        }
//        return null;
//    }

    // 获取单词的 ID
    public String getWordId(String word) {
        return wordIds.get(word);
    }
    public void setQuestions(List<WordQuizData> questions) {
        this.questions = questions;
        notifyDataSetChanged();
    }
    public WordQuizData getSelectedWord() {
        return selectedWord;
    }
    public List<WordQuizData> getQuestions() {
        return questions;
    }

}

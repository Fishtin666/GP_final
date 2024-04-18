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
    private WordQuizData selectedWord;
    private RecyclerView recyclerView;
    private Map<String, String> wordIds;

    public WordQuizAdapter(Context context, List<WordQuizData> questions) {
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
//        holder.questionTextView.setText(question.getDefinition() );
        String questionText = context.getString(R.string.question_text_format, question.getDefinition(), question.getPartOfSpeech());
        holder.questionTextView.setText(questionText);
        holder.opt1RadioButton.setText(question.getCorrectWord());
        holder.opt2RadioButton.setText(question.getIncorrectWord());
        holder.itemView.setTag(question.getDocumentId());

        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 使用 holder.getAdapterPosition() 获取当前准确的位置
                int currentPosition = holder.getAdapterPosition();

                // 更新 selectedOption
                if (checkedId == R.id.A1) {
                    questions.get(currentPosition).setSelectedOption(1);
                } else if (checkedId == R.id.A2) {
                    questions.get(currentPosition).setSelectedOption(2);
                } else {
                    questions.get(currentPosition).setSelectedOption(-1);  // 表示未选择
                }

                // 使用 holder.getAdapterPosition() 获取当前准确的位置
                selectedWord = questions.get(currentPosition);
            }
        });
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

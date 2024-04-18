package com.example.gproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gproject.R;

import java.util.List;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.ViewHolder> {

    private List<WordListData> dataList;
    private static OnItemClickListener listener;

    public WordListAdapter(List<WordListData> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.word_card, parent, false);

        // back to ViewHolder
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get DataModel
        WordListData data = dataList.get(position);

        // set Data into ViewHolder
        holder.wordTextView.setText(data.getWord());
        holder.phoneticTextView.setText(data.getPhonetic());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public List<WordListData> getDataList() {
        return dataList;
    }

    // ViewHolder 类
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView wordTextView;
        public TextView phoneticTextView;
        public View cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // 找到布局中的元素
            wordTextView = itemView.findViewById(R.id.word_textview);
            phoneticTextView = itemView.findViewById(R.id.phonetic_textview);

            // Set click listener for item view
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
    //set clickListener
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        WordListAdapter.listener = listener;

    }
    public WordListData getItem(int position) {
        if (position < dataList.size()) {
            return dataList.get(position);
        }
        return null;
    }


}


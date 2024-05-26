package com.example.gproject.Adapters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gproject.R;
import com.example.gproject.Adapters.WordListData;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.ViewHolder> {

    private List<WordListData> dataList;
    private static OnItemClickListener listener;
    private Context context;

    private WordListData deletedItem = null; // 用来记录左滑删除的Item

    public WordListAdapter(List<WordListData> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.word_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WordListData data = dataList.get(position);
        holder.wordTextView.setText(data.getWord());
        holder.phoneticTextView.setText(data.getPhonetic());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void attachItemTouchHelperToRecyclerView(RecyclerView recyclerView) {
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int position_dragged = viewHolder.getBindingAdapterPosition();
                int position_target = target.getBindingAdapterPosition();

                Log.e("position_f", String.valueOf(viewHolder.getBindingAdapterPosition()));
                Log.e("position_b", String.valueOf(target.getBindingAdapterPosition()));

                Collections.swap(dataList, position_dragged, position_target);
                notifyItemMoved(position_dragged, position_target);

                return true;
            }

            //            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                int position = viewHolder.getBindingAdapterPosition();
//                deletedItem = dataList.get(position);
//                String deletedWord = deletedItem.getWord(); // 获取单词名称
//                //database
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                String userId = user.getUid();
//                FirebaseDatabase db = FirebaseDatabase.getInstance();
//                DatabaseReference root = db.getReference("word_collect");
//
//                // 展示确认对话框
//                new AlertDialog.Builder(context)
//                        .setTitle("確認刪除")
//                        .setMessage("確定要刪除此選項嗎？")
//                        .setPositiveButton("是", (dialog, which) -> {
//
//                            if (user != null) {
//                                root.child(userId).child(deletedWord).removeValue();
//                                Log.e("collect", "Deleted from collection " + userId);
//                                notifyDataSetChanged();
//                            } else {
//                                Log.e("collect", "User not authenticated");
//                            }
////                            dataList.remove(position); // 从dataList中删除项
////                            notifyItemRemoved(position); // 通知适配器数据已删除
//
//                            Snackbar.make(viewHolder.itemView, deletedWord + " 已被刪除", Snackbar.LENGTH_LONG)
//                                    .setAction("恢復", view -> {
//                                        root.child(userId).child(deletedWord).setValue(deletedItem.getWord());
////                                        dataList.add(position, deletedItem); // 恢复删除的项
////                                        notifyItemInserted(position); // 通知适配器数据已插入
////                                        deletedItem = null; // 重置deletedItem，防止重复操作
////                                        notifyDataSetChanged(); // 重新加载适配器
//                                    }).show();
//                            notifyDataSetChanged(); // 重新加载适配器
//                        })
//                        .setNegativeButton("否", (dialog, which) -> {
//                            notifyDataSetChanged();
//                            notifyItemChanged(viewHolder.getBindingAdapterPosition()); // 重新加载该项以撤销滑动操作
//                            deletedItem = null; // 重置deletedItem，防止重复操作
//                        })
//                        .setOnDismissListener(dialog -> {
//                            notifyItemChanged(viewHolder.getBindingAdapterPosition()); // 确保对话框关闭后重新加载该项
//                            deletedItem = null; // 重置deletedItem，防止重复操作
//                        })
//                        .show();
//            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                deletedItem = dataList.get(position);
                String deletedWord = deletedItem.getWord(); // 獲取要刪除的單詞

                // 從數據庫刪除單詞
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userId = user.getUid();
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference root = db.getReference("word_collect").child(userId);
                root.child(deletedWord).removeValue();

//                // 刪除 dataList 中的項目
//                dataList.remove(position);

                dataList.clear();

//                // 通知適配器該項目已刪除
//                notifyItemRemoved(position);
//
//                notifyItemRangeChanged(position, dataList.size());

                notifyDataSetChanged();

//                // 顯示Snackbar提示用戶
//                Snackbar.make(viewHolder.itemView, deletedWord + "已被刪除", Snackbar.LENGTH_LONG)
//                        .setAction("恢復", view -> {
//                            root.setValue(deletedWord);
//                        }).show();
            }

//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                //管理RecyclerView滑動操作
//                int position = viewHolder.getAdapterPosition();
//                switch (direction) {
//                    case ItemTouchHelper.LEFT:
//                    case ItemTouchHelper.RIGHT:
//                        //刪除List中的position項資料
//                        dataList.remove(position);
//                        //於RecyclerView中刪除position項資料
//                        notifyItemRemoved(position);
//                        break;
//                }
//            }


            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(Color.parseColor("#CB1B45"))
                        .addActionIcon(R.drawable.ic_baseline_delete)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        });

        helper.attachToRecyclerView(recyclerView);
    }


    // ViewHolder 类
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView wordTextView;
        public TextView phoneticTextView;
        public View cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            wordTextView = itemView.findViewById(R.id.word_textview);
            phoneticTextView = itemView.findViewById(R.id.phonetic_textview);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    public List<WordListData> getDataList() {
        return dataList;
    }

    // set clickListener
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

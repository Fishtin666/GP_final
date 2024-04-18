package com.example.gproject;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder>{
    List<Message> messageList;  //用來儲存聊天訊息

    //當建立 MessageAdapter 對象時，需要傳遞一個 List<Message> 作為參數，並將其保存在 messageList 變數中。
    public MessageAdapter(List<Message> messageList) {
        this.messageList=messageList;
    }

    @NonNull  //參數或返回值不允許為空
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chatView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,null);
        MyViewHolder myViewHolder=new MyViewHolder(chatView);
        return myViewHolder;
    }

    //看訊息是誰傳的(me,bot)
    //更新列表時調用
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Message message =messageList.get(position);
        if(message.getSendBy().equals(Message.SENT_BY_ME)){
            //me:左隱藏、右顯現
            holder.leftChatView.setVisibility(View.GONE);
            holder.rightChatView.setVisibility(View.VISIBLE);
            holder.rightTextView.setText(message.getMessage());
        }else{
            //bot
            holder.rightChatView.setVisibility(View.GONE);
            holder.leftChatView.setVisibility(View.VISIBLE);
            holder.leftTextView.setText(message.getMessage());
        }
    }

    //返回聊天訊息的數量
    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftChatView,rightChatView;
        TextView leftTextView,rightTextView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            leftChatView=itemView.findViewById(R.id.left_chat_view);
            rightChatView=itemView.findViewById(R.id.right_chat_view);
            leftTextView=itemView.findViewById(R.id.left_chat_text_view);
            rightTextView=itemView.findViewById(R.id.right_chat_text_view);

        }
    }
}


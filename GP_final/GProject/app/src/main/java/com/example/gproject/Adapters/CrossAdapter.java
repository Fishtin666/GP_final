package com.example.gproject.Adapters;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gproject.CrossTopic_show;
import com.example.gproject.Models.TopicModel;
import com.example.gproject.R;
import com.example.gproject.Speaking.Speaking_questionAdd;

import java.net.CookieHandler;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CrossAdapter extends RecyclerView.Adapter<CrossAdapter.viewholder>{

    ArrayList<String> arrayList=new ArrayList<>();
    ArrayList<String> arrayList1=new ArrayList<>();
    ArrayList<String> arrayList2=new ArrayList<>();
    Context context;

    public CrossAdapter(Context context,ArrayList<String> arrayList, ArrayList<String> arrayList1, ArrayList<String> arrayList2) {
        this.arrayList = arrayList;
        this.arrayList1 = arrayList1;
        this.arrayList2 = arrayList2;
        this.context = context;
    }


    public ArrayList<String> getArrayList1() {
        return arrayList1;
    }

    public void setArrayList1(ArrayList<String> arrayList1) {
        this.arrayList1 = arrayList1;
    }

    public ArrayList<String> getArrayList2() {
        return arrayList2;
    }

    public void setArrayList2(ArrayList<String> arrayList2) {
        this.arrayList2 = arrayList2;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<String> getArrayList() {
        return arrayList;

    }

    public void setArrayList(ArrayList<String> arrayList) {
        this.arrayList = arrayList;

    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cross_adapter, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CrossAdapter.viewholder holder, int position) {

        holder.textView.setText(arrayList.get(position));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Bundle bundle=new Bundle();
                Intent intent =new Intent(context, CrossTopic_show.class);
                bundle.putString("question",arrayList.get(position));
                bundle.putString("ans",arrayList1.get(position));
                bundle.putString("key",arrayList2.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        TextView textView;
        CardView cardView;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.item_question);
            cardView=itemView.findViewById(R.id.cardView1);

        }
    }
}

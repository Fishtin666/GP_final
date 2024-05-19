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

import com.example.gproject.Models.TopicModel;
import com.example.gproject.R;
import com.example.gproject.Speaking.Speaking_questionAdd;

import java.net.CookieHandler;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CrossAdapter extends RecyclerView.Adapter<CrossAdapter.viewholder>{

    ArrayList<String> arrayList=new ArrayList<>();
    Context context;

    public CrossAdapter(Context context ,ArrayList<String> arrayList) {
        this.arrayList = arrayList;
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
                Intent intent =new Intent(context, Speaking_questionAdd.class);
                bundle.putString("question",arrayList.get(position));
                //bundle.putString("part",String.valueOf(model.getPart()));
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

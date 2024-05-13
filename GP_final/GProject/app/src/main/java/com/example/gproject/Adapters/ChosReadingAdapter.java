package com.example.gproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gproject.R;
import com.example.gproject.reading.R_blank;
import com.example.gproject.reading.R_chose;
import com.example.gproject.reading.R_judge;
import com.example.gproject.reading.R_match;
import com.example.gproject.reading.R_multiple;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class ChosReadingAdapter extends RecyclerView.Adapter<ChosReadingAdapter.MyViewHolder> {

    private Context context;
    private List<String> documentIds;
    private int chosenNumber;

    public ChosReadingAdapter(Context context, int chosenNumber) {
        this.context = context;
        this.chosenNumber = chosenNumber;
        this.documentIds = new ArrayList<>();
        fetchDocumentIds(chosenNumber);
    }

    // Fetch document IDs from Firestore based on chosenNumber
    private void fetchDocumentIds(int chosenNumber) {
        String collectionName;
        switch (chosenNumber) {
            case 1:
                collectionName = "R_blank";
                break;
            case 2:
                collectionName = "R_judge";
                break;
            case 3:
                collectionName = "R_chose";
                break;
            case 4:
                collectionName = "R_match";
                break;
            case 5:
                collectionName = "R_multiple";
                break;
            default:
                Log.e("chose error", "Invalid number: " + chosenNumber);
                return;
        }

        FirebaseFirestore.getInstance().collection(collectionName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            documentIds.add(document.getId());
                        }
                        notifyDataSetChanged();
                    } else {
                        Log.e("Firestore", "Error getting documents.", task.getException());
                    }
                });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_help, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String documentId = documentIds.get(position);
        int Dnumber = Integer.parseInt(documentId);
        String text = "Test" + documentId;
        holder.textView.setText(text);
        holder.itemView.setOnClickListener(v -> {
            // Handle item click, return chosenNumber
            switch (chosenNumber) {
                case 1:
                    Intent intent1 = new Intent(context, R_blank.class);
                    // 在這裡可以添加任何你希望添加的額外數據
                    intent1.putExtra("DocumentId", Dnumber);
                    context.startActivity(intent1);
                    break;
                case 2:
                    Intent intent2 = new Intent(context, R_judge.class);
                    intent2.putExtra("DocumentId", Dnumber);
                    context.startActivity(intent2);
                    break;
                case 3:
                    Intent intent3 = new Intent(context, R_chose.class);
                    intent3.putExtra("DocumentId", Dnumber);
                    context.startActivity(intent3);
                    break;
                case 4:
                    Intent intent4 = new Intent(context, R_match.class);
                    intent4.putExtra("DocumentId", Dnumber);
                    context.startActivity(intent4);
                    break;
                case 5:
                    Intent intent5 = new Intent(context, R_multiple.class);
                    intent5.putExtra("DocumentId", Dnumber);
                    context.startActivity(intent5);
                    break;
                default:
                    Log.e("chose error", "Invalid number: " + chosenNumber);
            }
        });
    }

    @Override
    public int getItemCount() {
        return documentIds.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.Ques_num);
        }
    }
    public void CollectReading(){

    }
}

package com.example.gproject.Adapters;

import static android.os.Build.VERSION_CODES.R;

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
                .inflate(R.layout.item_question_number, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String documentId = documentIds.get(position);
        holder.textView.setText(documentId);
        holder.itemView.setOnClickListener(v -> {
            // Handle item click, return anotherNumber
            Intent intent = new Intent();
            intent.putExtra("ChoseNumber", documentId);
            context.startActivity(intent);
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
}

package com.example.gproject.dictionary;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.gproject.databinding.MeaningRecyclerRowBinding;

import java.util.List;
import java.util.stream.IntStream;

public class MeaningAdapter extends RecyclerView.Adapter<MeaningAdapter.MeaningViewHolder> {

    public String getDefinitionsText;
    private List<WordResult.Meaning> meaningList;
    public static String definitionsText;

    public MeaningAdapter(List<WordResult.Meaning> meaningList) {
        this.meaningList = meaningList;
    }

    public String getDefinitionsText() {
        return definitionsText;
    }

    public void setDefinitionsText(String definitionsText) {
        this.definitionsText = definitionsText;
    }


    public static class MeaningViewHolder extends RecyclerView.ViewHolder {

        private MeaningRecyclerRowBinding binding;

        public MeaningViewHolder(MeaningRecyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(WordResult.Meaning meaning) {
            binding.partOfSpeechTextview.setText(meaning.partOfSpeech);
            //binding.definitionsTextview.setText(joinDefinitions(meaning.definitions);
            //List<Definition> definitionsList = Definition.;
            binding.definitionsTextview.setText(
                    TextUtils.join("\n\n",
                            IntStream.range(0, meaning.definitions.size())
                                    .mapToObj(index -> (index + 1) + ". " + meaning.definitions.get(index).definition)
                                    .toArray(String[]::new)
                    )
            );
            definitionsText=binding.definitionsTextview.getText().toString();
            if (meaning.synonyms.isEmpty()) {
                binding.synonymsTitleTextview.setVisibility(View.GONE);
                binding.synonymsTextview.setVisibility(View.GONE);
            } else {
                binding.synonymsTitleTextview.setVisibility(View.VISIBLE);
                binding.synonymsTextview.setVisibility(View.VISIBLE);
                binding.synonymsTextview.setText(joinStrings(meaning.synonyms, ", "));
            }

            if (meaning.antonyms.isEmpty()) {
                binding.antonymsTitleTextview.setVisibility(View.GONE);
                binding.antonymsTextview.setVisibility(View.GONE);
            } else {
                binding.antonymsTitleTextview.setVisibility(View.VISIBLE);
                binding.antonymsTextview.setVisibility(View.VISIBLE);
                binding.antonymsTextview.setText(joinStrings(meaning.antonyms, ", "));
            }
        }

        private String joinDefinitions(List<Definition> definitions) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < definitions.size(); i++) {
                Definition definition = definitions.get(i);
                builder.append(i + 1).append(". ").append(definition.getDefinition()).append("\n\n");
            }
            return builder.toString();
        }

        private String joinStrings(List<String> strings, String delimiter) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < strings.size(); i++) {
                builder.append(strings.get(i));
                if (i < strings.size() - 1) {
                    builder.append(delimiter);
                }
            }
            return builder.toString();
        }

        public String getDefinitionsText() {
            return definitionsText;
        }
    }

    public void updateNewData(List<WordResult.Meaning> newMeaningList) {
        meaningList = newMeaningList;
        notifyDataSetChanged();
    }

    @Override
    public MeaningViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MeaningRecyclerRowBinding binding = MeaningRecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MeaningViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MeaningViewHolder holder, int position) {
        holder.bind(meaningList.get(position));
    }

    @Override
    public int getItemCount() {
        return meaningList.size();
    }

    public List<WordResult.Meaning> getMeaningList() {
        return meaningList;
    }

    public void setMeaningList(List<WordResult.Meaning> meaningList) {
        this.meaningList = meaningList;
    }
}

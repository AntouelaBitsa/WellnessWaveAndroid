package com.wellnesswaveandroid.wellnesswaveandroid.Utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wellnesswaveandroid.wellnesswaveandroid.R;

import java.util.List;

public class SpecializationCarouselAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SpecializationSet> specializationList;
    private Context context;

    public SpecializationCarouselAdapter(List<SpecializationSet> specializatinList, Context context) {
        this.specializationList = specializatinList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View specializationView = LayoutInflater.from(context).inflate(R.layout.carousel_doc_specialization_item, parent, false);
        return new SpecializationViewHolder(specializationView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(position >= specializationList.size()){
            // Log an error in case of an invalid position
            Log.e("CarouselAdapter", "Invalid position: " + position + ", List size: " + specializationList.size());
            Toast toast = Toast.makeText(context, "This was all the Specializations!", Toast.LENGTH_SHORT);
            return;
        }
        SpecializationSet specSet = specializationList.get(position);
        ((SpecializationViewHolder) holder).specializationTxt.setText(specSet.getSpecializationType());
        ((SpecializationViewHolder) holder).specializationImgView.setImageDrawable(specSet.getSpecializationIcon());
    }

    @Override
    public int getItemCount() {
        if (specializationList.isEmpty()){
            return 0;
        }
        return specializationList.size() + 1;
    }

    public class SpecializationViewHolder extends RecyclerView.ViewHolder{
        TextView specializationTxt;
        ImageView specializationImgView;

        public SpecializationViewHolder(@NonNull View itemView) {
            super(itemView);
            specializationTxt = itemView.findViewById(R.id.cardItemSpecializationTxt);
            specializationImgView = itemView.findViewById(R.id.cardItemImgSpecialization);
        }
    }
}

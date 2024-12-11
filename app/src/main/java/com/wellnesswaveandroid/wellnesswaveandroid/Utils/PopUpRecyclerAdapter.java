package com.wellnesswaveandroid.wellnesswaveandroid.Utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wellnesswaveandroid.wellnesswaveandroid.Activities.BookAppointmentActivity;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Doctor;
import com.wellnesswaveandroid.wellnesswaveandroid.R;

import java.util.List;

public class PopUpRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Doctor> doctorList;
    private Context context;

    public PopUpRecyclerAdapter(List<Doctor> docSpecializedList, Context context) {
        this.doctorList = docSpecializedList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View specDocsView = LayoutInflater.from(context).inflate(R.layout.carousel_specialization_item, parent, false);
        return new SpecializationPopUpItem(specDocsView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position < doctorList.size()){
            Doctor spcDocItem = doctorList.get(position);
            String dr = "DR. ";
            ((SpecializationPopUpItem) holder).fullNameTxt.setText(dr.concat(spcDocItem.getDocFirstName()).
                    concat(" ").concat(spcDocItem.getDocLastName()));
            ((SpecializationPopUpItem) holder).phoneTxt.setText(spcDocItem.getDocPhoneNum());
            ((SpecializationPopUpItem) holder).addressTxt.setText(spcDocItem.getDocAddress());
            //TODO: Button Listener
            ((SpecializationPopUpItem) holder).bookAppointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goToBookAppointment = new Intent(context, BookAppointmentActivity.class);
                    context.startActivity(goToBookAppointment);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (doctorList.isEmpty()){
            return 0;
        }
        return doctorList.size();
    }

    public class SpecializationPopUpItem extends RecyclerView.ViewHolder{
        TextView fullNameTxt, phoneTxt, addressTxt;
        Button bookAppointment;

        public SpecializationPopUpItem(@NonNull View itemView) {
            super(itemView);
            fullNameTxt = itemView.findViewById(R.id.specRecyclTxt);
            phoneTxt = itemView.findViewById(R.id.phoneNumRecyclTxt);
            addressTxt = itemView.findViewById(R.id.addressRecyclrTxt);
            bookAppointment = itemView.findViewById(R.id.bookRecyclrBtn);
        }
    }
}

package com.wellnesswaveandroid.wellnesswaveandroid.Utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wellnesswaveandroid.wellnesswaveandroid.Activities.BookAppointmentActivity;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Appointments;
import com.wellnesswaveandroid.wellnesswaveandroid.R;

import java.util.List;

public class PatAppointmentCarouselAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_APPOINTMENT = 0;
    private static final int TYPE_ADD = 1;

    private List<Appointments> appointmentList;
    private Context context;

    //Constructor of Carousel
    public PatAppointmentCarouselAdapter(List<Appointments> appointmentList, Context context) {
        this.appointmentList = appointmentList;
        this.context = context;
    }

    //Returns the size of the list to produce the right amount of card items
    @Override
    public int getItemViewType(int position) {
        if (position < appointmentList.size()){
            return TYPE_APPOINTMENT;
        }else {
            return TYPE_ADD;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_APPOINTMENT){
            View appointmentView = LayoutInflater.from(context).inflate(R.layout.carousel_pat_appointment_item, parent, false);
            return new ItemViewHolder(appointmentView);
        }else {
            View addView = LayoutInflater.from(context).inflate(R.layout.carousel_add_item, parent, false);
            return new AddViewHolder(addView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_APPOINTMENT){
            Appointments appoint = appointmentList.get(position);
            ((ItemViewHolder) holder).docProfAppntmntTxt.setText(appoint.getDoctor().getDocProfession());
            ((ItemViewHolder) holder).dateAppntmntTxt.setText(appoint.getDate());
            ((ItemViewHolder) holder).infoAppntmntBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: Activity -> Appointment Info
                    //TODO: Intent for navigation to screen of appointments info
                    //TODO: Appointment info class
                }
            });
        }else {
            ((AddViewHolder) holder).addImageView.setOnClickListener(new View.OnClickListener() {
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
        if (appointmentList.isEmpty()){
            return 0;
        }
        return appointmentList.size() + 1;
    }

    //Initialize Appointment Card Item
    public class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView docProfAppntmntTxt, dateAppntmntTxt;
        Button infoAppntmntBtn;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            docProfAppntmntTxt = itemView.findViewById(R.id.cardItemDocProfTxt);
            dateAppntmntTxt = itemView.findViewById(R.id.cardItemDateDocTxt);
            infoAppntmntBtn = itemView.findViewById(R.id.appointCrslCrdItmPatInfolBtn);
        }
    }

    //Initialize Add Card Item
    public class AddViewHolder extends RecyclerView.ViewHolder{
        ImageView addImageView;

        public AddViewHolder(@NonNull View itemView) {
            super(itemView);
            addImageView = itemView.findViewById(R.id.cardItemImgSpecialization);
        }
    }

    //TODO: create specialisation View Holder Class that extends RecyclyerView.ViewHolder
}

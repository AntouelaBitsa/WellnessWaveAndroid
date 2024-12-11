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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.wellnesswaveandroid.wellnesswaveandroid.Activities.BookAppointmentActivity;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Appointments;
import com.wellnesswaveandroid.wellnesswaveandroid.R;

import java.util.List;

public class PatAppointmentCarouselAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_APPOINTMENT = 0;
    private static final int TYPE_ADD = 1;
    private static final int TYPE_EMPTY_STATE = 2;

    private List<Appointments> appointmentList;
    private Context context;
    private OnInfoBtnListener btnListener;
    private int selectedPos = RecyclerView.NO_POSITION;

    public interface OnInfoBtnListener{
        void onInfoButtonClick(Appointments appoint);
    }

    //Constructor of Carousel
    public PatAppointmentCarouselAdapter(List<Appointments> appointmentList, Context context, OnInfoBtnListener onInfoBtnListener) {
        this.appointmentList = appointmentList;
        this.context = context;
        this.btnListener = onInfoBtnListener;
    }

    //Returns the size of the list to produce the right amount of card items
    @Override
    public int getItemViewType(int position) {
        if (appointmentList.isEmpty()){
            return TYPE_EMPTY_STATE;
        } else if (position < appointmentList.size()) {
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
        }else if (viewType == TYPE_ADD){
            View addView = LayoutInflater.from(context).inflate(R.layout.carousel_add_item, parent, false);
            return new AddViewHolder(addView);
        }else {
            View emptyStateView = LayoutInflater.from(context).inflate(R.layout.empty_state_new_appointment, parent,false);
            return new EmptyStateHolder(emptyStateView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_APPOINTMENT){
            Appointments appoint = appointmentList.get(position);
            ((ItemViewHolder) holder).docProfAppntmntTxt.setText(appoint.getDoctor().getDocProfession());
            ((ItemViewHolder) holder).dateAppntmntTxt.setText(appoint.getDate());
            //TODO: pop up of appoint info in home page
            ((ItemViewHolder) holder).bind(appoint, btnListener, position);
        }else if (getItemViewType(position) == TYPE_ADD){
            ((AddViewHolder) holder).addImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goToBookAppointment = new Intent(context, BookAppointmentActivity.class);
                    context.startActivity(goToBookAppointment);
                }
            });
        }else {
            ((EmptyStateHolder) holder).emptyStateBookAppointmentBtn.setOnClickListener(new View.OnClickListener() {
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
            return 1;
        }
        return appointmentList.size() + 1;
    }

    //Initialize Appointment Card Item
    public class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView docProfAppntmntTxt, dateAppntmntTxt;
//        Button infoAppntmntBtn;
        MaterialCardView patCardView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            docProfAppntmntTxt = itemView.findViewById(R.id.cardItemDocProfTxt);
            dateAppntmntTxt = itemView.findViewById(R.id.cardItemDateDocTxt);
//            infoAppntmntBtn = itemView.findViewById(R.id.appointCrslCrdItmPatInfolBtn);
            patCardView = itemView.findViewById(R.id.carouselItemDocAppointments);
        }

        public void bind(Appointments appoint, OnInfoBtnListener btnListener, int position) {
            if (selectedPos != position) {
                // Change back to the original "sand" color
                patCardView.setCardBackgroundColor(
                        ContextCompat.getColor(patCardView.getContext(), R.color.sand)
                );
            } else {
                // Change to "grey" color
                patCardView.setCardBackgroundColor(
                        ContextCompat.getColor(patCardView.getContext(), R.color.grey)
                );
            }

            patCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (btnListener != null){
                        btnListener.onInfoButtonClick(appoint);
                    }
                    // Update the selected position
                    int previousSelectedPosition = selectedPos;
                    selectedPos = getAdapterPosition();

                    // Notify the adapter to update the previous and current items
                    notifyItemChanged(previousSelectedPosition);
                    notifyItemChanged(selectedPos);
                }
            });
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

    public class EmptyStateHolder extends RecyclerView.ViewHolder{
        Button emptyStateBookAppointmentBtn;

        public EmptyStateHolder(@NonNull View itemView) {
            super(itemView);
            emptyStateBookAppointmentBtn = itemView.findViewById(R.id.emptyStateBookAppointBtn);
        }
    }
}

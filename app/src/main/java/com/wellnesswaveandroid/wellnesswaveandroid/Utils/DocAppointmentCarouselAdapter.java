package com.wellnesswaveandroid.wellnesswaveandroid.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.wellnesswaveandroid.wellnesswaveandroid.Activities.BookAppointmentActivity;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Appointments;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Doctor;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Patient;
import com.wellnesswaveandroid.wellnesswaveandroid.R;

import java.util.List;

public class DocAppointmentCarouselAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Appointments> docAppointList;
    private Context context;
    private OnInfoBtnListener btnListener;
    private int selectedPos = RecyclerView.NO_POSITION;

    public  interface OnInfoBtnListener{
        void onInfoButtonClick(Appointments appoint);
    }

    public DocAppointmentCarouselAdapter(List<Appointments> docAppoint, Context context, OnInfoBtnListener onInfoBtnListener) {
        this.docAppointList = docAppoint;
        this.context = context;
        this.btnListener = onInfoBtnListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0){
            View emptyStateAppointments = LayoutInflater.from(context).inflate(R.layout.empty_state_doc_appointments_status, parent, false);
            return new EmptyStateDocHolder(emptyStateAppointments);
        }

        View appointmentView = LayoutInflater.from(context).inflate(R.layout.carousel_doc_appointment_item, parent, false);
        return new AppointmentViewHolder(appointmentView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //DONE: find what is the problem that i'm getting one more card item in run, except for the list of items
        System.out.println("SOS Position = " + position);

        if(position < docAppointList.size()){
            Log.d("AppointmentAdapter", "Size of appointmentList: " + docAppointList.size());
            Log.d("AppointmentAdapter", "onBindViewHolder: position = " + position);
            Appointments appoint = docAppointList.get(position);
            Patient onAppointmentPatient = docAppointList.get(position).getPatient();
            //DONE: change to get patients name
            String fullName = appoint.getPatient().getPatFirstName().concat(" ").concat(appoint.getPatient().getPatLastName());
            System.out.println("[-1-onBindViewHolder()]: fullName= " + fullName);
            ((AppointmentViewHolder) holder).fullNamePatTxt.setText(fullName);
            ((AppointmentViewHolder) holder).appointDateTxt.setText(appoint.getDate());
            System.out.println();
            /**
             * DONE: must populate data to the detail-layout in home page
             * **/
            ((AppointmentViewHolder) holder).bind(appoint, btnListener, position);

        }else {
            Log.e("DocCarouselAdapter", "Invalid position: " + position + ", List size: " + docAppointList.size());
//            Toast toast = Toast.makeText(context, "This was all the Specializations!", Toast.LENGTH_SHORT);
//            return;
            Doctor instantDoc = Doctor.getInstance();
            if (instantDoc == null){
                Toast toast = Toast.makeText(context, "Cannot find Doctor Name, Error Occurred", Toast.LENGTH_SHORT);
                Log.d("DocCarouselAdapter", "Doctor Instance is null");
                return;
            }
//            String dr = "Dr. ";
//            ((EmptyStateDocHolder) holder).docFullName.setText(dr.concat(instantDoc.getDocFirstName()).
//                    concat(" ").concat(instantDoc.getDocLastName()));
        }

    }

    @Override
    public int getItemCount() {
        if (docAppointList.isEmpty()){
            return 1;
        }
        return docAppointList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return docAppointList.isEmpty() ? 0 : 1;
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder{
        TextView fullNamePatTxt, appointDateTxt;
//        Button infoAppointBtn;
        MaterialCardView docCardView;
//        RecyclerView patInfoAppointRecycler;
        boolean isClicked = false;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            fullNamePatTxt = itemView.findViewById(R.id.cardItemDocName);
            appointDateTxt = itemView.findViewById(R.id.cardItemDateDocTxt);
//            infoAppointBtn = itemView.findViewById(R.id.appointCrslCrdItmPatInfolBtn);
            //patInfoAppointRecycler = itemView.findViewById(R.id.infoAppointPatRecycler);
            docCardView = itemView.findViewById(R.id.carouselItemDocAppointments);
        }

        public void bind(Appointments appoint, OnInfoBtnListener btnListener, int position) {
            if (selectedPos != position) {
                // Change back to the original "sand" color
                docCardView.setCardBackgroundColor(
                        ContextCompat.getColor(docCardView.getContext(), R.color.sand)
                );
            } else {
                // Change to "grey" color
                docCardView.setCardBackgroundColor(
                        ContextCompat.getColor(docCardView.getContext(), R.color.grey)
                );
            }

            docCardView.setOnClickListener(new View.OnClickListener() {
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

    public class EmptyStateDocHolder extends RecyclerView.ViewHolder{
        TextView docFullName;

        public EmptyStateDocHolder(@NonNull View itemView) {
            super(itemView);
            docFullName = itemView.findViewById(R.id.emptyStateDocFullNameTxt);
        }
    }
}

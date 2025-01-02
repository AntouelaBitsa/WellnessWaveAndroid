package com.wellnesswaveandroid.wellnesswaveandroid.Utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wellnesswaveandroid.wellnesswaveandroid.Activities.BookAppointmentActivity;
import com.wellnesswaveandroid.wellnesswaveandroid.Activities.RescheduleAppointment;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Appointments;
import com.wellnesswaveandroid.wellnesswaveandroid.R;

import java.util.List;

public class DocManageAppointmentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Appointments> appointmentsList;
    private Context context;
    private OnBtnListener cancelListener;
//    private int selectedPos = RecyclerView.NO_POSITION;

    public  interface OnBtnListener {
        void onCancelButtonClick(Appointments appoint);
//        void onRescheduleButtonClick(Appointments appoint);
    }

    public DocManageAppointmentsAdapter(List<Appointments> appointmentsList, Context context, OnBtnListener onCancelListener) {
        this.appointmentsList = appointmentsList;
        this.context = context;
        this.cancelListener = onCancelListener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View manageView = LayoutInflater.from(context).inflate(R.layout.manage_appointments_item, parent, false);
        return new ManageViewHolder(manageView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position < appointmentsList.size()){
            Log.d("ManageAppointmentAdapter", "Size of appointmentList: " + appointmentsList.size());
            Log.d("ManageAppointmentAdapter", "onBindViewHolder: position = " + position);
            Appointments appoint = appointmentsList.get(position);
            String fullName = appoint.getPatient().getPatFirstName().concat(" ").concat(appoint.getPatient().getPatLastName());
            System.out.println("[-1-onBindViewHolder() Manage]: fullName= " + fullName);
            ((ManageViewHolder) holder).fullNamePatTxt.setText(fullName);
            //DONE: add data to all other components of the CardView
            ((ManageViewHolder) holder).appointTimeTxt.setText(appoint.getTime());
            //DONE: implement onClick Listeners for Image View's
            ((ManageViewHolder) holder).bind(appoint, cancelListener, position);
            ((ManageViewHolder) holder).reascheduleAppoint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goToRescheduleAppointment = new Intent(context, RescheduleAppointment.class);
                    goToRescheduleAppointment.putExtra("appoint", appoint);
                    context.startActivity(goToRescheduleAppointment);
                }
            });
        }else {
            Log.e("DocManageAppoint", "Invalid position: " + position + ", List size: " + appointmentsList.size());
            Toast toast = Toast.makeText(context, "This was all the Specializations!", Toast.LENGTH_SHORT);
            return;
        }
    }

    @Override
    public int getItemCount() {
        if (appointmentsList.isEmpty()){
            return 0;
        }
        return appointmentsList.size();
    }

    public class ManageViewHolder extends RecyclerView.ViewHolder{
        TextView fullNamePatTxt, appointTimeTxt;
        ImageView cancelAppoint, reascheduleAppoint;

        public ManageViewHolder(@NonNull View itemView) {
            super(itemView);
            fullNamePatTxt = itemView.findViewById(R.id.patfullNmTxt);
//            amkaPatTxt = itemView.findViewById(R.id.manageCrdItmPatAmka);
            appointTimeTxt = itemView.findViewById(R.id.appointHourTxt);
            reascheduleAppoint = itemView.findViewById(R.id.rescheduleAppointImgView);
            cancelAppoint = itemView.findViewById(R.id.cancelAppointImgView);
        }

        public void bind(Appointments appoint, OnBtnListener cancelListener, int position) {
            cancelAppoint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cancelListener != null){
                        cancelListener.onCancelButtonClick(appoint);
                    }
                }
            });
        }
    }
}

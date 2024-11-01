package com.wellnesswaveandroid.wellnesswaveandroid.Utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Appointments;
import com.wellnesswaveandroid.wellnesswaveandroid.R;

import java.util.List;

public class DocManageAppointmentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Appointments> appointmentsList;
    private Context context;

    public DocManageAppointmentsAdapter(List<Appointments> appointmentsList, Context context) {
        this.appointmentsList = appointmentsList;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View manageView = LayoutInflater.from(context).inflate(R.layout.doc_manage_appointment_items, parent, false);
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
            ((ManageViewHolder) holder).amkaPatTxt.setText(appoint.getPatient().getPatSecuredNum());
            ((ManageViewHolder) holder).datePatTxt.setText(appoint.getDate());
            ((ManageViewHolder) holder).timePatTxt.setText(appoint.getTime());
            ((ManageViewHolder) holder).commentsPatTxt.setText(appoint.getAppointInfo());
            ((ManageViewHolder) holder).cancelAppointBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /**
                     * TODO: must navigate to another screen -> manage appointments screen
                     * **/
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
        TextView fullNamePatTxt, amkaPatTxt, datePatTxt, timePatTxt, commentsPatTxt;
        Button cancelAppointBtn;

        public ManageViewHolder(@NonNull View itemView) {
            super(itemView);
            fullNamePatTxt = itemView.findViewById(R.id.manageCrdItmPatFullNameTxt);
            amkaPatTxt = itemView.findViewById(R.id.manageCrdItmPatAmka);
            datePatTxt = itemView.findViewById(R.id.manageCrdItmPatAppntDate);
            timePatTxt = itemView.findViewById(R.id.manageCrdItmPatAppntTime);
            commentsPatTxt = itemView.findViewById(R.id.manageCrdItmPatCommnts);
            cancelAppointBtn = itemView.findViewById(R.id.appointCrslCrdItmPatInfolBtn);
        }
    }
}

package com.wellnesswaveandroid.wellnesswaveandroid.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Appointments;
import com.wellnesswaveandroid.wellnesswaveandroid.R;

import java.util.List;

public class PatInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Appointments liveAppoint;
    private Context context;

    public PatInfoAdapter(Appointments appointment, Context context) {
        this.liveAppoint = appointment;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View patInfoView = LayoutInflater.from(context).inflate(R.layout.info_pat_single_item, parent, false);
        return new PatInfoViewHolder(patInfoView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        System.out.println("[PATIENT-INFO-ADAPTER] SOS TEST PRINT APPOINTMENTS: "+ liveAppoint.toString());
        String fullName = liveAppoint.getPatient().getPatFirstName().concat(" ").concat(liveAppoint.getPatient().getPatLastName());
        System.out.println("[-1-onBindViewHolder() Manage]: fullName= " + fullName);
        ((PatInfoViewHolder) holder).fullNamePatTxt.setText(fullName);
        ((PatInfoViewHolder) holder).amkaPatTxt.setText(liveAppoint.getPatient().getPatSecuredNum());
        ((PatInfoViewHolder) holder).datePatTxt.setText(liveAppoint.getDate());
        ((PatInfoViewHolder) holder).timePatTxt.setText(liveAppoint.getTime());
        ((PatInfoViewHolder) holder).createDiagnosisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * TODO: must navigate to another screen -> create Diagnosis screen
                 * TODO: must send Patient ID
                 * **/
            }
        });
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public void setAppointment(Appointments appoint) {
        this.liveAppoint = appoint;
        notifyDataSetChanged();
    }

    public class PatInfoViewHolder extends RecyclerView.ViewHolder{
        TextView fullNamePatTxt, amkaPatTxt, datePatTxt, timePatTxt;
        Button createDiagnosisBtn;

        public PatInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            fullNamePatTxt = itemView.findViewById(R.id.crdItmPatFullNameTxt);
            amkaPatTxt = itemView.findViewById(R.id.crdItmPatAmka);
            datePatTxt = itemView.findViewById(R.id.crdItmPatAppntDate);
            timePatTxt = itemView.findViewById(R.id.crdItmPatAppntTime);
            createDiagnosisBtn = itemView.findViewById(R.id.crdItmPatDiagnosisBtn);
        }
    }

}

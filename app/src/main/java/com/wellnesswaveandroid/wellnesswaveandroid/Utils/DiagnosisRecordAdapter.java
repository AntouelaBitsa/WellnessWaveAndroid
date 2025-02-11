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
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Diagnosis;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Doctor;
import com.wellnesswaveandroid.wellnesswaveandroid.R;

import java.util.List;

public class DiagnosisRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Diagnosis> diagnosisList;
    private Context context;
    private OnBtnListener diagnosisListener;


    //DONE: Implement Listener for btnOnClick -> check methodology in Manage appointments activity (for PopUp)
    public  interface OnBtnListener {
        void onDiagnosisButtonClick(Diagnosis diagnosis);
    }

    public DiagnosisRecordAdapter(List<Diagnosis> diagnosisList, Context context, OnBtnListener onDiagnosisListener) {
        this.diagnosisList = diagnosisList;
        this.context = context;
        this.diagnosisListener = onDiagnosisListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View diagnosisView = LayoutInflater.from(context).inflate(R.layout.diagnosis_record_item, parent, false);
        return new DiagnosisHistoryViewHolder(diagnosisView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position >= diagnosisList.size()){
            Log.e("CarouselAdapter", "Invalid position: " + position + ", List size: " + diagnosisList.size());
            Toast toast = Toast.makeText(context, "This was all the Specializations!", Toast.LENGTH_SHORT);
            return;
        }
        Diagnosis diagnosis = diagnosisList.get(position);
        Doctor docByDiagnosis = diagnosis.getDoctor();
        ((DiagnosisHistoryViewHolder) holder).fullNameTxt.setText(
                docByDiagnosis.getDocFirstName().concat(" ").concat(docByDiagnosis.getDocLastName()));
        ((DiagnosisHistoryViewHolder) holder).specializationTxt.setText(docByDiagnosis.getDocProfession());
        ((DiagnosisHistoryViewHolder) holder).startDateTxt.setText(diagnosis.getStartDate());
        ((DiagnosisHistoryViewHolder) holder).endDateTxt.setText(diagnosis.getEndDate());
        ((DiagnosisHistoryViewHolder) holder).bind(diagnosis, diagnosisListener, position);
    }

    @Override
    public int getItemCount() {
        if (diagnosisList.isEmpty()){
            return 0;
        }
        return diagnosisList.size();
    }

    //For diagnosis history recycler view
    public  class DiagnosisHistoryViewHolder extends RecyclerView.ViewHolder{
        TextView fullNameTxt, specializationTxt, startDateTxt, endDateTxt;
        Button diagnosisPopUpBtn;


        public DiagnosisHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            fullNameTxt = itemView.findViewById(R.id.docDiagnosedFullName);
            specializationTxt = itemView.findViewById(R.id.docSpecializationTxt);
            startDateTxt = itemView.findViewById(R.id.diagnStartDateTxt);
            endDateTxt = itemView.findViewById(R.id.diagnEndDateTxt);
            diagnosisPopUpBtn = itemView.findViewById(R.id.diagnRecyclrBtn);
        }

        public void bind(Diagnosis diagnosis, OnBtnListener diagnosisListener, int position) {
            diagnosisPopUpBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (diagnosisListener != null){
                        diagnosisListener.onDiagnosisButtonClick(diagnosis);
                    }
                }
            });
        }
    }
}

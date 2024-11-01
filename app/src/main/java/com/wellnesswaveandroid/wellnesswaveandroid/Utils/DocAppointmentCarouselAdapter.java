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
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Patient;
import com.wellnesswaveandroid.wellnesswaveandroid.R;

import java.util.List;

public class DocAppointmentCarouselAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Appointments> docAppointList;
    private Context context;

//    public  interface OnItemClickListener{
//        void onInfoButtonClick(int position);
//    }

    public DocAppointmentCarouselAdapter(List<Appointments> docAppoint, Context context) {
        this.docAppointList = docAppoint;
        this.context = context;
//        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
            //TODO: change to get patients name
            String fullName = appoint.getPatient().getPatFirstName().concat(" ").concat(appoint.getPatient().getPatLastName());
            System.out.println("[-1-onBindViewHolder()]: fullName= " + fullName);
            ((AppointmentViewHolder) holder).fullNamePatTxt.setText(fullName);
            ((AppointmentViewHolder) holder).appointDateTxt.setText(appoint.getDate());
            System.out.println();
            ((AppointmentViewHolder) holder).infoAppointBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /**
                     * TODO: must open the component below the carousel so i can be visible the patients
                     * TODO: data and change visibility
                     * **/

//                    if (patInfoAppointRecycler.getVisibility() == View.GONE){
//                        patInfoAppointRecycler.setVisibility(View.VISIBLE);
//                    }else{
//                        patInfoAppointRecycler.setVisibility(View.GONE);
//                    }
//                    patInfoAdapter.setAppointment(appoint);
//                    adjustManageAppointRecyclerLayout();
                }
            });

        }else {
            Log.e("DocCarouselAdapter", "Invalid position: " + position + ", List size: " + docAppointList.size());
            Toast toast = Toast.makeText(context, "This was all the Specializations!", Toast.LENGTH_SHORT);
            return;
        }

    }

//    private void adjustManageAppointRecyclerLayout() {
//        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) manageAppointRecycler.getLayoutParams();
//        if (patInfoAppointRecycler.getVisibility()== View.VISIBLE){
//            params.topMargin = 50;
//        }else {
//            params.topMargin = 28;
//        }
//        manageAppointRecycler.setLayoutParams(params);
//    }

    @Override
    public int getItemCount() {
        if (docAppointList.isEmpty()){
            return 0;
        }
        return docAppointList.size();
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder{
        TextView fullNamePatTxt, appointDateTxt;
        Button infoAppointBtn;
        RecyclerView patInfoAppointRecycler;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            fullNamePatTxt = itemView.findViewById(R.id.cardItemDocName);
            appointDateTxt = itemView.findViewById(R.id.cardItemDateDocTxt);
            infoAppointBtn = itemView.findViewById(R.id.appointCrslCrdItmPatInfolBtn);
            //patInfoAppointRecycler = itemView.findViewById(R.id.infoAppointPatRecycler);
        }
    }
}

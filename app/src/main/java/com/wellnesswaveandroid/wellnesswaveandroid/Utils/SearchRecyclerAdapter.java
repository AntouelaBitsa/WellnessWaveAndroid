package com.wellnesswaveandroid.wellnesswaveandroid.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wellnesswaveandroid.wellnesswaveandroid.Activities.InsertDiagnosisActivity;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Patient;
import com.wellnesswaveandroid.wellnesswaveandroid.R;

import java.util.ArrayList;
import java.util.List;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.SearchViewHolder> {

    //Here we'll create a dynamic data recycler view
    Context context;
    ArrayList<Patient> patientArrayList;
    LayoutInflater layoutInflater;
    OnItemClickListener onItemClickListener;

    public SearchRecyclerAdapter(InsertDiagnosisActivity context, ArrayList<Patient> patientList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.patientArrayList = patientList;
        layoutInflater = LayoutInflater.from(context);
        this.onItemClickListener = onItemClickListener;
    }

    //TODO: implemntation of onItemClickListener()
    public interface OnItemClickListener{
        void onItemClick(Patient patient);
    }

    public void setFilteredList(ArrayList<Patient> filteredPatList){
        this.patientArrayList = filteredPatList;
        notifyDataSetChanged();
        System.out.println("* ADAPTER 3 * -> " + filteredPatList);
    }

    public SearchRecyclerAdapter(Context context, ArrayList<Patient> patientArrayList) {
        this.context = context;
        this.patientArrayList = patientArrayList;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recycler_pat_item, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        Patient currentPatient = patientArrayList.get(position);
        //DONE: concat the first and last name
        System.out.println("--------------------------////---------------------");
        System.out.println(">[SrchRcclrAdptr 01] onBindViewHolder(): before getting data from currentPatient " + currentPatient);
        String fName = currentPatient.getPatFirstName();
        String lName = currentPatient.getPatLastName();
        String amka = currentPatient.getPatSecuredNum();
        //TODO: get also the patients id ... (soon)
        System.out.println(">[SrchRcclrAdptr 02] onBindViewHolder(): after getting data from currentPatient " +
                fName + " " + lName + " " + amka);

        String fullName = fName.concat(" ").concat(lName);
        System.out.println(">[SrchRcclrAdptr 03] onBindViewHolder(): after fullName concatenation");
        System.out.println(">[SrchRcclrAdptr 04] onBindViewHolder(): before setting data to item activity");
        holder.patFullNameTxt.setText(fullName);
        holder.patAmkaTxt.setText(amka);

        //This is for string array -> Static data
        //I have dynamic data from API so i want to use ArrayList
//        holder.patFullNameTxt.setText(currentPatient.getPatFirstName());
//        holder.patAmkaTxt.setText(currentPatient.getPatSecuredNum());

        //DONE: implemntation of onClickListener() on the item View
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(">[SrchRcclrAdptr 05] holder.itemView.setOnClickListener(): inside onClick()");
                if (onItemClickListener != null){
                    System.out.println(">[SrchRcclrAdptr 06] holder.itemView.setOnClickListener(): check for onItemClickListener value null/not?");
                    onItemClickListener.onItemClick(currentPatient);
                }
            }
        });
        System.out.println(">[SrchRcclrAdptr 07] onBindViewHolder(): after on click");
        System.out.println("--------------------------////---------------------");
    }

    @Override
    public int getItemCount() {
        return patientArrayList.size();
    }

    //TODO: updateList for dynamic data population
    public void updatePatList(ArrayList<Patient> newPatList){
        //1st way
        patientArrayList.clear();
        patientArrayList.addAll(newPatList);
        System.out.println("--------------------------////---------------------");
        System.out.println(">[SrchRcclrAdptr 01] updatePatList(): patientArrayList= " + patientArrayList);
        System.out.println("--------------------------////---------------------");
        notifyDataSetChanged();

        //2nd way -> same as before
//        this.patientArrayList = newPatList;
//        notifyDataSetChanged();
    }


    public class SearchViewHolder extends RecyclerView.ViewHolder{
        TextView patFullNameTxt, patAmkaTxt;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            patFullNameTxt = itemView.findViewById(R.id.fullNameTxtRclr);
            patAmkaTxt = itemView.findViewById(R.id.amkaTxtRclr);
        }
    }
}

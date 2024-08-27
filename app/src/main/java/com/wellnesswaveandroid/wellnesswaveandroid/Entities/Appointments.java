package com.wellnesswaveandroid.wellnesswaveandroid.Entities;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointments {
    @SerializedName("appointmentId")
    private Integer appointmentId;
    @SerializedName("date")
    private String date;
    @SerializedName("time")
    private String time;
    @SerializedName("appointInfo")
    private String appointInfo;
    @SerializedName("doctor")
    private Doctor doctor;
    @SerializedName("patient")
    private Patient patient;

    public Appointments() {
    }

    public Appointments(String date, String time, String appointInfo, Integer doctorId, Integer patientId) {
        this.date = date;
        this.time = time;
        this.appointInfo = appointInfo;
//        this.doctor = Doctor.getInstance();
        this.doctor.setDocId(doctorId);
        this.patient = Patient.getInstance();
        this.patient.setPatientId(patientId);
    }

    public Integer getDocIDApp(){
        return doctor.getDocId();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAppointInfo() {
        return appointInfo;
    }

    public void setAppointInfo(String appointInfo) {
        this.appointInfo = appointInfo;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    @Override
    public String toString() {
        return "Appointments{" +
                "appointmentId=" + appointmentId +
                ", date=" + date +
                ", time=" + time +
                ", appointInfo='" + appointInfo + '\'' +
                ", doctor=" + doctor +
                ", patient=" + patient +
                '}';
    }
}

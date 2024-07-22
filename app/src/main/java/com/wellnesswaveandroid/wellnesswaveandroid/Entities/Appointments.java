package com.wellnesswaveandroid.wellnesswaveandroid.Entities;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointments {
    @SerializedName("appointmentId")
    private Integer appointmentId;
    @SerializedName("date")
    private LocalDate date;
    @SerializedName("time")
    private LocalTime time;
    @SerializedName("appointInfo")
    private String appointInfo;
    @SerializedName("doctor")
    private Doctor doctor;
    @SerializedName("patient")
    private Patient patient;

    public Appointments() {
    }

    public Appointments(LocalDate date, LocalTime time, String appointInfo, Doctor doctor, Patient patient) {
        this.date = date;
        this.time = time;
        this.appointInfo = appointInfo;
        this.doctor = doctor;
        this.patient = patient;
    }

    public Integer getDocIDApp(){
        return doctor.getDocId();
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
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

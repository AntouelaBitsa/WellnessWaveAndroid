package com.wellnesswaveandroid.wellnesswaveandroid.Entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookedSlots {

    @SerializedName("slotId")
    private Integer slotId;
    @SerializedName("slotDate")
    private String slotDate;
    @SerializedName("slotTime")
    private String slotTime;
//    @SerializedName("appointments")
//    private Appointments appointments;
    @SerializedName("doctor")
    private Doctor doctor;

    public BookedSlots() {
    }


    public BookedSlots(String slotDate, String slotTime, Integer doctorId) {
        this.slotDate = slotDate;
        this.slotTime = slotTime;
        this.doctor = Doctor.getInstance();
        this.doctor.setDocId(doctorId);
    }

    public Integer getSlotId() {
        return slotId;
    }

    public void setSlotId(Integer slotId) {
        this.slotId = slotId;
    }

    public String getSlotDate() {
        return slotDate;
    }

    public void setSlotDate(String slotDate) {
        this.slotDate = slotDate;
    }

    public String getSlotTime() {
        return slotTime;
    }

    public void setSlotTime(String slotTime) {
        this.slotTime = slotTime;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    @Override
    public String toString() {
        return "BookedSlots{" +
                "slotId=" + slotId +
                ", slotDate='" + slotDate + '\'' +
                ", slotTime='" + slotTime + '\'' +
                ", doctor=" + doctor +
                '}';
    }

    //    public Appointments getAppointments() {
//        return appointments;
//    }
//
//    public void setAppointments(Appointments appointments) {
//        this.appointments = appointments;
//    }

}

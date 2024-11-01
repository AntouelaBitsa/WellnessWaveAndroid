package com.wellnesswaveandroid.wellnesswaveandroid.Entities;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;

public class Diagnosis {
    @SerializedName("diagnosisId")
    private Integer diagnosisId;
    @SerializedName("diagnType")
    private String diagnType;
    @SerializedName("treatment")
    private String treatment;
    @SerializedName("treatmDose")
    private String treatmDose;
    @SerializedName("startDate")
    private String startDate;
    @SerializedName("endDate")
    private String endDate;
    @SerializedName("diagnInfo")
    private String diagnInfo;
    @SerializedName("doctor")
    private Doctor doctor;
    @SerializedName("patient")
    private Patient patient;

    public Diagnosis() {
    }

    public Diagnosis(String diagnType, String treatment, String treatmDose, String startDate, String endDate, String diagnInfo, Integer doctorID, Integer patientID) {
        this.diagnType = diagnType;
        this.treatment = treatment;
        this.treatmDose = treatmDose;
        this.startDate = startDate;
        this.endDate = endDate;
        this.diagnInfo = diagnInfo;
        this.doctor = Doctor.getInstance();
        this.doctor.setDocId(doctorID);
        this.patient = Patient.getInstance();
        this.patient.setPatientId(patientID);
    }

    public Integer getDiagnosisId() {
        return diagnosisId;
    }

    public void setDiagnosisId(Integer diagnosisId) {
        this.diagnosisId = diagnosisId;
    }

    public String getDiagnInfo() {
        return diagnInfo;
    }

    public void setDiagnInfo(String diagnInfo) {
        this.diagnInfo = diagnInfo;
    }

    public String getDiagnType() {
        return diagnType;
    }

    public void setDiagnType(String diagnType) {
        this.diagnType = diagnType;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getTreatmDose() {
        return treatmDose;
    }

    public void setTreatmDose(String treatmDose) {
        this.treatmDose = treatmDose;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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
        return "Diagnosis{" +
                "diagnosisId=" + diagnosisId +
                ", diagnType='" + diagnType + '\'' +
                ", treatment='" + treatment + '\'' +
                ", treatmDose='" + treatmDose + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", diagnInfo='" + diagnInfo + '\'' +
                ", doctor=" + doctor +
                ", patient=" + patient +
                '}';
    }
}

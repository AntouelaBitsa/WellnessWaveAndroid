package com.wellnesswaveandroid.wellnesswaveandroid.Entities;

import com.google.gson.annotations.SerializedName;

public class Patient {

    @SerializedName("patientId")
    private Integer patientId;
    @SerializedName("firstName")
    private String patFirstName;
    @SerializedName("lastName")
    private String patLastName;
    @SerializedName("patUsername")
    private String patUsername;
    @SerializedName("patPassword")
    private String patPassword;
    @SerializedName("email")
    private String patEmail;
    @SerializedName("phoneNum")
    private String patPhoneNum;
    @SerializedName("patAmka")
    private String patSecuredNum;
    @SerializedName("dob")
    private String patDob;
    private int userType;

    public Patient() {
    }

    public Patient(String patFirstName, String patLastName, String patUsername, String patPassword, String patEmail, String patPhoneNum, String patSecuredNum, String patDob, int userType) {
        this.patFirstName = patFirstName;
        this.patLastName = patLastName;
        this.patUsername = patUsername;
        this.patPassword = patPassword;
        this.patEmail = patEmail;
        this.patPhoneNum = patPhoneNum;
        this.patSecuredNum = patSecuredNum;
        this.patDob = patDob;
        this.userType = userType;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getPatFirstName() {
        return patFirstName;
    }

    public void setPatFirstName(String patFirstName) {
        this.patFirstName = patFirstName;
    }

    public String getPatLastName() {
        return patLastName;
    }

    public void setPatLastName(String patLastName) {
        this.patLastName = patLastName;
    }

    public String getPatUsername() {
        return patUsername;
    }

    public void setPatUsername(String patUsername) {
        this.patUsername = patUsername;
    }

    public String getPatPassword() {
        return patPassword;
    }

    public void setPatPassword(String patPassword) {
        this.patPassword = patPassword;
    }

    public String getPatEmail() {
        return patEmail;
    }

    public void setPatEmail(String patEmail) {
        this.patEmail = patEmail;
    }

    public String getPatPhoneNum() {
        return patPhoneNum;
    }

    public void setPatPhoneNum(String patPhoneNum) {
        this.patPhoneNum = patPhoneNum;
    }

    public String getPatSecuredNum() {
        return patSecuredNum;
    }

    public void setPatSecuredNum(String patSecuredNum) {
        this.patSecuredNum = patSecuredNum;
    }

    public String getPatDob() {
        return patDob;
    }

    public void setPatDob(String patDob) {
        this.patDob = patDob;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}

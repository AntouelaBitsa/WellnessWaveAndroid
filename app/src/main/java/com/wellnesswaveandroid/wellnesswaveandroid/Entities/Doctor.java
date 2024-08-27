package com.wellnesswaveandroid.wellnesswaveandroid.Entities;

import com.google.gson.annotations.SerializedName;

public class Doctor {
    private static Doctor docInstance;

    @SerializedName("docId")
    private Integer docId;
    @SerializedName("firstName")
    private String docFirstName;
    @SerializedName("lastName")
    private String docLastName;
    @SerializedName("docUsername")
    private String docUsername;
    @SerializedName("password")
    private String docPassword;
    @SerializedName("email")
    private String docEmail;
    @SerializedName("amka")
    private String docSecuredNum;
    @SerializedName("phoneNum")
    private String docPhoneNum;
    @SerializedName("profession")
    private String docProfession;
    @SerializedName("address")
    private String docAddress;
    @SerializedName("userType")
    private int userType;

    //Transfer patient data between activities
    public static synchronized Doctor getInstance(){
        if (docInstance == null){
            docInstance = new Doctor();
        }
        return docInstance;
    }

    //Transfer patient data between activities
    public void setDoctorData(Doctor d){
        this.docId = d.getDocId();
        this.docFirstName = d.getDocFirstName();
        this.docLastName = d.getDocLastName();
        this.docUsername = d.getDocUsername();
        this.docEmail = d.getDocEmail();
        this.docPhoneNum = d.getDocPhoneNum();
        this.docProfession = d.getDocProfession();
        this.docAddress = d.getDocAddress();
        this.userType = d.getUserType();
    }

    private Doctor() {
    }

    //Constructor accessed by Doctor.getInstance().clone() -> creates a new empty Doctor
    public Doctor clone(){
        return new Doctor();
    }

    public Doctor(String docFirstName, String docLastName, String docUsername, String docPassword, String docEmail, String docSecuredNum, String docPhoneNum, String docProfession, String docAddress, int userType) {
        this.docFirstName = docFirstName;
        this.docLastName = docLastName;
        this.docUsername = docUsername;
        this.docPassword = docPassword;
        this.docEmail = docEmail;
        this.docSecuredNum = docSecuredNum;
        this.docPhoneNum = docPhoneNum;
        this.docProfession = docProfession;
        this.docAddress = docAddress;
        this.userType = 1;
    }

    public Doctor(Integer docId, String docFirstName, String docLastName, String docUsername, String docEmail, String docPhoneNum, String docProfession, String docAddress, int userType) {
        this.docId = docId;
        this.docFirstName = docFirstName;
        this.docLastName = docLastName;
        this.docUsername = docUsername;
        this.docEmail = docEmail;
        this.docPhoneNum = docPhoneNum;
        this.docProfession = docProfession;
        this.docAddress = docAddress;
        this.userType = userType;
    }

    public Doctor(Integer idDoc) {
        this.docId = idDoc;
    }

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

    public String getDocFirstName() {
        return docFirstName;
    }

    public void setDocFirstName(String docFirstName) {
        this.docFirstName = docFirstName;
    }

    public String getDocLastName() {
        return docLastName;
    }

    public void setDocLastName(String docLastName) {
        this.docLastName = docLastName;
    }

    public String getDocUsername() {
        return docUsername;
    }

    public void setDocUsername(String docUsername) {
        this.docUsername = docUsername;
    }

    public String getDocPassword() {
        return docPassword;
    }

    public void setDocPassword(String docPassword) {
        this.docPassword = docPassword;
    }

    public String getDocEmail() {
        return docEmail;
    }

    public void setDocEmail(String docEmail) {
        this.docEmail = docEmail;
    }

    public String getDocSecuredNum() {
        return docSecuredNum;
    }

    public void setDocSecuredNum(String docSecuredNum) {
        this.docSecuredNum = docSecuredNum;
    }

    public String getDocPhoneNum() {
        return docPhoneNum;
    }

    public void setDocPhoneNum(String docPhoneNum) {
        this.docPhoneNum = docPhoneNum;
    }

    public String getDocProfession() {
        return docProfession;
    }

    public void setDocProfession(String docProfession) {
        this.docProfession = docProfession;
    }

    public String getDocAddress() {
        return docAddress;
    }

    public void setDocAddress(String docAddress) {
        this.docAddress = docAddress;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "docId=" + docId +
                ", docFisrtName='" + docFirstName + '\'' +
                ", docLastName='" + docLastName + '\'' +
                ", docUsername='" + docUsername + '\'' +
                ", docPassword='" + docPassword + '\'' +
                ", docEmail='" + docEmail + '\'' +
                ", docAmka='" + docSecuredNum + '\'' +
                ", docPhoneNum='" + docPhoneNum + '\'' +
                ", docProfession='" + docProfession + '\'' +
                ", docAddress='" + docAddress + '\'' +
                '}';
    }
}

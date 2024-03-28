package com.wellnesswaveandroid.wellnesswaveandroid.Entities;

public class Doctor {

    private Integer docId;
    private String docFisrtName;
    private String docLastName;
    private String docUsername;
    private String docPassword;
    private String docEmail;
    private String docAmka;
    private String docPhoneNum;
    private String docProfession;
    private String docAddress;

    public Doctor(String docFisrtName, String docLastName, String docUsername, String docPassword, String docEmail, String docAmka, String docPhoneNum, String docProfession, String docAddress) {
        this.docFisrtName = docFisrtName;
        this.docLastName = docLastName;
        this.docUsername = docUsername;
        this.docPassword = docPassword;
        this.docEmail = docEmail;
        this.docAmka = docAmka;
        this.docPhoneNum = docPhoneNum;
        this.docProfession = docProfession;
        this.docAddress = docAddress;
    }

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

    public String getDocFisrtName() {
        return docFisrtName;
    }

    public void setDocFisrtName(String docFisrtName) {
        this.docFisrtName = docFisrtName;
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

    public String getDocAmka() {
        return docAmka;
    }

    public void setDocAmka(String docAmka) {
        this.docAmka = docAmka;
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

    @Override
    public String toString() {
        return "Doctor{" +
                "docId=" + docId +
                ", docFisrtName='" + docFisrtName + '\'' +
                ", docLastName='" + docLastName + '\'' +
                ", docUsername='" + docUsername + '\'' +
                ", docPassword='" + docPassword + '\'' +
                ", docEmail='" + docEmail + '\'' +
                ", docAmka='" + docAmka + '\'' +
                ", docPhoneNum='" + docPhoneNum + '\'' +
                ", docProfession='" + docProfession + '\'' +
                ", docAddress='" + docAddress + '\'' +
                '}';
    }
}

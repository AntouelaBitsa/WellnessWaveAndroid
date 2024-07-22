package com.wellnesswaveandroid.wellnesswaveandroid.Utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Doctor;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Result {
    private int status;
    private String message;
    private int userType;
//    private List<Doctor> specialisedDoc;
//    public Result(int status, List<Doctor> doctorList) {
//        this.status = status;
//        this.specialisedDoc = doctorList;
//    }

    // Getters and setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

//    public List<Doctor> getSpecialisedDoc() {
//        return specialisedDoc;
//    }
//
//    public void setSpecialisedDoc(List<Doctor> specialisedDoc) {
//        this.specialisedDoc = specialisedDoc;
//    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + status +
                ", sessionJson='" + message + '\'' +
                '}';
    }


}

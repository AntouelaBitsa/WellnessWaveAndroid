package com.wellnesswaveandroid.wellnesswaveandroid.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Doctor;

public class SplitJSONImpl {
    ObjectMapper objectMapper = new ObjectMapper();
    int userType;

    public SplitJSONImpl() {
    }
    //TODO : based on the user type will be called each method separately
    //TODO : add the same names as the entity class to match
    public int extractUserType(String initialJSON){
        try{
            Result res = objectMapper.readValue(initialJSON, Result.class);

            //Extracting user type
            userType = res.getUserType();

            System.out.println("TEST >>> : SplitJSONImpl " + userType);

        }catch(Exception e){
            e.printStackTrace();
        }
        return userType;
    }

    //TODO : split JSON for Doctor
    public Doctor extractDocFromJson(String userJSON) {
        Doctor completedDoc = new Doctor();
        try{
            Doctor doc = objectMapper.readValue(userJSON, Doctor.class);

            //Extracting individual Fields
            Integer docId = doc.getDocId();
            String docFirstName = doc.getDocFirstName();
            String docLastName = doc.getDocLastName();
            String docUsername = doc.getDocUsername();
            String docEmail = doc.getDocEmail();
            String docPhoneNum = doc.getDocPhoneNum();
            String docProfession = doc.getDocProfession();
            String docAddress = doc.getDocAddress();
            userType = doc.getUserType();

            completedDoc = new Doctor(docId, docFirstName, docLastName, docUsername, docEmail, docPhoneNum, docProfession, docAddress, userType);
            System.out.println("Doctor after LOGIN :  " + completedDoc.toString());

        }catch(Exception e){
            e.printStackTrace();
        }
        return completedDoc;
    }

    //TODO : split JSON for Patient
//    public Patient extractPatFromJson(String userJSON) {
//    }



}

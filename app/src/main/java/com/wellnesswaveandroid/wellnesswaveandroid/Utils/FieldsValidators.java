package com.wellnesswaveandroid.wellnesswaveandroid.Utils;

import android.util.Log;

import java.util.regex.Pattern;

public class FieldsValidators {

    public FieldsValidators() {
    }

    public boolean validateEmptyDoc(String fname, String lname, String username, String password, String email, String amka, String phone, String address){
        return fname.isEmpty() || lname.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty() || amka.isEmpty()
                || phone.isEmpty() || address.isEmpty();
    }

    public boolean validateEmptyPat(String fname, String lname, String username, String password, String email, String amka, String phone, String dob){
        return fname.isEmpty() || lname.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty() || amka.isEmpty()
                || phone.isEmpty() || dob.isEmpty();
    }

    public boolean validateEmptyDiagnosis(String diagnosis, String info){
        return diagnosis.isEmpty() || info.isEmpty();
    }

    public boolean validateEmptyTreatment(String treatment, String dose){
        return treatment.isEmpty() || dose.isEmpty();
    }

    public boolean validateRegex(String password, String email, String phone, String amka){
        Log.d("InputCheck", "Password: " + password);
        Log.d("InputCheck", "Email: " + email);
        Log.d("InputCheck", "AMKA: " + amka);
        Log.d("InputCheck", "Phone: " + phone);

        String passRegex = "^(?=.*[0-9])"            //a digit must occur at least once
                + "(?=.*[a-z])(?=.*[A-Z])"      //a lower case alphabet must occur at least once & an upper case alphabet that must occur at least once
                + "(?=.*[@!^&_])"               //a special character that must occur at least once
                + "(?=\\S+$).{8,15}$";          //white spaces aren’t allowed & at least 8 characters and at most 15 characters
        Pattern passPattern = Pattern.compile(passRegex);

        String emailRegex = "^[a-zA-Z0-9.%+-]"
                + "+@[a-zA-Z0-9.-]"
                + "+\\.[a-zA-Z]{2,}$";
        Pattern emailPattern = Pattern.compile(emailRegex);

//        String phoneNumber = "^\\+?[1-9][0-9]{9,14}$";
//        String phoneNumber = "^(\\(?\\d{3}\\)?[-.\\s]?)?\\d{3}[-.\\s]?\\d{4}$";
        String phoneNumber = "^[0-9]{10}$";
        Pattern phonePattern = Pattern.compile(phoneNumber);

        String securedNum = "^[0-9]{11}$";
        Pattern securedNumPattern = Pattern.compile(securedNum);

        if (!passPattern.matcher(password).matches()) {
            Log.d("Debug PASSWORD", "REGEX: password = " +  !passPattern.matcher(password).matches() );
            return true;
        }

        if (!emailPattern.matcher(email).matches()){
            Log.d("Debug EMAIL", "REGEX: email = " +  !emailPattern.matcher(email).matches() );
            return true;
        }

        if (!phonePattern.matcher(phone).matches()){
            Log.d("Debug PHONE", "REGEX: phone = " + !phonePattern.matcher(phone).matches() + " Phone length: " + phone.length());
            return true;
        }

        if (!securedNumPattern.matcher(amka).matches()){
            Log.d("Debug AMKA", "REGEX: amka = " +  !securedNumPattern.matcher(securedNum).matches());
            return true;
        }

        return false;
    }

    public boolean validateLength(String password, String phone, String amka){
        if ( !(password.length()>=8 && password.length()<=15) ){
            System.out.println("[ValUtilsClass] Password= " + !(password.length()>=8 && password.length()<=15));
            Log.d("Debug PASSWORD", "validateLength: password = " +  !(password.length()>=8 && password.length()<=15) + "pass length: " + password.length());
            return true;
        }
        if (amka.length()<10 || amka.length()>12) {
            System.out.println("[ValUtilsClass] " + " AMKA= " + !(amka.length()==11));
            Log.d("Debug AMKA", "validateLength: amka = " +  !(amka.length()==11) + "amka length: " + amka.length());
            return true;
        }
        if (phone.length()<9 || phone.length()>11) {
            System.out.println("[ValUtilsClass] " + " Phone= " + !(phone.length()==10));
            Log.d("Debug AMKA", "validateLength: phone = " +  !(phone.length()==10) + "phone length: " + phone.length());
            return true;
        }

        return false;
    }

    public boolean validateEmptyLogIn(String username, String password) {
        return username.isEmpty() || password.isEmpty();
    }

    public boolean validateRegexLogIn(String password) {
        String passRegex = "^(?=.*[0-9])"            //a digit must occur at least once
                + "(?=.*[a-z])(?=.*[A-Z])"      //a lower case alphabet must occur at least once & an upper case alphabet that must occur at least once
                + "(?=.*[@!^&_])"               //a special character that must occur at least once
                + "(?=\\S+$).{8,15}$";          //white spaces aren’t allowed & at least 8 characters and at most 15 characters
        Pattern passPattern = Pattern.compile(passRegex);

        if (!passPattern.matcher(password).matches()) {
            Log.d("Debug PASSWORD", "REGEX: password = " +  !passPattern.matcher(password).matches() );
            return true;
        }

        return false;
    }

    public boolean validateLengthLogIn(String password) {
        if ( !(password.length()>=8 && password.length()<=15) ){
            System.out.println("[ValUtilsClass] Password= " + !(password.length()>=8 && password.length()<=15));
            Log.d("Debug PASSWORD", "validateLength: password = " +  !(password.length()>=8 && password.length()<=15) + "pass length: " + password.length());
            return true;
        }
        return false;
    }

    public String validatePassword(String incomingPass){
        //Password validation characters and rules
        String passRegex = "^(?=.*[0-9])"            //a digit must occur at least once
                + "(?=.*[a-z])(?=.*[A-Z])"      //a lower case alphabet must occur at least once & an upper case alphabet that must occur at least once
                + "(?=.*[@!^&_])"               //a special character that must occur at least once
                + "(?=\\S+$).{8,15}$";          //white spaces aren’t allowed & at least 8 characters and at most 15 characters
        Pattern passPattern = Pattern.compile(passRegex);

        if(incomingPass.isEmpty()){
            return "Password is required.";
        } else if (!passPattern.matcher(incomingPass).matches()) {
//            return "Password is too weak.";
            return "Password must have a-z, A-Z, 0-9, @!^&_,";
        } else if (incomingPass.length()<=8) {
//            return "Password length must be > 8.";
            return "Password must have at least 8 to 15 characters.";
        }else {
            return "";
        }
    }

    public String validateEmail(String incomingEmail){
        String email = "^[a-zA-Z0-9.%+-]"
                + "+@[a-zA-Z0-9.-]"
                + "+\\.[a-zA-Z]{2,}$";
        Pattern emailPattern = Pattern.compile(email);

        if(incomingEmail.isEmpty()){
            return "Email is required.";
        } else if (!emailPattern.matcher(incomingEmail).matches()) {
            return "Invalid email format. Please enter a valid email address.";
        } else {
            return "";
        }
    }

    public String validatePhoneNumber(String incomingPhoneNumber){
//        String phoneNumber = "^\\+?[1-9][0-9]{9,14}$";
//        String phoneNumber = "^(\\(?\\d{3}\\)?[-.\\s]?)?\\d{3}[-.\\s]?\\d{4}$";
        String phoneNumber = "^[0-9]{10}$";
        Pattern phonePattern = Pattern.compile(phoneNumber);

        if(incomingPhoneNumber.isEmpty()){
            return "Phone number is required.";
        } else if (!phonePattern.matcher(incomingPhoneNumber).matches()) {
            return "Invalid phone number format. Please enter a valid number.";
        } else {
            return "";
        }
    }

    public String validateAddress(String incomingAddress){
        if (incomingAddress.isEmpty()){
            return "Address is required.";
        }else {
            return "";
        }
    }

    public String validateFirstName(String incomingFirstName){
        if (incomingFirstName.isEmpty()){
            return "First Name is required.";
        }else {
            return "";
        }
    }

    public String validateLastName(String incomingLastName){
        if (incomingLastName.isEmpty()){
            return "Last Name is required.";
        }else {
            return "";
        }
    }

    public String validateUsername(String incomingUsername){
        if (incomingUsername.isEmpty()){
            return "Username is required.";
        }else {
            return "";
        }
    }

    public String validateAmka(String incomingAmka) {
        String securedNum = "^[0-9]{11}$";
        Pattern securedNumPattern = Pattern.compile(securedNum);

        if (incomingAmka.isEmpty()){
            return "Amka is required.";
        } else if (!securedNumPattern.matcher(incomingAmka).matches()) {
            return "Invalid AMKA format. Please enter a valid AMKA.";
        } else {
            return "";
        }
    }

    public String validateProfession(String incomingProfession) {
        if (incomingProfession.isEmpty()){
            return "Profession is required.";
        }else {
            return "";
        }
    }

    public String validateDob(String incomingDob) {
        if (incomingDob.isEmpty()){
            return "Birthdate is required.";
        }else {
            return "";
        }
    }

    public String validateEmpty(String incomingTxt) {
        if (incomingTxt.isEmpty()){
            return "Fill in the field";
        }else {
            return "";
        }
    }


}

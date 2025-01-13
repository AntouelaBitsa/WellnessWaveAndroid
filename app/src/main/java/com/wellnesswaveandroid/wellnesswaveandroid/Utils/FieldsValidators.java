package com.wellnesswaveandroid.wellnesswaveandroid.Utils;

import java.util.regex.Pattern;

public class FieldsValidators {

    public FieldsValidators() {
    }

    public boolean validateEmpty(String fname, String lname, String username, String password, String email, String amka, String phone, String profession, String address){
        return fname.isEmpty() && lname.isEmpty() && username.isEmpty() && password.isEmpty() && email.isEmpty() && amka.isEmpty()
                && phone.isEmpty() && profession.isEmpty() && address.isEmpty();
    }

    public boolean validateRegex(String password, String email, String amka, String phone){
        String passRegex = "^(?=.*[0-9])"            //a digit must occur at least once
                + "(?=.*[a-z])(?=.*[A-Z])"      //a lower case alphabet must occur at least once & an upper case alphabet that must occur at least once
                + "(?=.*[@!^&_])"               //a special character that must occur at least once
                + "(?=\\S+$).{8,15}$";          //white spaces aren’t allowed & at least 8 characters and at most 15 characters
        Pattern passPattern = Pattern.compile(passRegex);

        String emailRegex = "^[a-zA-Z0-9.%+-]"
                + "+@[a-zA-Z0-9.-]"
                + "+\\.[a-zA-Z]{2,}$";
        Pattern emailPattern = Pattern.compile(emailRegex);

        String phoneNumber = "^\\+[1-9\\d{1,10}$]";
        Pattern phonePattern = Pattern.compile(phoneNumber);

        String securedNum = "^[0-9]{11}$";
        Pattern securedNumPattern = Pattern.compile(securedNum);

        if (!passPattern.matcher(password).matches() || !emailPattern.matcher(email).matches() ||
                !phonePattern.matcher(phone).matches() || !securedNumPattern.matcher(amka).matches()) {
            return true;
        }

        return false;
    }

    public boolean validateLength(String password, String amka, String phone){
        if (!(password.length()>=8 && password.length()<=15) || !(amka.length()==11) || !(phone.length()==10) ){
            System.out.println("[ValUtilsClass] Password= " + !(password.length()>=8 && password.length()<=15) +
                    " AMKA= " + !(amka.length()==11) + " Phone= " + !(phone.length()==10));
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
        //TODO: "Email is already in use. Please use a different email."
    }

    public String validatePhoneNumber(String incomingPhoneNumber){
//        String phoneNumber = "^\\+[1-9\\d{1,10}$]";
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
}

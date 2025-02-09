package com.wellnesswaveandroid.wellnesswaveandroid.Utils;

import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Doctor;

public class LogInDTO {
    private static LogInDTO userInstance;
    private String username, password;
    private Integer userType;

    //Transfer user data between activities
    public static synchronized LogInDTO getInstance(){
        if (userInstance == null){
            userInstance = new LogInDTO();
        }
        return userInstance;
    }

    //Transfer user data between activities
    public void setDoctorData(LogInDTO user){
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.userType = user.getUserType();
    }

    public LogInDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    private LogInDTO() {
    }

    public LogInDTO(String username, String password, Integer userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }
}

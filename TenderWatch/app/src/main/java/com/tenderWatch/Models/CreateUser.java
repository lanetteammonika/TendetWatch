package com.tenderWatch.Models;


import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.util.List;

/**
 * Created by lcom48 on 11/12/17.
 */

public class CreateUser {

    /**
     * Created by lcom48 on 8/12/17.
     */

    static private File profilePhoto;

    static private String email;

    static  private String password;

    static   private String country;

    static   private String contactNo;

    static   private String occupation;
    static public String aboutMe;

    static   private String role;

    static   private String deviceId;


        public String getPassword() {
        return password;
    }

        public void setPassword(String password) {
        this.password = password;
    }

        public File getProfilePhoto() {
            return profilePhoto;
        }

        public void setProfilePhoto(File profilePhoto) {
            this.profilePhoto = profilePhoto;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getContactNo() {
            return contactNo;
        }

        public void setContactNo(String contactNo) {
            this.contactNo = contactNo;
        }

        public String getOccupation() {
            return occupation;
        }

        public void setOccupation(String occupation) {
            this.occupation = occupation;
        }

        public String getAboutMe() {
            return aboutMe;
        }

        public void setAboutMe(String aboutMe) {
            this.aboutMe = aboutMe;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }


        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }


    }



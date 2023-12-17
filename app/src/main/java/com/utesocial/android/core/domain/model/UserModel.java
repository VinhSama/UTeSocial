package com.utesocial.android.core.domain.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class UserModel {

    public enum UserType {
        CollegeStudent(1),
        Lecturer(2),
        Candidate(3);
        public final int userType;
        UserType(int userType) {
            this.userType = userType;
        }

    }
    @SerializedName("_id")
    public String userId;
    public String identityCode;
    public String firstName;
    public String lastName;
    public String email;
    public String username;
    public String hometown;
    public String birthdate;
    public Avatar avatar;
    public int status;
    public List<String> friends;
    public int friendCount;
    public UserType type;
    public Date createdAt;
    public Date updatedAt;
    public UserDetails details;

}

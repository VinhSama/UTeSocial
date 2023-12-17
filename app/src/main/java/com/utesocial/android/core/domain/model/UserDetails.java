package com.utesocial.android.core.domain.model;

public class UserDetails {
    public boolean graduated;
    public String classCode;
    public String faculty;
    public String major;
    public String enrollmentYear;

    public UserDetails(boolean graduated, String classCode, String faculty, String major, String enrollmentYear) {
        this.graduated = graduated;
        this.classCode = classCode;
        this.faculty = faculty;
        this.major = major;
        this.enrollmentYear = enrollmentYear;
    }
}

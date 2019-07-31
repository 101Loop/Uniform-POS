package com.tapatuniforms.pos.model;

public class Student {
    private String studentId;
    private String name;
    private String standard;
    private String section;
    private String fatherName;
    private String gender;
    private int school;
    private String email;
    private String mobile;

    public Student(String studentId, String name, String standard, String section, String fatherName, String gender, int school, String email, String mobile) {
        this.studentId = studentId;
        this.name = name;
        this.standard = standard;
        this.section = section;
        this.fatherName = fatherName;
        this.gender = gender;
        this.school = school;
        this.email = email;
        this.mobile = mobile;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getSchool() {
        return school;
    }

    public void setSchool(int school) {
        this.school = school;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}

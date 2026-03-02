package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name="student_clients")
public class StudentKimlik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "kimlik_number")
    private String kimlikNumber;

    @Column(name = "kimlik_start_date")
    private LocalDate kimlikStartDate;

    @Column(name = "kimlik_end_date")
    private LocalDate kimlikEndDate;

    @Column(name = "university")
    private String university;

    @Column(name = "department")
    private String department;

    @Column(name = "study_year_begin")
    private LocalDate universityStartDate;

    @Column(name = "study_year_end")
    private LocalDate universityEndDate;

    @Column(name = "notified_60_days")
    private boolean notified60Days;

    @Transient
    public long getDaysLeft(){
        if (getKimlikEndDate() == null){
            return 0;
        }
        return ChronoUnit.DAYS.between(LocalDate.now(), kimlikEndDate);
    }

    public StudentKimlik (){}

    public StudentKimlik(String firstName, String lastName, String email, String phoneNumber, String kimlikNumber, LocalDate kimlikStartDate,  LocalDate kimlikEndDay, boolean notified60Days, String university,
                         String department, LocalDate universityStartDate, LocalDate universityEndDate) {
        this.firstName = firstName;
        this.lastName=lastName;
        this.email=email;
        this.phoneNumber=phoneNumber;
        this.kimlikNumber=kimlikNumber;
        this.kimlikStartDate=kimlikStartDate;
        this.kimlikEndDate=kimlikEndDay;
        this.notified60Days=notified60Days;
        this.university=university;
        this.department=department;
        this.universityStartDate=universityStartDate;
        this.universityEndDate=universityEndDate;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getKimlikNumber() {
        return kimlikNumber;
    }

    public void setKimlikNumber(String kimlikNumber) {
        this.kimlikNumber = kimlikNumber;
    }

    public LocalDate getKimlikStartDate() {
        return kimlikStartDate;
    }

    public void setKimlikStartDate(LocalDate kimlikStartDate) {
        this.kimlikStartDate = kimlikStartDate;
    }

    public LocalDate getKimlikEndDate() {
        return kimlikEndDate;
    }

    public void setKimlikEndDate(LocalDate kimlikEndDay) {
        this.kimlikEndDate = kimlikEndDay;
    }

    public boolean isNotified60days() {
        return notified60Days;
    }

    public void setNotified60days(boolean notified60days) {
        this.notified60Days = notified60days;
    }



    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public LocalDate getUniversityStartDate() {
        return universityStartDate;
    }

    public void setUniversityStartDate(LocalDate universityStartDate) {
        this.universityStartDate = universityStartDate;
    }

    public LocalDate getUniversityEndDate() {
        return universityEndDate;
    }

    public void setUniversityEndDate(LocalDate universityEndDate) {
        this.universityEndDate = universityEndDate;
    }

    @Override
    public String toString() {
        return "StudentKimlik{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", kimlikNumber='" + kimlikNumber + '\'' +
                ", kimlikStartDate=" + kimlikStartDate +
                ", kimlikEndDate=" + kimlikEndDate +
                ", university='" + university + '\'' +
                ", department='" + department + '\'' +
                ", universityStartDate=" + universityStartDate +
                ", universityEndDate=" + universityEndDate +
                ", notified60Days=" + notified60Days +
                '}';
    }
}

package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity;

import jakarta.persistence.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "vorker_clients")
public class VorkerKimlik {

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

    @Column(name =  "phone_number")
    private String phoneNumber;

    @Column(name = "kimlik_number")
    private String kimlikNumber;

    @Column(name = "kimlik_start_date")
    private LocalDate kimlikStartDate;

    @Column(name = "kimlik_end_date")
    private LocalDate kimlikEndDate;

    @Column(name = "workplace")
    private String workplace;

    @Column(name = "notified_60_days")
    private boolean notified60Days;

    @Transient
    public long getDays(){
        if (kimlikEndDate==null){
            return 0;
        }else {
            return ChronoUnit.DAYS.between(LocalDate.now(), kimlikEndDate);
        }
    }

    public VorkerKimlik (){}

    public VorkerKimlik(String firstName, String lastName, String email, String phoneNumber, String kimlikNumber, LocalDate kimlikStartDate,  LocalDate kimlikEndDay, String workplace,boolean notified60Days) {
        this.firstName = firstName;
        this.lastName=lastName;
        this.email=email;
        this.phoneNumber=phoneNumber;
        this.kimlikNumber=kimlikNumber;
        this.kimlikStartDate=kimlikStartDate;
        this.kimlikEndDate=kimlikEndDay;
        this.workplace=workplace;
        this.notified60Days=notified60Days;

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

    public void setKimlikEndDate(LocalDate kimlikEndDate) {
        this.kimlikEndDate = kimlikEndDate;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public boolean isNotified60Days() {
        return notified60Days;
    }

    public void setNotified60Days(boolean notified60Days) {
        this.notified60Days = notified60Days;
    }

    @Override
    public String toString() {
        return "VorkerKimlik{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", kimlikNumber='" + kimlikNumber + '\'' +
                ", kimlikStartDate=" + kimlikStartDate +
                ", kimlikEndDate=" + kimlikEndDate +
                ", workplace='" + workplace + '\'' +
                ", notified60Days=" + notified60Days +
                '}';
    }
}

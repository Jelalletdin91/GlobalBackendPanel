package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "vorker_clients")
public class VorkerKimlik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "kimlik_number", nullable = false)
    private String kimlikNumber;

    @Column(name = "kimlik_start_date", nullable = false)
    private LocalDate kimlikStartDate;

    @Column(name = "kimlik_end_date", nullable = false)
    private LocalDate kimlikEndDate;

    @Column(name = "workplace")
    private String workplace;

    @Column(name = "notified_60_days", nullable = false)
    private boolean notified60Days;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @Transient
    public long getDaysLeft() {
        if (kimlikEndDate == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(LocalDate.now(), kimlikEndDate);
    }

    public VorkerKimlik() {
    }

    public VorkerKimlik(String firstName, String lastName, String email, String phoneNumber,
                        String kimlikNumber, LocalDate kimlikStartDate, LocalDate kimlikEndDate,
                        String workplace, boolean notified60Days) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.kimlikNumber = kimlikNumber;
        this.kimlikStartDate = kimlikStartDate;
        this.kimlikEndDate = kimlikEndDate;
        this.workplace = workplace;
        this.notified60Days = notified60Days;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getKimlikNumber() {
        return kimlikNumber;
    }

    public LocalDate getKimlikStartDate() {
        return kimlikStartDate;
    }

    public LocalDate getKimlikEndDate() {
        return kimlikEndDate;
    }

    public String getWorkplace() {
        return workplace;
    }

    public boolean isNotified60Days() {
        return notified60Days;
    }

    public Company getCompany() {
        return company;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setKimlikNumber(String kimlikNumber) {
        this.kimlikNumber = kimlikNumber;
    }

    public void setKimlikStartDate(LocalDate kimlikStartDate) {
        this.kimlikStartDate = kimlikStartDate;
    }

    public void setKimlikEndDate(LocalDate kimlikEndDate) {
        this.kimlikEndDate = kimlikEndDate;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public void setNotified60Days(boolean notified60Days) {
        this.notified60Days = notified60Days;
    }

    public void setCompany(Company company) {
        this.company = company;
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
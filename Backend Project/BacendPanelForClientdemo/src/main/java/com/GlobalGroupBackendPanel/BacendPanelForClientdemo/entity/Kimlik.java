package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name="clients")
public class Kimlik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",
    nullable = false)
    private Long id;

    @Column(name="first_name", nullable = false)
    private String firstName;

    @Column(name="last_name", nullable = false)
    private String lastName;

    @Column(name="email", nullable = false)
    private String email;

    @Column(name="phone_number", nullable = false)
    private String phoneNumber;

    @Column(name="kimlik_number", nullable = false)
    private String kimlikNumber;

    @Column(name="kimlik_start_date", nullable = false)
    private LocalDate kimlikStartDate;

    @Column(name="kimlik_end_date", nullable = false)
    private LocalDate kimlikEndDate;

    @Column(name="notified_60_days", nullable = false)
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

    public Kimlik(){}

    public Kimlik(String firstName, String lastName, String email, String phoneNumber, String kimlikNumber, LocalDate kimlikStartDate,  LocalDate kimlikEndDay, boolean notified60Days) {
        this.firstName = firstName;
        this.lastName=lastName;
        this.email=email;
        this.phoneNumber=phoneNumber;
        this.kimlikNumber=kimlikNumber;
        this.kimlikStartDate=kimlikStartDate;
        this.kimlikEndDate=kimlikEndDay;
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

    public void setKimlikEndDate(LocalDate kimlikEndDay) {
        this.kimlikEndDate = kimlikEndDay;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public boolean isNotified60Days() {
        return notified60Days;
    }

    public void setNotified60Days(boolean notified60Days) {
        this.notified60Days = notified60Days;
    }

    @Override
    public String toString() {
        return "Kimlik{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", kimlikNumber=" + kimlikNumber +
                ", kimlikStartDate=" + kimlikStartDate +
                ", kimlikEndDay=" + kimlikEndDate +
                ", notifie60days=" + notified60Days +
                '}';
    }
}

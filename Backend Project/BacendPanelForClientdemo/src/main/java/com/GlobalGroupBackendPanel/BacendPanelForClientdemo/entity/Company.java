package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "subscription_plan")
    private String subscriptionPlan;

    @Column(name = "subscription_start")
    private LocalDate subscriptionStart;

    @Column(name = "subscription_end")
    private LocalDate subscriptionEnd;

    @Column(name = "active", nullable = false)
    private boolean active = true;



    @Column(name = "invted_by_user_id")
    private String invitedBy;

    public Company() {
    }

    public Company(String name, String subscriptionPlan, LocalDate subscriptionStart,
                   LocalDate subscriptionEnd, boolean active, String invitedByUser) {
        this.name = name;
        this.subscriptionPlan = subscriptionPlan;
        this.subscriptionStart = subscriptionStart;
        this.subscriptionEnd = subscriptionEnd;
        this.active = active;
        this.invitedBy = invitedByUser;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSubscriptionPlan() {
        return subscriptionPlan;
    }

    public LocalDate getSubscriptionStart() {
        return subscriptionStart;
    }

    public LocalDate getSubscriptionEnd() {
        return subscriptionEnd;
    }

    public boolean isActive() {
        return active;
    }

    public String  getInvitedBy() {
        return invitedBy;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSubscriptionPlan(String subscriptionPlan) {
        this.subscriptionPlan = subscriptionPlan;
    }

    public void setSubscriptionStart(LocalDate subscriptionStart) {
        this.subscriptionStart = subscriptionStart;
    }

    public void setSubscriptionEnd(LocalDate subscriptionEnd) {
        this.subscriptionEnd = subscriptionEnd;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setInvitedBy(String invitedByUser) {
        this.invitedBy = invitedByUser;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", subscriptionPlan='" + subscriptionPlan + '\'' +
                ", subscriptionStart=" + subscriptionStart +
                ", subscriptionEnd=" + subscriptionEnd +
                ", active=" + active +
                '}';
    }
}
package com.GlobalGroupBackendPanel.BacendPanelForClientdemo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("COOOOODE"+encoder.encode("Kimlik96!"));

        //Jelalletdin Berjanov
    }
}
package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.KimlikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public class SchedulerKimlik {

    @Autowired
    KimlikRepository kimlikRepository;

    @Scheduled(cron = "0 0 9 * * *")
    public void scheduleEveryDay(){

        System.out.println("Schedule");

    }

}

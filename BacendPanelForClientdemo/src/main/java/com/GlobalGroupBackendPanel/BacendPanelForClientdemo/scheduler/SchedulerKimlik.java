package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.scheduler;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.Kimlik;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.KimlikRepository;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
public class SchedulerKimlik {

    private final KimlikRepository kimlikRepository;
    private final EmailService emailService;

    public SchedulerKimlik(KimlikRepository kimlikRepository, EmailService emailService) {
        this.kimlikRepository = kimlikRepository;
        this.emailService=emailService;
    }


    @Scheduled(fixedDelay = 60000)
    public void scheduleEveryDay(){

        List<Kimlik> kimliks = kimlikRepository.findAllByOrderByKimlikEndDateAsc();

        for (Kimlik kimlik: kimliks){
            if (kimlik.getDaysLeft() == 60){
                String text = "Kimlik " + kimlik.getDaysLeft() +  " gunden bitiyor:\n\n" + kimlik.getFirstName() + "\n" +
                        kimlik.getLastName() + "\n" + kimlik.getPhoneNumber() + "\n"+
                        kimlik.getEmail() + "\n" + kimlik.getKimlikNumber();

                emailService.sendCompanyAlert(text);

            }
        }

    }

}

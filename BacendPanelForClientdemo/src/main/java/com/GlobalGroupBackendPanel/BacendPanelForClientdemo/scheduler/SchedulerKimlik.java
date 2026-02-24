package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.scheduler;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.Kimlik;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.KimlikRepository;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SchedulerKimlik {

    private final KimlikRepository kimlikRepository;
    private final EmailService emailService;

    public SchedulerKimlik(KimlikRepository kimlikRepository, EmailService emailService) {
        this.kimlikRepository = kimlikRepository;
        this.emailService = emailService;
    }

    // ✅ раз в день (например в 09:00)
    @Scheduled(cron = "0 0 9 * * *")
    public void scheduleEveryDay() {

        List<Kimlik> kimliks = kimlikRepository.findByNotified60DaysFalse();

        for (Kimlik kimlik : kimliks) {

            long daysLeft = kimlik.getDaysLeft(); // у тебя уже @Transient getDaysLeft()

            if (daysLeft == 60) {

                String text =
                        "Kimlik 60 günden bitiyor:\n\n" +
                                "Ad: " + kimlik.getFirstName() + "\n" +
                                "Soyad: " + kimlik.getLastName() + "\n" +
                                "Telefon: " + kimlik.getPhoneNumber() + "\n" +
                                "Email: " + kimlik.getEmail() + "\n" +
                                "Kimlik No: " + kimlik.getKimlikNumber();

                emailService.sendCompanyAlert(text);

                // ✅ помечаем что уже отправили
                kimlik.setNotified60Days(true);
                kimlikRepository.save(kimlik);
            }
        }
    }
}
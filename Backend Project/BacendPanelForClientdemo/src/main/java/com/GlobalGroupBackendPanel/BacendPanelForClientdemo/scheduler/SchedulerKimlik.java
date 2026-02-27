package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.scheduler;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.Kimlik;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.StudentKimlik;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.KimlikRepository;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.StudentRepository;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SchedulerKimlik {

    private final KimlikRepository kimlikRepository;
    private final StudentRepository studentRepository;
    private final EmailService emailService;

    public SchedulerKimlik(KimlikRepository kimlikRepository, StudentRepository studentRepository, EmailService emailService) {
        this.kimlikRepository = kimlikRepository;
        this.studentRepository=studentRepository;
        this.emailService = emailService;
    }

    // ✅ раз в день (например в 09:00)
    @Scheduled(cron = "0 0 9 * * *")
    public void scheduleEveryDayForStudentKimlik() {

        List<StudentKimlik> studentKimliks = studentRepository.findByNotified60daysFalse();

        for (StudentKimlik studentKimlik : studentKimliks) {

            long daysLeft = studentKimlik.getDaysLeft(); // у тебя уже @Transient getDaysLeft()

            if (daysLeft <= 60 && daysLeft >= 0 && !studentKimlik.isNotified60days()) {

                String text =
                        "Kimlik 60 günden bitiyor:\n\n" +
                                "Ad: " + studentKimlik.getFirstName() + "\n" +
                                "Soyad: " + studentKimlik.getLastName() + "\n" +
                                "Telefon: " + studentKimlik.getPhoneNumber() + "\n" +
                                "Email: " + studentKimlik.getEmail() + "\n" +
                                "Kimlik No: " + studentKimlik.getKimlikNumber();

                emailService.sendCompanyAlert(text);

                // ✅ помечаем что уже отправили
                studentKimlik.setNotified60days(true);
                studentRepository.save(studentKimlik);
            }
        }
    }

    @Scheduled(cron = "0 5 9 * * *")
    public void scheduleEveryDay() {

        List<Kimlik> kimliks = kimlikRepository.findByNotified60DaysFalse();

        for (Kimlik kimlik : kimliks) {

            long daysLeft = kimlik.getDaysLeft(); // у тебя уже @Transient getDaysLeft()

            if (daysLeft <= 60 && daysLeft >= 0 && !kimlik.isNotified60Days()){
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
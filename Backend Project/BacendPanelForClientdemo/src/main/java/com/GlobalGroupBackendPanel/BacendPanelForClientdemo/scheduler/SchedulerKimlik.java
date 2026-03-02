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
    @Scheduled(cron = "0 0 9 * * *") // для теста
    public void scheduleStudents() {

        List<StudentKimlik> studentKimliks = studentRepository.findByNotified60DaysFalse();

        for (StudentKimlik s : studentKimliks) {
            long daysLeft = s.getDaysLeft();

            if (daysLeft <= 60 && daysLeft >= 0 && !s.isNotified60days()) {

                String text =
                        "Student Kimlik 60 günden bitiyor:\n\n" +
                                "Ad: " + s.getFirstName() + "\n" +
                                "Soyad: " + s.getLastName() + "\n" +
                                "Telefon: " + s.getPhoneNumber() + "\n" +
                                "Email: " + s.getEmail() + "\n" +
                                "Kimlik No: " + s.getKimlikNumber() + "\n" +
                                "Üniversite: " + s.getUniversity() + "\n" +
                                "Bölüm: " + s.getDepartment() + "\n" +
                                "Bitiş: " + s.getKimlikEndDate() + "\n" +
                                "Kalan gün: " + daysLeft;

                emailService.sendCompanyAlert(text);

                s.setNotified60days(true);
                studentRepository.save(s);
            }
        }
    }

    // CLIENTS (Kimlik)
    @Scheduled(cron = "0 5 9 * * *") // для теста, потом поменяем на раз в день
    public void scheduleClients() {

        List<Kimlik> kimliks = kimlikRepository.findByNotified60DaysFalse();

        for (Kimlik kimlik : kimliks) {
            long daysLeft = kimlik.getDaysLeft();

            // ✅ отправляем ОДИН раз, когда 0..60 дней
            if (daysLeft <= 60 && daysLeft >= 0 && !kimlik.isNotified60Days()) {

                String text =
                        "Kimlik 60 günden bitiyor:\n\n" +
                                "Ad: " + kimlik.getFirstName() + "\n" +
                                "Soyad: " + kimlik.getLastName() + "\n" +
                                "Telefon: " + kimlik.getPhoneNumber() + "\n" +
                                "Email: " + kimlik.getEmail() + "\n" +
                                "Kimlik No: " + kimlik.getKimlikNumber() + "\n" +
                                "Bitiş: " + kimlik.getKimlikEndDate() + "\n" +
                                "Kalan gün: " + daysLeft;

                emailService.sendCompanyAlert(text);

                kimlik.setNotified60Days(true);
                kimlikRepository.save(kimlik);
            }
        }
    }

}
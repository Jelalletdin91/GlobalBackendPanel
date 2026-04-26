package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.scheduler;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.AppUser;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.Kimlik;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.StudentKimlik;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.VorkerKimlik;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.KimlikRepository;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.StudentRepository;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.VorkerRepository;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.AppUser;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.UserRepository;

import java.util.List;

@Component
public class SchedulerKimlik {

    private final KimlikRepository kimlikRepository;
    private final StudentRepository studentRepository;
    private final VorkerRepository vorkerRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;

    public SchedulerKimlik(KimlikRepository kimlikRepository,
                           VorkerRepository vorkerRepository,
                           StudentRepository studentRepository,
                           EmailService emailService,
                           UserRepository userRepository) {
        this.kimlikRepository = kimlikRepository;
        this.studentRepository = studentRepository;
        this.vorkerRepository = vorkerRepository;
        this.emailService = emailService;
        this.userRepository=userRepository;
    }

    @Scheduled(cron = "0 0 9 * * *")
    public void scheduleStudents() {
        List<StudentKimlik> studentKimliks = studentRepository.findByNotified60DaysFalse();

        for (StudentKimlik s : studentKimliks) {
            long daysLeft = s.getDaysLeft();

            if (daysLeft <= 60 && daysLeft >= 0 && !s.isNotified60Days()) {
                String toEmail = getCompanyOwnerEmail(s.getCompany() == null ? null : s.getCompany().getId());

                if (toEmail == null) {
                    continue;
                }

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

                emailService.sendCompanyAlert(toEmail, text);

                s.setNotified60Days(true);
                studentRepository.save(s);
            }
        }
    }

    @Scheduled(cron = "0 5 9 * * *")
    public void scheduleClients() {
        List<Kimlik> kimliks = kimlikRepository.findByNotified60DaysFalse();

        for (Kimlik kimlik : kimliks) {
            long daysLeft = kimlik.getDaysLeft();

            if (daysLeft <= 60 && daysLeft >= 0 && !kimlik.isNotified60Days()) {
                String toEmail = getCompanyOwnerEmail(kimlik.getCompany() == null ? null : kimlik.getCompany().getId());

                if (toEmail == null) {
                    continue;
                }

                String text =
                        "Kimlik 60 günden bitiyor:\n\n" +
                                "Ad: " + kimlik.getFirstName() + "\n" +
                                "Soyad: " + kimlik.getLastName() + "\n" +
                                "Telefon: " + kimlik.getPhoneNumber() + "\n" +
                                "Email: " + kimlik.getEmail() + "\n" +
                                "Kimlik No: " + kimlik.getKimlikNumber() + "\n" +
                                "Bitiş: " + kimlik.getKimlikEndDate() + "\n" +
                                "Kalan gün: " + daysLeft;

                emailService.sendCompanyAlert(toEmail, text);

                kimlik.setNotified60Days(true);
                kimlikRepository.save(kimlik);
            }
        }
    }

    @Scheduled(cron = "0 10 9 * * *")
    public void scheduleVorkerKimlik() {
        List<VorkerKimlik> vorkerKimliks = vorkerRepository.findByNotified60DaysFalse();

        for (VorkerKimlik vorkerKimlik : vorkerKimliks) {
            long daysLeft = vorkerKimlik.getDaysLeft();

            if (daysLeft <= 60 && daysLeft >= 0 && !vorkerKimlik.isNotified60Days()) {
                String toEmail = getCompanyOwnerEmail(vorkerKimlik.getCompany() == null ? null : vorkerKimlik.getCompany().getId());

                if (toEmail == null) {
                    continue;
                }

                String text =
                        "Worker Kimlik 60 günden bitiyor:\n\n" +
                                "Ad: " + vorkerKimlik.getFirstName() + "\n" +
                                "Soyad: " + vorkerKimlik.getLastName() + "\n" +
                                "Telefon: " + vorkerKimlik.getPhoneNumber() + "\n" +
                                "Email: " + vorkerKimlik.getEmail() + "\n" +
                                "Kimlik No: " + vorkerKimlik.getKimlikNumber() + "\n" +
                                "Bitiş: " + vorkerKimlik.getKimlikEndDate() + "\n" +
                                "Çalışan yeri: " + vorkerKimlik.getWorkplace() + "\n" +
                                "Kalan gün: " + daysLeft;

                emailService.sendCompanyAlert(toEmail, text);

                vorkerKimlik.setNotified60Days(true);
                vorkerRepository.save(vorkerKimlik);
            }
        }
    }

    private String getCompanyOwnerEmail(Long companyId) {
        if (companyId == null) {
            return null;
        }

        List<AppUser> users = userRepository.findByCompanyId(companyId);

        for (AppUser user : users) {
            boolean isYonetici = user.getRoles()
                    .stream()
                    .anyMatch(role -> role.getName().equals("YONETICI"));

            if (isYonetici && user.getEmail() != null && !user.getEmail().isBlank()) {
                return user.getEmail();
            }
        }

        return null;
    }
}
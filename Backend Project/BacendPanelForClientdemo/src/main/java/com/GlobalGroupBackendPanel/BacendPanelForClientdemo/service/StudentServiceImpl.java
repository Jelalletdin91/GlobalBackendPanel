package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.AppUser;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.StudentKimlik;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final ActivityLogService activityLogService;
    private final CurrentUserService currentUserService;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository,
                              ActivityLogService activityLogService,
                              CurrentUserService currentUserService) {
        this.studentRepository = studentRepository;
        this.activityLogService = activityLogService;
        this.currentUserService = currentUserService;
    }

    @Override
    public List<StudentKimlik> findAll() {
        AppUser currentUser = currentUserService.getCurrentUser();

        if (currentUser.getCompany() == null) {
            throw new RuntimeException("User has no company");
        }

        return studentRepository.findByCompanyIdOrderByKimlikEndDateAsc(
                currentUser.getCompany().getId()
        );
    }

    @Override
    public List<StudentKimlik> search(String keyword) {
        AppUser currentUser = currentUserService.getCurrentUser();

        if (currentUser.getCompany() == null) {
            throw new RuntimeException("User has no company");
        }

        if (keyword == null || keyword.trim().isEmpty()) {
            return studentRepository.findByCompanyIdOrderByKimlikEndDateAsc(
                    currentUser.getCompany().getId()
            );
        }

        return studentRepository.searchByCompanyIdAndKeyword(
                currentUser.getCompany().getId(),
                keyword
        );
    }

    @Override
    public StudentKimlik findById(Long id) {
        AppUser currentUser = currentUserService.getCurrentUser();

        StudentKimlik studentKimlik = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (studentKimlik.getCompany() == null ||
                currentUser.getCompany() == null ||
                !studentKimlik.getCompany().getId().equals(currentUser.getCompany().getId())) {
            throw new RuntimeException("ACCESS DENIED");
        }

        return studentKimlik;
    }

    @Override
    public StudentKimlik save(StudentKimlik studentKimlik) {
        AppUser currentUser = currentUserService.getCurrentUser();

        if (currentUser.getCompany() == null) {
            throw new RuntimeException("User has no company");
        }

        boolean isNew = (studentKimlik.getId() == null);

        if (isNew) {
            studentKimlik.setCompany(currentUser.getCompany());
            studentKimlik.setNotified60Days(false);
        } else {
            StudentKimlik existing = findById(studentKimlik.getId());
            studentKimlik.setCompany(existing.getCompany());
        }

        StudentKimlik savedStudentKimlik = studentRepository.save(studentKimlik);
        String fullName = savedStudentKimlik.getFirstName() + " " + savedStudentKimlik.getLastName();

        if (isNew) {
            activityLogService.save("CREATE", "STUDENT", fullName, "New student added: " + fullName);
        } else {
            activityLogService.save("UPDATE", "STUDENT", fullName, "Student updated: " + fullName);
        }

        return savedStudentKimlik;
    }

    @Override
    public void deleteById(Long id) {
        StudentKimlik studentKimlik = findById(id);

        String fullName = studentKimlik.getFirstName() + " " + studentKimlik.getLastName();

        studentRepository.deleteById(id);

        activityLogService.save("DELETE", "STUDENT", fullName, "Student Deleted: " + fullName);
    }
}
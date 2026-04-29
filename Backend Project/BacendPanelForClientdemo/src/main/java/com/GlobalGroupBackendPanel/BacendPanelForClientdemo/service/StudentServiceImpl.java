package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.Company;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.StudentKimlik;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final ActivityLogService activityLogService;
    private final CompanyContextService companyContextService;

    public StudentServiceImpl(StudentRepository studentRepository,
                              ActivityLogService activityLogService,
                              CompanyContextService companyContextService) {
        this.studentRepository = studentRepository;
        this.activityLogService = activityLogService;
        this.companyContextService = companyContextService;
    }

    @Override
    public List<StudentKimlik> findAll() {
        Long companyId = companyContextService.getCurrentCompanyId();
        return studentRepository.findByCompanyIdOrderByKimlikEndDateAsc(companyId);
    }

    @Override
    public List<StudentKimlik> search(String keyword) {
        Long companyId = companyContextService.getCurrentCompanyId();

        if (keyword == null || keyword.trim().isEmpty()) {
            return studentRepository.findByCompanyIdOrderByKimlikEndDateAsc(companyId);
        }

        return studentRepository.searchByCompanyIdAndKeyword(companyId, keyword);
    }

    @Override
    public StudentKimlik findById(Long id) {
        Long companyId = companyContextService.getCurrentCompanyId();

        StudentKimlik studentKimlik = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (studentKimlik.getCompany() == null ||
                !studentKimlik.getCompany().getId().equals(companyId)) {
            throw new RuntimeException("ACCESS DENIED");
        }

        return studentKimlik;
    }

    @Override
    public StudentKimlik save(StudentKimlik studentKimlik) {
        Company company = companyContextService.getCurrentCompany();

        boolean isNew = (studentKimlik.getId() == null);

        if (isNew) {
            studentKimlik.setCompany(company);
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
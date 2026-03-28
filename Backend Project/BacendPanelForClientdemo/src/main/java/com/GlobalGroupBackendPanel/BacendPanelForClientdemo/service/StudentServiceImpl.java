package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.StudentKimlik;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.ActivityLogRepository;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class StudentServiceImpl implements StudentService{

     private StudentRepository studentRepository;
     private final ActivityLogService activityLogService;


    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, ActivityLogService activityLogService){
        this.studentRepository=studentRepository;
        this.activityLogService=activityLogService;
    }



    @Override
    public List<StudentKimlik> findAll() {
        return studentRepository.findAllByOrderByKimlikEndDateAsc();
    }

    @Override
    public StudentKimlik findById(Long id) {

        Optional<StudentKimlik> result = studentRepository.findById(id);

        StudentKimlik studentKimlik = null;

        if (result.isPresent()){
            studentKimlik = result.get();
        }else {
            throw new RuntimeException("Did not find");
        }

        return studentKimlik;

    }

    @Override
    public StudentKimlik save(StudentKimlik studentKimlik) {

        boolean isNew = (studentKimlik.getId() == null);

        if (isNew) {
            studentKimlik.setNotified60days(false);
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
        String fullName=studentKimlik.getFirstName()+" " +studentKimlik.getLastName();

        studentRepository.deleteById(id);

        activityLogService.save("DELETE", "STUDENT", fullName, "Student Deleted: "+ fullName);


    }
}

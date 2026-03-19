package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.StudentKimlik;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class StudentServiceImpl implements StudentService{

    StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository){
        this.studentRepository=studentRepository;
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

        if(studentKimlik.getId() == null){
            studentKimlik.setNotified60days(false);
        }


        return studentRepository.save(studentKimlik);
    }

    @Override
    public void deleteById(Long id) {
        studentRepository.deleteById(id);


    }
}

package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.controller;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.KimlikRepository;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.StudentRepository;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.VorkerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    private final KimlikRepository kimlikRepository;
    private final StudentRepository studentRepository;
    private final VorkerRepository vorkerRepository;

    public MainController(KimlikRepository kimlikRepository, StudentRepository studentRepository, VorkerRepository vorkerRepository){
        this.kimlikRepository=kimlikRepository;
        this.studentRepository=studentRepository;
        this.vorkerRepository=vorkerRepository;
    }

    @GetMapping("/")
    public String showMainPage(Model model){
        long kimlikCounts=kimlikRepository.count();
        long studentCounts=studentRepository.count();
        long vorkerCounts=vorkerRepository.count();

        model.addAttribute("kimlikCounts", kimlikCounts);
        model.addAttribute("studentCounts", studentCounts);
        model.addAttribute("vorkerCounts", vorkerCounts);


        return "Kimlik/homePage";
    }

}

package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.controller;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.Kimlik;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service.KimlikerService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("kimlik")
public class KimlikController {

    private KimlikerService kimlikerService;

    public KimlikController(KimlikerService kimlikerService){
        this.kimlikerService=kimlikerService;
    }

    @GetMapping("/list")
    public String list (Model theModel){
        List<Kimlik> kimliks = kimlikerService.findAll();

        theModel.addAttribute("kimliks", kimliks);

        return "Kimlik/list_of_kimlik";
    }

    @GetMapping("/showForm")
    public String showForm(Model theModel){

        Kimlik kimlik = new Kimlik();

        theModel.addAttribute("kimlik", kimlik);

        return "Kimlik/form";

    }

    @PostMapping("/save")
    public String save(@ModelAttribute("kimlik") Kimlik kimlik, Model model) {
        try {
            kimlikerService.save(kimlik);
            return "redirect:/kimlik/list";
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            model.addAttribute("kimlik", kimlik);
            model.addAttribute("errorMessage", "This kimlik number already exists. Please enter a different kimlik number.");
            return "Kimlik/form";
        } catch (Exception e) {
            model.addAttribute("kimlik", kimlik);
            model.addAttribute("errorMessage", "Something went wrong while saving the record.");
            return "Kimlik/form";
        }
    }

    @GetMapping("/showFormForUpdate")
    public String update(@RequestParam("kimlikId") Long id, Model theModel){

        Kimlik kimlik = kimlikerService.findById(id);

        theModel.addAttribute("kimlik", kimlik);

        return "Kimlik/form";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("kimlikId") Long id){
        kimlikerService.deleteById(id);


        return "redirect:/kimlik/list";
    }


}

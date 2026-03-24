package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.controller;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.VorkerKimlik;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service.VorkerKimlikService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/VorkerKimlik")
public class VorkerKimlikController {

    private final VorkerKimlikService vorkerKimlikService;

    public VorkerKimlikController(VorkerKimlikService vorkerKimlikService) {
        this.vorkerKimlikService = vorkerKimlikService;
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<VorkerKimlik> vorkerKimliks = vorkerKimlikService.findAll();
        model.addAttribute("vorkerKimliks", vorkerKimliks);
        return "Kimlik/vorker-kimlik-list";
    }

    @GetMapping("/showForm")
    public String showForm(Model model) {
        VorkerKimlik vorkerKimlik = new VorkerKimlik();
        model.addAttribute("vorkerKimlik", vorkerKimlik);
        return "Kimlik/vorker-kimlik-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("vorkerKimlik") VorkerKimlik vorkerKimlik) {
        vorkerKimlik.setNotified60Days(false);
        vorkerKimlikService.save(vorkerKimlik);
        return "redirect:/VorkerKimlik/list";
    }

    @GetMapping("/showVorkerKimlikForUpdate")
    public String update(@RequestParam("vorkerKimlikId") Long id, Model model) {
        VorkerKimlik vorkerKimlik = vorkerKimlikService.findById(id);
        model.addAttribute("vorkerKimlik", vorkerKimlik);
        return "Kimlik/vorker-kimlik-form";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("vorkerKimlikId") Long id) {
        vorkerKimlikService.deleteById(id);
        return "redirect:/VorkerKimlik/list";
    }
}
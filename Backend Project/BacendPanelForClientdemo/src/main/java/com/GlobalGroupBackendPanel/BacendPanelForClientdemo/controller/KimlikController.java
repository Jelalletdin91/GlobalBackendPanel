package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.controller;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.Company;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.Kimlik;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service.CompanyContextService;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service.KimlikerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("kimlik")
public class KimlikController {

    private final KimlikerService kimlikerService;
    private final CompanyContextService companyContextService;

    public KimlikController(KimlikerService kimlikerService,
                            CompanyContextService companyContextService) {
        this.kimlikerService = kimlikerService;
        this.companyContextService = companyContextService;
    }

    private void addCompanyInfo(Model model) {
        Company company = companyContextService.getCurrentCompany();
        model.addAttribute("companyName", company.getName());
        model.addAttribute("companyFirstLetterOfName", company.getName().charAt(0));
    }

    @GetMapping("/list")
    public String list(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Kimlik> kimliks = kimlikerService.search(keyword);

        addCompanyInfo(model);
        model.addAttribute("kimliks", kimliks);
        model.addAttribute("keyword", keyword);

        return "Kimlik/list_of_kimlik";
    }

    @GetMapping("/showForm")
    public String showForm(Model model) {
        addCompanyInfo(model);
        model.addAttribute("kimlik", new Kimlik());
        return "Kimlik/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("kimlik") Kimlik kimlik, Model model) {
        try {
            kimlikerService.save(kimlik);
            return "redirect:/kimlik/list";
        } catch (Exception e) {
            addCompanyInfo(model);
            model.addAttribute("kimlik", kimlik);
            model.addAttribute("errorMessage", "Something went wrong while saving the record.");
            return "Kimlik/form";
        }
    }

    @GetMapping("/showFormForUpdate")
    public String update(@RequestParam("kimlikId") Long id, Model model) {
        addCompanyInfo(model);
        model.addAttribute("kimlik", kimlikerService.findById(id));
        return "Kimlik/form";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("kimlikId") Long id) {
        kimlikerService.deleteById(id);
        return "redirect:/kimlik/list";
    }
}
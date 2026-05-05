package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.controller;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.Company;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.VorkerKimlik;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service.CompanyContextService;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service.VorkerKimlikService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/VorkerKimlik")
public class VorkerKimlikController {

    private final VorkerKimlikService vorkerKimlikService;
    private final CompanyContextService companyContextService;

    public VorkerKimlikController(VorkerKimlikService vorkerKimlikService,
                                  CompanyContextService companyContextService) {
        this.vorkerKimlikService = vorkerKimlikService;
        this.companyContextService = companyContextService;
    }

    private void addCompanyInfo(Model model) {
        Company company = companyContextService.getCurrentCompany();
        model.addAttribute("companyName", company.getName());
        model.addAttribute("companyFirstLetterOfName", company.getName().charAt(0));
    }

    @GetMapping("/list")
    public String list(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<VorkerKimlik> vorkerKimliks = vorkerKimlikService.search(keyword);

        addCompanyInfo(model);
        model.addAttribute("vorkerKimliks", vorkerKimliks);
        model.addAttribute("keyword", keyword);

        return "Kimlik/vorker-kimlik-list";
    }

    @GetMapping("/showForm")
    public String showForm(Model model) {
        addCompanyInfo(model);
        model.addAttribute("vorkerKimlik", new VorkerKimlik());
        return "Kimlik/vorker-kimlik-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("vorkerKimlik") VorkerKimlik vorkerKimlik, Model model) {
        try {
            vorkerKimlikService.save(vorkerKimlik);
            return "redirect:/VorkerKimlik/list";
        } catch (Exception e) {
            addCompanyInfo(model);
            model.addAttribute("vorkerKimlik", vorkerKimlik);
            model.addAttribute("errorMessage", "Something went wrong while saving the record.");
            return "Kimlik/vorker-kimlik-form";
        }
    }

    @GetMapping("/showVorkerKimlikForUpdate")
    public String update(@RequestParam("vorkerKimlikId") Long id, Model model) {
        addCompanyInfo(model);
        model.addAttribute("vorkerKimlik", vorkerKimlikService.findById(id));
        return "Kimlik/vorker-kimlik-form";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("vorkerKimlikId") Long id) {
        vorkerKimlikService.deleteById(id);
        return "redirect:/VorkerKimlik/list";
    }
}
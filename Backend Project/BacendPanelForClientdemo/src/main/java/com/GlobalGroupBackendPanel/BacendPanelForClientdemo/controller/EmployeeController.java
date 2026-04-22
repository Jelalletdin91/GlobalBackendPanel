package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.controller;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.AppUser;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service.EmployeeService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    @GetMapping("/list")
    public String list(Model model){
        List<AppUser> employees = employeeService.findAllEmployee();
        model.addAttribute("employees", employees);
        return "Kimlik/employee-list";
    }

    @GetMapping("/showForm")
    public String showForm(Model model){
        AppUser appUser = new AppUser();
        model.addAttribute("employee", appUser);
        return "Kimlik/employee-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("employee") AppUser employee,
                       @RequestParam(value = "kimlikRole", required = false, defaultValue = "false") boolean kimlikRole,
                       @RequestParam(value = "studentRole", required = false, defaultValue = "false") boolean studentRole,
                       @RequestParam(value = "workerRole", required = false, defaultValue = "false") boolean workerRole,
                       Model model) {

        try {
            employeeService.save(employee, kimlikRole, studentRole, workerRole);
            return "redirect:/employee/list";

        } catch (DataIntegrityViolationException ex) {
            model.addAttribute("employee", employee);
            model.addAttribute("errorMessage", "This username already exists. Please choose another username.");

            model.addAttribute("kimlikRole", kimlikRole);
            model.addAttribute("studentRole", studentRole);
            model.addAttribute("workerRole", workerRole);

            return "Kimlik/employee-form";

        } catch (RuntimeException ex) {
            model.addAttribute("employee", employee);
            model.addAttribute("errorMessage", ex.getMessage());

            model.addAttribute("kimlikRole", kimlikRole);
            model.addAttribute("studentRole", studentRole);
            model.addAttribute("workerRole", workerRole);

            return "Kimlik/employee-form";
        }
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("employeeId") Long id, Model model){
        AppUser employee = employeeService.findById(id);

        employee.setPassword("");

        model.addAttribute("employee", employee);
        model.addAttribute("kimlikRole", employee.getRoles().stream().anyMatch(r -> r.getName().equals("Kimlik")));
        model.addAttribute("studentRole", employee.getRoles().stream().anyMatch(r -> r.getName().equals("StudentKimlik")));
        model.addAttribute("workerRole", employee.getRoles().stream().anyMatch(r -> r.getName().equals("VorkerKimlik")));

        return "Kimlik/employee-form";
    }

    @GetMapping("/delete")
    public String deleteById(@RequestParam("employeeId") Long id, Model model){
        employeeService.deleteById(id);
        return "redirect:/employee/list";
    }
}
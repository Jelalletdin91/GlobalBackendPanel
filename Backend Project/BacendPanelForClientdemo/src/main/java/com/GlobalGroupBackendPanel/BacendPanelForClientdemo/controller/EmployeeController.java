package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.controller;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.AppUser;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.Kimlik;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService){
        this.employeeService=employeeService;
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
                       @RequestParam(value="kimlikRole", required = false) boolean kimlikRole,
                       @RequestParam(value="studentRole", required = false)boolean studentRole,
                       @RequestParam(value="workerRole", required = false)boolean workerRole
                       ){

        employeeService.save(employee, kimlikRole, studentRole, workerRole);

        return "redirect:/employee/list";

    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("employeeId") Long id, Model model){

        AppUser employee = employeeService.findById(id);

        model.addAttribute("employee", employee);

        return "Kimlik/employee-form";
    }

    @GetMapping("/delete")
    public String deleteById(@RequestParam("employeeId") Long id){

        employeeService.deleteById(id);

        return "redirect:/employee/list";
    }

}

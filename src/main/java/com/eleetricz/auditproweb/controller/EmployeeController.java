package com.eleetricz.auditproweb.controller;

import com.eleetricz.auditproweb.service.CompanyService;
import com.eleetricz.auditproweb.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class EmployeeController {
    private final CompanyService companyService;
    private final EmployeeService employeeService;

    public EmployeeController(CompanyService cs, EmployeeService es) {
        this.companyService = cs;
        this.employeeService = es;
    }

    @GetMapping("/empresas/{id}/funcionarios")
    public String listEmployees(@PathVariable Long id, Model model) {
        var company = companyService.findById(id);
        model.addAttribute("company", company);
        model.addAttribute("employees", employeeService.findByCompany(id));
        return "company";
    }
}

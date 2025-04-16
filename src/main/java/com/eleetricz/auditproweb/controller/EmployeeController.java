package com.eleetricz.auditproweb.controller;

import com.eleetricz.auditproweb.dto.EmployeeWithDocumentCount;
import com.eleetricz.auditproweb.service.CompanyService;
import com.eleetricz.auditproweb.service.DocumentService;
import com.eleetricz.auditproweb.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class EmployeeController {
    private final CompanyService companyService;
    private final EmployeeService employeeService;
    private final DocumentService documentService;

    public EmployeeController(CompanyService companyService, EmployeeService employeeService, DocumentService documentService) {
        this.companyService = companyService;
        this.employeeService = employeeService;
        this.documentService = documentService;
    }

    @GetMapping("/empresas/{id}/funcionarios")
    public String listEmployees(@PathVariable Long id, Model model) {
        var company = companyService.findById(id);

        var employees = employeeService.findByCompany(id);

        var employeesWithCount = employees.stream()
                        .map(employee -> new EmployeeWithDocumentCount(
                                employee.getId(),
                                employee.getName(),
                                documentService.countByEmployeeId(employee.getId())
                        ))
                                .toList();

        model.addAttribute("company", company);
        model.addAttribute("employees", employeesWithCount);

        return "company";
    }
}

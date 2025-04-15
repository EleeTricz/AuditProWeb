package com.eleetricz.auditproweb.controller;

import com.eleetricz.auditproweb.service.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CompanyController {
    private final CompanyService service;
    public CompanyController(CompanyService service){
        this.service = service;
    }

    @GetMapping("/empresas")
    public String listCompanies(Model model) {
        model.addAttribute("companies", service.findAll());
        return "companies";
    }
}

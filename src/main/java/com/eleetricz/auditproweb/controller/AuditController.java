package com.eleetricz.auditproweb.controller;

import com.eleetricz.auditproweb.dto.AuditResume;
import com.eleetricz.auditproweb.model.Company;
import com.eleetricz.auditproweb.repository.CompanyRepository;
import com.eleetricz.auditproweb.service.AuditService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@Controller
public class AuditController {

    private final AuditService auditService;
    private final CompanyRepository companyRepo;

    public AuditController(AuditService auditService, CompanyRepository companyRepo) {
        this.auditService = auditService;
        this.companyRepo = companyRepo;
    }

    @GetMapping("/empresas/{id}/dashboard")
    public String dashboard(@PathVariable("id") Long id, Model model) {
        Company company = companyRepo.findById(id).orElseThrow();

        double folhaPercentage = auditService.calculateFolhaPercentageByCompany(company);
        List<AuditResume> resumoFuncionarios = auditService.getAuditResumeByEmployee(company);
        List<String> missingFolha = auditService.listMissingFolhaByCompany(company);

        model.addAttribute("company", company.getName());
        model.addAttribute("folhaPercentage", folhaPercentage);
        model.addAttribute("resumoFuncionarios", resumoFuncionarios);
        model.addAttribute("missingFolha", missingFolha);

        return "dashboard";
    }

}

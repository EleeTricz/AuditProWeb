package com.eleetricz.auditproweb.controller;

import com.eleetricz.auditproweb.model.DocumentType;
import com.eleetricz.auditproweb.model.enums.Month;
import com.eleetricz.auditproweb.service.DocumentService;
import com.eleetricz.auditproweb.service.EmployeeService;
import com.eleetricz.auditproweb.service.StorageService;
import com.eleetricz.auditproweb.utils.YearProvider;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@Controller
public class DocumentController {
    private final DocumentService documentService;
    private final EmployeeService employeeService;
    private final StorageService storageService;


    public DocumentController(DocumentService documentService, EmployeeService employeeService, StorageService storageService) {
        this.documentService = documentService;
        this.employeeService = employeeService;
        this.storageService = storageService;
    }

    @GetMapping("/funcionarios/{id}/documentos")
    public String listDocuments(@PathVariable Long id,
                                @RequestParam(required = false) Integer year,
                                @RequestParam(required = false) Integer month,
                                @RequestParam(required = false, name = "documentType") DocumentType type,
                                Model model) {
        model.addAttribute("employee", employeeService.findById(id));
        model.addAttribute("documents", documentService.findByEmployeeWithFilters(id, year, month, type));
        model.addAttribute("months", Month.values());
        model.addAttribute("documentTypes", DocumentType.values());
        model.addAttribute("years", YearProvider.getYears(2020, 2025));
        model.addAttribute("yearSelected", year);
        model.addAttribute("monthSelected", month);
        model.addAttribute("typeSelected", type);
        return "employee";
    }

    @GetMapping("/pdf/{company}/{fileName:.+}")
    public ResponseEntity<Resource> getPdf(
            @PathVariable String company,
            @PathVariable String fileName) {
        try {
            Resource resource = storageService.loadPdf(company, fileName);

            if (resource == null || !resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }



}

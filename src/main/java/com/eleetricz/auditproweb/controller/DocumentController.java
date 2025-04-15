package com.eleetricz.auditproweb.controller;

import com.eleetricz.auditproweb.model.DocumentType;
import com.eleetricz.auditproweb.model.enums.Month;
import com.eleetricz.auditproweb.service.DocumentService;
import com.eleetricz.auditproweb.service.EmployeeService;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@Controller
public class DocumentController {
    private final DocumentService documentService;
    private final EmployeeService employeeService;

    public DocumentController(DocumentService documentService, EmployeeService employeeService){
        this.documentService = documentService;
        this.employeeService = employeeService;
    }

    @GetMapping("/funcionarios/{id}/documentos")
    public String listDocuments(@PathVariable Long id, Model model) {
        model.addAttribute("employee", employeeService.findById(id));
        model.addAttribute("documents", documentService.findByEmployee(id));
        model.addAttribute("months", Month.values());
        model.addAttribute("documentTypes", DocumentType.values());
        List<Integer> years = YearProvider.getYears(2020, 2025);
        model.addAttribute("years", years);
        return "employee";
    }

    @GetMapping("/pdf/{empresa}/{nomeArquivo:.+}")
    public ResponseEntity<Resource> takePdf(
            @PathVariable String empresa,
            @PathVariable String nomeArquivo) throws IOException {
        Path basePath = Paths.get("C:/Users/TERMINAL-3/Desktop/PROJETO_AUDITORIA/PROJETO_AUDITORIA_PDFS");
        Path filePath = basePath.resolve(empresa).resolve(nomeArquivo);

        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(filePath.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }



}

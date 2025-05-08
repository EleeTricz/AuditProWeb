package com.eleetricz.auditproweb.demo;

import com.eleetricz.auditproweb.model.*;
import com.eleetricz.auditproweb.repository.CompanyRepository;
import com.eleetricz.auditproweb.repository.DocumentRepository;
import com.eleetricz.auditproweb.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

import java.time.LocalDate;
import java.util.Arrays;

@Component
@Profile("demo")
public class DemoDataLoader implements CommandLineRunner {

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DocumentRepository documentRepository;

    @Override
    public void run(String... args) {
        Company company = new Company();
        company.setName("Empresa Exemplo");
        companyRepository.save(company);

        Employee joao = new Employee();
        joao.setName("João Silva");
        joao.setAdmissionDate(LocalDate.of(2021, 1, 10));
        joao.setCompany(company);

        Employee maria = new Employee();
        maria.setName("Maria Souza");
        maria.setAdmissionDate(LocalDate.of(2022, 5, 20));
        maria.setCompany(company);

        employeeRepository.saveAll(Arrays.asList(joao, maria));

        Document doc1 = new Document();
        doc1.setEmployee(joao);
        doc1.setCompany(company);
        doc1.setType(DocumentType.CONTRACHEQUE);
        doc1.setYear(2024);
        doc1.setMonth(3);

        Document doc2 = new Document();
        doc2.setEmployee(maria);
        doc2.setCompany(company);
        doc2.setType(DocumentType.FERIAS);
        doc2.setYear(2024);
        doc2.setMonth(3);

        documentRepository.saveAll(Arrays.asList(doc1, doc2));
    }
}

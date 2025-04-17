package com.eleetricz.auditproweb.service;

import com.eleetricz.auditproweb.dto.AuditResume;
import com.eleetricz.auditproweb.model.Company;
import com.eleetricz.auditproweb.model.DocumentType;
import com.eleetricz.auditproweb.model.Employee;
import com.eleetricz.auditproweb.repository.DocumentRepository;
import com.eleetricz.auditproweb.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class AuditServiceImpl implements AuditService {
    private final DocumentRepository documentRepo;
    private final EmployeeRepository employeeRepo;

    private static final int START_YEAR = 2020;
    private static final int START_MONTH = 1;
    private static final int END_YEAR = 2025;
    private static final int END_MONTH = 2;

    public AuditServiceImpl(DocumentRepository documentRepo, EmployeeRepository employeeRepo) {
        this.documentRepo = documentRepo;
        this.employeeRepo = employeeRepo;
    }

    private List<int[]> generateMonthInterval() {
        List<int[]> months = new ArrayList<>();
        int year = START_YEAR;
        int month = START_MONTH;

        while (year < END_YEAR || (year == END_YEAR && month <= END_MONTH)) {
            months.add(new int[]{year, month});
            month++;
            if (month > 12) {
                month = 1;
                year++;
            }
        }

        return months;
    }



    @Override
    public Map<String, List<String>> listMissingDocumentsByEmployee(Company company) {
        List<Employee> employees = employeeRepo.findByCompanyId(company.getId());
        List<int[]> months = generateMonthInterval();
        Map<String, List<String>> missing = new HashMap<>();

        for (Employee emp : employees) {
            List<String> missingDocs = new ArrayList<>();

            // Obter a data de admissão
            LocalDate admissionDate = emp.getAdmissionDate() != null ? emp.getAdmissionDate() : LocalDate.of(2020, 1, 1); // Se for null, define como 01/01/2020

            for (int[] ym : months) {
                int year = ym[0];
                int month = ym[1];

                // Só considerar documentos a partir da data de admissão
                LocalDate competencia = LocalDate.of(year, month, 1); // Competência do mês
                if (competencia.isBefore(admissionDate)) {
                    continue; // Pular meses antes da admissão
                }

                // Verificar férias e contracheques
                boolean hasVacation = documentRepo.existsByCompanyAndEmployeeAndYearAndMonthAndType(company, emp, year, month, DocumentType.FERIAS);
                if (!hasVacation) {
                    boolean hasContracheque = documentRepo.existsByCompanyAndEmployeeAndYearAndMonthAndType(company, emp, year, month, DocumentType.CONTRACHEQUE);
                    if (!hasContracheque) {
                        missingDocs.add(String.format("%02d/%d - CONTRACHEQUE", month, year));
                    }
                }

                // Verificar 13º salário
                if (month == 11 || month == 12) {
                    boolean has13 = documentRepo.existsByCompanyAndEmployeeAndYearAndMonthAndType(company, emp, year, month, DocumentType.CONTRACHEQUE13);
                    if (!has13) {
                        missingDocs.add(String.format("%02d/%d - CONTRACHEQUE13", month, year));
                    }
                }
            }

            missing.put(emp.getName(), missingDocs);
        }

        return missing;
    }

    @Override
    public List<String> listMissingFolhaByCompany(Company company) {


        List<int[]> months = generateMonthInterval();
        List<String> missingFolhas = new ArrayList<>();

        for (int[] ym : months) {
            int year = ym[0];
            int month = ym[1];

            boolean hasFolha = documentRepo.existsByCompanyAndEmployeeIsNullAndYearAndMonthAndType(company, year, month, DocumentType.FOLHA);
            if (!hasFolha) {
                missingFolhas.add(String.format("%02d/%d - FOLHA", month, year));
            }

            if (month == 11 || month == 12) {
                boolean hasFolha13 = documentRepo.existsByCompanyAndEmployeeIsNullAndYearAndMonthAndType(company, year, month, DocumentType.FOLHA13);
                if (!hasFolha13) {
                    missingFolhas.add(String.format("%02d/%d - FOLHA13", month, year));
                }
            }
        }

        return missingFolhas;
    }

    @Override
    public Map<String, Double> calculatePresencePercentageByEmployee(Company company) {
        List<Employee> employees = employeeRepo.findByCompanyId(company.getId());
        List<int[]> months = generateMonthInterval();
        Map<String, Double> percentageMap = new HashMap<>();

        for (Employee emp : employees) {
            int expected = 0;
            int found = 0;

            // Verificar a data de admissão
            LocalDate admissionDate = emp.getAdmissionDate();
            if (admissionDate == null) {
                admissionDate = LocalDate.of(2020, 1, 1); // Definir data de admissão como 01/01/2020 se for null
            }

            for (int[] ym : months) {
                int year = ym[0];
                int month = ym[1];
                LocalDate competencia = LocalDate.of(year, month, 1); // Competência do mês

                // Só considerar documentos a partir da data de admissão
                if (competencia.isBefore(admissionDate)) {
                    continue; // Pular meses antes da admissão
                }

                boolean hasVacation = documentRepo.existsByCompanyAndEmployeeAndYearAndMonthAndType(company, emp, year, month, DocumentType.FERIAS);

                if (!hasVacation) {
                    expected++;
                    if (documentRepo.existsByCompanyAndEmployeeAndYearAndMonthAndType(company, emp, year, month, DocumentType.CONTRACHEQUE)) {
                        found++;
                    }
                }

                if (month == 11 || month == 12) {
                    expected++;
                    if (documentRepo.existsByCompanyAndEmployeeAndYearAndMonthAndType(company, emp, year, month, DocumentType.CONTRACHEQUE13)) {
                        found++;
                    }
                }
            }

            double percent = expected == 0 ? 100.0 : (found * 100.0 / expected);
            percentageMap.put(emp.getName(), percent);
        }

        return percentageMap;
    }



    @Override
    public double calculateFolhaPercentageByCompany(Company company) {
        List<int[]> months = generateMonthInterval();
        int expected = 0;
        int found = 0;

        for (int[] ym : months) {
            int year = ym[0];
            int month = ym[1];

            // FOLHA é sempre esperada
            expected++;
            boolean hasFolha = documentRepo.existsByCompanyAndEmployeeIsNullAndYearAndMonthAndType(
                    company, year, month, DocumentType.FOLHA
            );
            if (hasFolha) {
                found++;
            }

            // FOLHA13 esperada nos meses 11 e 12
            if (month == 11 || month == 12) {
                expected++;
                boolean hasFolha13 = documentRepo.existsByCompanyAndEmployeeIsNullAndYearAndMonthAndType(
                        company, year, month, DocumentType.FOLHA13
                );
                if (hasFolha13) {
                    found++;
                }
            }
        }

        return expected == 0 ? 100.0 : (found * 100.0 / expected);
    }




    @Override
    public List<AuditResume> getAuditResumeByEmployee(Company company) {
        Map<String, Double> porcentagens = calculatePresencePercentageByEmployee(company);
        Map<String, List<String>> faltantes = listMissingDocumentsByEmployee(company);

        List<AuditResume> resumo = new ArrayList<>();

        for (String funcionario : porcentagens.keySet()) {
            double porcentagem = porcentagens.getOrDefault(funcionario, 0.0);
            List<String> faltando = faltantes.getOrDefault(funcionario, List.of());
            resumo.add(new AuditResume(funcionario, porcentagem, faltando));
        }

        return resumo;
    }

}

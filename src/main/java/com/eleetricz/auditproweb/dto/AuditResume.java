package com.eleetricz.auditproweb.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class AuditResume {

    private final String name;
    private final double percentage;
    private final List<String> missingDocuments;
    private final String colorClass;

    public AuditResume(String name, double percentage, List<String> missingDocuments) {
        this.name = name;
        this.percentage = percentage;
        this.missingDocuments = missingDocuments;

        if (percentage >= 90) {
            this.colorClass = "bg-green-100 text-green-700";
        } else if (percentage >= 50) {
            this.colorClass = "bg-yellow-100 text-yellow-700";
        } else {
            this.colorClass = "bg-red-100 text-red-700";
        }
    }
}

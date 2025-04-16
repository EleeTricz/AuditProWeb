package com.eleetricz.auditproweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeWithDocumentCount {
    private Long id;
    private String name;
    private Long documentCount;
}

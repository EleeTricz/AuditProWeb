package com.eleetricz.auditproweb.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Document {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = true)
    private Company company;

    @Enumerated(EnumType.STRING)
    private DocumentType type;

    private int year;
    private int month;

}

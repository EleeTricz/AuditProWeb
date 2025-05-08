package com.eleetricz.auditproweb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor @AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    private LocalDate admissionDate;

    @ManyToOne
    @JoinColumn( name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Document> documentos;
}

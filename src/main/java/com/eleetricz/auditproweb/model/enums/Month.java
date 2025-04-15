package com.eleetricz.auditproweb.model.enums;

import lombok.Getter;

@Getter
public enum Month {
    JANEIRO(1, "Janeiro"),
    FEVEREIRO(2, "Fevereiro"),
    MARCO(3, "Março"),
    ABRIL(4, "Abril"),
    MAIO(5, "Maio"),
    JUNHO(6, "Junho"),
    JULHO(7, "Julho"),
    AGOSTO(8, "Agosto"),
    SETEMBRO(9, "Setembro"),
    OUTUBRO(10, "Outubro"),
    NOVEMBRO(11, "Novembro"),
    DEZEMBRO(12, "Dezembro");


    private final int number;
    private final String name;

    Month(int number, String name){
        this.number = number;
        this.name = name;
    }

}

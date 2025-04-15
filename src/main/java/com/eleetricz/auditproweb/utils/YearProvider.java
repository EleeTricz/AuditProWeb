package com.eleetricz.auditproweb.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class YearProvider {

    public static List<Integer> getYears(int start, int end){
        return IntStream.rangeClosed(start, end)
                .boxed()
                .collect(Collectors.toList());
    }
}

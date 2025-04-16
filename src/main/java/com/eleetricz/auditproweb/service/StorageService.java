package com.eleetricz.auditproweb.service;


import org.springframework.core.io.Resource;

import java.io.IOException;

public interface StorageService {
    Resource loadPdf(String companyName, String fileName) throws IOException;
}

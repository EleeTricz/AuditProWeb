package com.eleetricz.auditproweb.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StorageServiceImpl implements StorageService{
    @Value("${storage.pdf.base-path}")
    private String basePath;


    @Override
    public Resource loadPdf(String companyName, String fileName) throws IOException {
        Path resolvedPath = Paths.get(basePath).resolve(companyName).resolve(fileName).normalize();

        if(!resolvedPath.startsWith(Paths.get(basePath))){
            throw new SecurityException("Acesso a esse caminho não permitido.");
        }

        if (!Files.exists(resolvedPath)){
            return null;
        }

        try{
            return new UrlResource(resolvedPath.toUri());
        }catch (MalformedURLException e){
            throw new IOException("Falha ao carregar o PDF.", e);
        }

    }
}

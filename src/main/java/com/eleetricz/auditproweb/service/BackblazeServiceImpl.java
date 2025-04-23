package com.eleetricz.auditproweb.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

@Service
public class BackblazeServiceImpl implements BackblazeService {

    @Value("${backblaze.keyId}")
    private String keyId;

    @Value("${backblaze.applicationKey}")
    private String applicationKey;

    @Value("${backblaze.bucketId}")
    private String bucketId;

    @Value("${backblaze.bucketName}")
    private String bucketName;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private String apiUrl;
    private String authorizationToken;
    private Instant tokenExpiration;

    /**
     * Autentica com a API do Backblaze B2 e salva o token (cache de 24h).
     */
    private void authorize() throws Exception {
        if (authorizationToken != null && tokenExpiration != null && Instant.now().isBefore(tokenExpiration)) {
            return; // Token ainda válido
        }

        String auth = Base64.getEncoder().encodeToString((keyId + ":" + applicationKey).getBytes(StandardCharsets.UTF_8));

        String response = Request.get("https://api.backblazeb2.com/b2api/v2/b2_authorize_account")
                .addHeader("Authorization", "Basic " + auth)
                .execute()
                .returnContent()
                .asString();

        JsonNode json = objectMapper.readTree(response);
        apiUrl = json.get("apiUrl").asText();
        authorizationToken = json.get("authorizationToken").asText();
        tokenExpiration = Instant.now().plusSeconds(23 * 3600); // Renova antes do vencimento
    }

    /**
     * Verifica se o arquivo existe no bucket.
     */
    private boolean fileExists(String fileName) throws Exception {
        authorize();

        String body = objectMapper.createObjectNode()
                .put("bucketId", bucketId)
                .put("prefix", fileName)
                .put("maxFileCount", 1)
                .toString();

        String response = Request.post(apiUrl + "/b2api/v2/b2_list_file_names")
                .addHeader("Authorization", authorizationToken)
                .bodyString(body, ContentType.APPLICATION_JSON)
                .execute()
                .returnContent()
                .asString();

        JsonNode json = objectMapper.readTree(response);
        JsonNode files = json.get("files");

        if (files != null && files.isArray()) {
            for (JsonNode file : files) {
                String foundName = file.get("fileName").asText();
                if (foundName.equals(fileName)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Gera uma URL temporária para download do arquivo privado.
     */
    @Override
    public String generateDownloadUrl(String fileName) throws Exception {
        if (!fileExists(fileName)) {
            throw new Exception("Arquivo não encontrado no bucket: " + fileName);
        }

        long validDuration = 3600; // segundos (1h)

        // Gera um token de download para arquivo privado
        String body = objectMapper.createObjectNode()
                .put("bucketId", bucketId)
                .put("fileNamePrefix", fileName)
                .put("validDurationInSeconds", validDuration)
                .toString();

        String response = Request.post(apiUrl + "/b2api/v2/b2_get_download_authorization")
                .addHeader("Authorization", authorizationToken)
                .bodyString(body, ContentType.APPLICATION_JSON)
                .execute()
                .returnContent()
                .asString();

        JsonNode json = objectMapper.readTree(response);
        String downloadAuthToken = json.get("authorizationToken").asText();

        // Monta a URL final
        return apiUrl + "/file/" + bucketName + "/" + fileName + "?Authorization=" + downloadAuthToken;
    }
}

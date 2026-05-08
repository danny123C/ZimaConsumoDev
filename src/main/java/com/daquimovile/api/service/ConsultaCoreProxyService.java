package com.daquimovile.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ConsultaCoreProxyService {
    // Logger para registrar advertencias en caso de errores HTTP
    private static final Logger logger = LoggerFactory.getLogger(ConsultaCoreProxyService.class);
// Inyección de dependencias para RestClient y ObjectMapper
    private final RestClient restClient;

    private final ObjectMapper objectMapper;
// URL del servicio externo, configurable a través de application.properties con un valor por defecto
    @Value("${zima.external.consultar-url:https://zimaext-backend-dev.coopdaquilema.com/zima-ext/rest/ext/consultar}")
    private String consultarUrl;

    public ConsultaCoreProxyService(RestClient restClient, ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    public ResponseEntity<JsonNode> consultar(JsonNode requestBody) {
        try {   
            logger.info("Request enviado al backend: {}", requestBody.toString());
            ResponseEntity<String> response = restClient.post() //respuesta del backend externo como String para poder manejar casos de body vacíos o no JSON
                    .uri(consultarUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(requestBody.toString())
                    .retrieve()
                    .toEntity(String.class);

            logger.info("Respuesta del backend externo: {}", response.getBody());
            //respuesta correcta, parseamos el body a JsonNode y lo devolvemos junto con el status code
            return ResponseEntity.status(response.getStatusCode())
                    .body(parseBody(response.getBody(), response.getStatusCode().value()));
        } catch (org.springframework.web.client.RestClientResponseException ex) {
            logger.warn("La API externa respondió con error HTTP {}", ex.getStatusCode().value());
            
            return ResponseEntity.status(ex.getStatusCode())
                    .body(parseBody(ex.getResponseBodyAsString(), ex.getStatusCode().value()));
        }
    }

    private JsonNode parseBody(String body, int statusCode) {
        if (body == null || body.isBlank()) {
            ObjectNode emptyResponse = objectMapper.createObjectNode();
            emptyResponse.put("cod", String.valueOf(statusCode));
            emptyResponse.put("msg", "Sin respuesta del servicio externo");
            logger.info("Body vacío, devolviendo: {}", emptyResponse);
            return emptyResponse;
        }

        try {
            JsonNode parsed = objectMapper.readTree(body);
            logger.info("Body parseado correctamente: {}", parsed);
            return parsed;
        } catch (Exception ex) {
            logger.error("Error al parsear body: {}", body, ex);
            ObjectNode fallback = objectMapper.createObjectNode();
            fallback.put("cod", String.valueOf(statusCode));
            fallback.put("msg", body);
            return fallback;
        }
    }
}
package com.daquimovile.api.service.ExternalServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import com.daquimovile.api.exception.BusinessException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.MediaType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;


@Service
public class OctopusProxyService {

    private final RestClient restClient;
    private static final Logger logger = LoggerFactory.getLogger(OctopusProxyService.class);

    @Value("${octopus.url.base}")
    private String baseUrl;

    @Value("${octopus.header.name}")
    private String headerName;

    @Value("${octopus.sw.apikey.zima}")
    private String apiKeyZima;

    public OctopusProxyService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public ResponseEntity<JsonNode> consumirOctopus(String endpoint, Object body) {
        String urlFinal = baseUrl + endpoint;
        
        // Log preventivo para debug: ayuda a ver qué se envía antes de que falle
        logger.info("Consumiendo Octopus - URL: {} | Header: {}", urlFinal, headerName);

        try {
            return restClient.post()
                    .uri(urlFinal)
                    .header(headerName, apiKeyZima)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    // El intercambio a JsonNode es dinámico, acepta cualquier respuesta de Octopus
                    .toEntity(JsonNode.class);
                    
        } catch (HttpClientErrorException e) {
            // Este bloque captura errores 4xx (como el 401 que tuvimos al inicio)
            logger.error("Error de cliente al llamar a Octopus ({}): {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw e; // Se lanza para que el GlobalExceptionHandler lo capture
            
        } catch (ResourceAccessException e) {
            // Este captura errores de red o Timeouts (si el servidor está caído)
            logger.error("No se pudo conectar con Octopus en {}: {}", urlFinal, e.getMessage());
            throw e; // Se lanza para que el GlobalExceptionHandler active el error 504
        }
    }
}
package com.daquimovile.api.service.ExternalServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
@Service
public class ConsultaCoreProxyService {

    private static final Logger logger = LoggerFactory.getLogger(ConsultaCoreProxyService.class);
    private final RestClient restClient;

    public ConsultaCoreProxyService(RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * Procesa cualquier petición POST al Core de forma dinámica.
     * @param url URL completa del servicio (Login, Consultar, etc.)
     * @param body Objeto que se enviará en el cuerpo (DTO o JsonNode)
     */
    public ResponseEntity<JsonNode> procesarPeticion(String url, Object body) {
        logger.info("Iniciando petición dinámica a: {}", url);

        try {
            return restClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .toEntity(JsonNode.class);//aca se hace la peticion al core, y se espera una respuesta que se parsea a JsonNode, y se devuelve como ResponseEntity<JsonNode>
        } catch (HttpClientErrorException ex) {
            logger.warn("Error HTTP en el Core: {} - Body: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw ex; 
        } catch (ResourceAccessException ex) {
            logger.error("Error de conexión o Timeout con el Core en: {}", url);
            throw ex;
        }
    }
}
/**    
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

    */

    
package com.daquimovile.api.service.ConsultasOctopusMetodos;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.daquimovile.api.dto.OctopusDto.OctopusRequest.OctopusClienteRequestDto;
import com.daquimovile.api.exception.BusinessException;
import com.daquimovile.api.service.ExternalServices.OctopusProxyService;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OctopusServiceImpl implements IOctopusService {

    private final OctopusProxyService octopusProxy;

    @Override
    public JsonNode obtenerDatosCliente(OctopusClienteRequestDto request) {
        // 1. Llamada técnica al Proxy
        // Usamos el endpoint real que ya probamos
        ResponseEntity<JsonNode> respuesta = octopusProxy.consumirOctopus("/usuario/datoscliente", request);
        
        JsonNode body = respuesta.getBody();

        // 2. Validación de Reglas de Negocio
        // Si Octopus responde con éxito técnico (200 OK) pero éxito lógico fallido (coderror != 200)
        if (body != null && body.has("coderror") && !"200".equals(body.get("coderror").asText())) {
            String mensajeParaUsuario = body.has("msgusu") ? body.get("msgusu").asText() : "Error en plataforma Octopus";
            
            // Lanzamos nuestra excepción personalizada que el GlobalExceptionHandler atrapará
            throw new BusinessException(mensajeParaUsuario);
        }
        
        // Si todo está bien, devolvemos el nodo "data" o el body completo según necesites
        return body;
    }
}


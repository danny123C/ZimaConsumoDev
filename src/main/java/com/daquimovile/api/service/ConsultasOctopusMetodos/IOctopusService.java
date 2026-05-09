package com.daquimovile.api.service.ConsultasOctopusMetodos;

import com.daquimovile.api.dto.OctopusDto.OctopusRequest.OctopusClienteRequestDto;
import com.fasterxml.jackson.databind.JsonNode;
public interface IOctopusService {
    // Ahora recibe el DTO específico en lugar de un Object genérico
    JsonNode obtenerDatosCliente(OctopusClienteRequestDto request);
}
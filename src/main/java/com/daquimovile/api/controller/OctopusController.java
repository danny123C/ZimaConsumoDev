package com.daquimovile.api.controller;

import com.daquimovile.api.dto.ResponseDto;
import com.daquimovile.api.dto.OctopusDto.OctopusRequest.OctopusClienteRequestDto;
import com.daquimovile.api.mapper.ConsultaCoreMapper; // Reutilizamos tu mapper para estandarizar
import com.daquimovile.api.service.ConsultasOctopusMetodos.IOctopusService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/octopus")
@RequiredArgsConstructor
public class OctopusController {

    private final IOctopusService octopusService;
    private final ConsultaCoreMapper mapper;

    @PostMapping(value = "/consultar-cliente", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> consultarCliente(@RequestBody OctopusClienteRequestDto request) {
        
        // El servicio ya valida errores internos, aquí solo recibimos el éxito
        JsonNode resultado = octopusService.obtenerDatosCliente(request);

        return ResponseEntity.ok(mapper.fromJsonNode(200, "Cliente encontrado en Octopus", resultado));
    }
}
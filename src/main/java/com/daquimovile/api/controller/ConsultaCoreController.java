package com.daquimovile.api.controller;

import com.daquimovile.api.dto.ConsultaCoreInputDto;

import com.daquimovile.api.dto.ResponseDto;
import com.daquimovile.api.mapper.ConsultaCoreMapper;
import com.daquimovile.api.service.ConsultaCoreProxyService;
import com.daquimovile.api.service.ConsultaCoreMetodos.IConsultaCoreService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/core")
public class ConsultaCoreController {

        //Mapper para convertir entre DTOs y JsonNode
        private final ConsultaCoreMapper mapper;
        private final IConsultaCoreService coreService;

        public ConsultaCoreController(
                ConsultaCoreProxyService consultaCoreProxyService, 
                ConsultaCoreMapper mapper, 
                ObjectMapper objectMapper, 
                IConsultaCoreService coreService) {
                
                this.mapper = mapper;
                this.coreService = coreService;
        }

    @Operation(
            summary = "Consulta al backend externo",
            description = "Recibe solo cpersona, construye internamente el JSON completo y lo reenvía al servicio externo."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Respuesta exitosa del backend externo con datos completos",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResponseDto.class)
            )
    )
   @PostMapping(value = "/consultar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> consultar(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "ID de la persona a consultar",
            content = @Content(schema = @Schema(implementation = ConsultaCoreInputDto.class))
        ) 
        @RequestBody ConsultaCoreInputDto inputDto) {
        
        JsonNode resultado = coreService.obtenerResumenSimplificado(inputDto.getCpersona());

        if (resultado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDto(404, "No se encontró cuenta activa o el servicio externo falló", null));
        }

        return ResponseEntity.ok(mapper.fromJsonNode(200, "Consulta exitosa", resultado));
    }
}
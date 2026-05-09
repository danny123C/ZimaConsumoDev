package com.daquimovile.api.dto.OctopusDto.OctopusRequest;

import io.swagger.v3.oas.annotations.media.Schema;

public class OctopusClienteRequestDto {
    
    @Schema(description = "Número de identificación del cliente", example = "0604055566")
    private String identificacion;

    // Getters y Setters
    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }
}
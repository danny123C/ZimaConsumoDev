package com.daquimovile.api.dto;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;

public class ResponseDto {
    @Schema(description = "Código de respuesta HTTP", example = "200")
    private int status;
    
    @Schema(description = "Mensaje de la respuesta", example = "Success")
    private String message;
    
    @Schema(description = "Datos de la respuesta del backend externo con toda la información solicitada (LOVOPERVISTA, etc.)", type = "object")
    private JsonNode payload;

    public ResponseDto() {}

    public ResponseDto(int status, String message, JsonNode payload) {
        this.status = status;
        this.message = message;
        this.payload = payload;
    }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public JsonNode getPayload() { return payload; }
    public void setPayload(JsonNode payload) { this.payload = payload; }
}

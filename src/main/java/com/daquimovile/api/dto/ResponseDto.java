package com.daquimovile.api.dto;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Contenedor universal de respuestas para la API")
public class ResponseDto<T> {
@Schema(description = "Indica si la operación fue exitosa", example = "true")
    private boolean ok; // <--- El nuevo estándar
    @Schema(description = "Código de estado lógico (ej: 200, 400, 500)", example = "200")
    private int status;
    
    @Schema(description = "Mensaje informativo para el usuario o desarrollador", example = "Operación exitosa")
    private String message;
    
    @Schema(description = "Carga útil de la respuesta. Puede ser cualquier objeto o lista.")
    private T payload;

    /**
     * Método estático de conveniencia para respuestas exitosas rápidas
     */
    public static <T> ResponseDto<T> success(String message, T payload) {
        return new ResponseDto<>(true, 200, message, payload);
    }
// Error Dinámico: Tú eliges el status (400, 401, 404, 500, etc.)
public static <T> ResponseDto<T> error(int status, String message) {
    return new ResponseDto<>(false, status, message, null);
}

// Opcional: Error rápido para flojera (asume 500)
public static <T> ResponseDto<T> error(String message) {
    return new ResponseDto<>(false, 500, message, null);
}
}


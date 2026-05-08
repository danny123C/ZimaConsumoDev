package com.daquimovile.api.service.ConsultaCoreMetodos;
import com.fasterxml.jackson.databind.JsonNode;

public interface IConsultaCoreService {
    /**
     * Realiza la lógica de obtener el resumen simplificado (pk, nombre, saldo)
     * para una persona específica.
     */
    JsonNode obtenerResumenSimplificado(Long cpersona);
}
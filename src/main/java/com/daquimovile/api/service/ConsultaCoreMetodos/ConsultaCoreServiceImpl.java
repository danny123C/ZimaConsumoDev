package com.daquimovile.api.service.ConsultaCoreMetodos;


import com.daquimovile.api.dto.RequestDto;
import com.daquimovile.api.exception.BusinessException;
import com.daquimovile.api.service.ExternalServices.ConsultaCoreProxyService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ConsultaCoreServiceImpl implements IConsultaCoreService {

    private final ConsultaCoreProxyService proxyService;
    private final ObjectMapper objectMapper;

    // El servicio ahora es dueño de su URL
    @Value("${zima.external.consultar-url}")
    private String consultarUrl;

    // Constantes de configuración
    private static final String DEFAULT_U = "101";
    private static final int DEFAULT_M = 5;
    private static final int DEFAULT_T = 1000;
    private static final String DEFAULT_CSISTEMAEXTERNO = "EXT";
    private static final int DEFAULT_CROL = 64;
    private static final String DEFAULT_BEAN = "TvisOperacion";
    private static final String DEFAULT_LISTA = "Y";
    private static final String DEFAULT_ORDERBY = "t.pk";
    private static final int DEFAULT_PAGINA = 0;
    private static final int DEFAULT_CANTIDAD = 6;

    @Override
    public JsonNode obtenerResumenSimplificado(Long cpersona) {
        // 1. Obtener lista de productos
        RequestDto requestDto = buildRequest(cpersona);
        
        // Llamada dinámica pasando la URL de consulta
        var resp = proxyService.procesarPeticion(consultarUrl, objectMapper.valueToTree(requestDto));
        
        // 2. Filtrar el PK activo
        JsonNode soloProductosDos = filtrarPorProductoYEstatus(resp.getBody(), 2, "ACT");
        String pkExtraido = extraerPkPrincipal(soloProductosDos);

        if (pkExtraido.isEmpty()) {
            throw new BusinessException("No se encontró una cuenta de ahorros activa para el socio: " + cpersona);
        }

        // 3. Consultar Detalle
        ObjectNode requestDetalleBody = armarRequestDetalle(pkExtraido);
        
        // Segunda llamada dinámica
        var respDetalle = proxyService.procesarPeticion(consultarUrl, requestDetalleBody);

        if (respDetalle.getStatusCode().is2xxSuccessful() && respDetalle.getBody() != null) {
            JsonNode detalleBody = respDetalle.getBody();
            
            ObjectNode respuestaSimplificada = objectMapper.createObjectNode();
            respuestaSimplificada.put("coperacion", extraerPkDesdeDetalle(detalleBody));
            respuestaSimplificada.put("nombre", extraerNombreDesdeDetalle(detalleBody));
            respuestaSimplificada.put("saldo", extraerSaldoEfectivo(detalleBody));
            
            return respuestaSimplificada;
        }

        throw new BusinessException("Error al obtener el detalle de la cuenta: " + pkExtraido);
    }

    // --- MÉTODOS AUXILIARES ---

    private RequestDto buildRequest(Long cpersona) {
        RequestDto requestDto = new RequestDto();
        requestDto.setU(DEFAULT_U);
        requestDto.setM(DEFAULT_M);
        requestDto.setT(DEFAULT_T);
        requestDto.setCsistemaexterno(DEFAULT_CSISTEMAEXTERNO);
        requestDto.setCrol(DEFAULT_CROL);

        RequestDto.LovOpPerVista lov = new RequestDto.LovOpPerVista();
        lov.setBean(DEFAULT_BEAN);
        lov.setLista(DEFAULT_LISTA);
        lov.setOrderby(DEFAULT_ORDERBY);
        lov.setPagina(DEFAULT_PAGINA);
        lov.setCantidad(DEFAULT_CANTIDAD);

        RequestDto.Filtro filtro = new RequestDto.Filtro();
        filtro.setCampo("cpersona");
        filtro.setValor(cpersona);
        lov.setFiltro(List.of(filtro));

        requestDto.setLOVOPERVISTA(lov);
        return requestDto;
    }

    private JsonNode filtrarPorProductoYEstatus(JsonNode body, int tipo, String estatus) {
        ArrayNode filtrados = objectMapper.createArrayNode();
        if (body != null && body.has("LOVOPERVISTA")) {
            ArrayNode original = (ArrayNode) body.get("LOVOPERVISTA");
            for (JsonNode item : original) {
                if (item.get("cproducto").asInt() == tipo && item.get("cestatus").asText().equals(estatus)) {
                    filtrados.add(item);
                }
            }
        }
        return filtrados;
    }

    private String extraerPkPrincipal(JsonNode lista) {
        if (lista != null && lista.isArray() && !lista.isEmpty()) {
            return lista.get(0).path("pk").asText();
        }
        return "";
    }

    private ObjectNode armarRequestDetalle(String pk) {
        ObjectNode body = objectMapper.createObjectNode();
        body.put("u", DEFAULT_U);
        body.put("m", DEFAULT_M);
        body.put("t", DEFAULT_T);
        body.put("crol", DEFAULT_CROL);
        body.put("CODIGOCONSULTA", "CONSULTAOPERACIONVISTA");
        body.put("coperacion", pk);
        body.put("mesessaldopromedio", 5);
        return body;
    }

    private double extraerSaldoEfectivo(JsonNode body) {
        return body.path("SALDOS").path(0).path("efe").asDouble(0.0);
    }

    private String extraerPkDesdeDetalle(JsonNode body) {
        return body.path("OPERACION").path(0).path("pk").asText("");
    }

    private String extraerNombreDesdeDetalle(JsonNode body) {
        return body.path("OPERACION").path(0).path("nombre").asText("NOMBRE NO ENCONTRADO");
    }
}
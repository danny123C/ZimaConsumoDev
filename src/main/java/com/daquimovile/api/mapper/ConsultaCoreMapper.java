package com.daquimovile.api.mapper;

import com.daquimovile.api.dto.RequestDto;
import com.daquimovile.api.dto.ResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Component;

@Component
public class ConsultaCoreMapper {

    private final ObjectMapper objectMapper;

    public ConsultaCoreMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public JsonNode toJsonNode(RequestDto dto) {
        // Permite incluir valores null en la serialización
        ObjectMapper mapperWithNulls = objectMapper.copy();
        mapperWithNulls.setDefaultPropertyInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS);
        return mapperWithNulls.valueToTree(dto);
    }

    public ResponseDto fromJsonNode(int status, String message, JsonNode node) {
        return new ResponseDto(status, message, node);
    }
}

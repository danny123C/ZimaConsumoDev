package com.daquimovile.api.mapper;

import com.daquimovile.api.dto.RequestDto;
import com.daquimovile.api.dto.ResponseDto;
import com.daquimovile.api.dto.ZimaDto.ZimaRespose.LoginResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

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


public <T> ResponseDto<T> fromJsonNode(int status, String message, T payload) {
    // Si el status es 2xx, ok es true, de lo contrario false
    boolean isOk = (status >= 200 && status < 300);
    return new ResponseDto<>(isOk, status, message, payload);
}
    public LoginResponseDto toLoginResponse(JsonNode node) {
        JsonNode mradicacion = node.path("mradicacion");
        
        // Extraer y mapear la lista de roles
        List<LoginResponseDto.RolDto> rolesList = new ArrayList<>();
        JsonNode rolesNode = mradicacion.path("roles");
        
        if (rolesNode.isArray()) {
            for (JsonNode rol : rolesNode) {
                rolesList.add(LoginResponseDto.RolDto.builder()
                        .id(rol.path("id").asText())
                        .nombre(rol.path("name").asText())
                        .build());
            }
        }

        return LoginResponseDto.builder()
                .token(node.path("token").asText())
                .cpersona(mradicacion.path("cp").asLong())
                .nombreSocio(mradicacion.path("np").asText())
                .usuario(mradicacion.path("cu").asText())
                .agencia(mradicacion.path("age").asText())
                .roles(rolesList) // Seteamos la lista procesada
                .build();
    }
}
package com.daquimovile.api.service.ConsultaCoreMetodos.AuthServices;

import com.daquimovile.api.dto.ZimaDto.ZimaRequest.LoginRequestDto;
import com.fasterxml.jackson.databind.JsonNode;


public interface IAuthService {
    /**
     * Autentica al usuario en el Core de Zima.
     * @param loginRequest DTO con usuario y password (hash).
     * @return JsonNode con la información del perfil del socio.
     */
    JsonNode login(LoginRequestDto loginRequest);
}
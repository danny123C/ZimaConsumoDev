package com.daquimovile.api.controller;


import com.daquimovile.api.dto.ResponseDto;
import com.daquimovile.api.dto.ZimaDto.ZimaRequest.LoginRequestDto;
import com.daquimovile.api.dto.ZimaDto.ZimaRespose.LoginResponseDto;
import com.daquimovile.api.mapper.ConsultaCoreMapper;
import com.daquimovile.api.service.ConsultaCoreMetodos.AuthServices.IAuthService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor // Esto genera el constructor automáticamente para los campos final
public class AuthController {

    private final IAuthService authService;
    private final ConsultaCoreMapper mapper;

    /**
     * Endpoint de autenticación para la App Móvil.
     * URL: http://localhost:8080/api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        
        // 1. Llamamos al servicio (Obtenemos el JSON crudo del Core)
        JsonNode dataSocioRaw = authService.login(loginRequest);
        
        // 2. Mapeamos a un DTO simplificado (Filtramos solo lo que Flutter necesita)
        LoginResponseDto filterDataLogin = mapper.toLoginResponse(dataSocioRaw);

        // 3. Devolvemos la respuesta estandarizada mediante ResponseDto
        return ResponseEntity.ok(ResponseDto.success("Autenticación exitosa", filterDataLogin));
    }
}
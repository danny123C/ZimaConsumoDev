package com.daquimovile.api.service.ConsultaCoreMetodos.AuthServices;


import com.daquimovile.api.dto.ZimaDto.ZimaRequest.LoginRequestDto;
import com.daquimovile.api.exception.BusinessException;
import com.daquimovile.api.service.ExternalServices.ConsultaCoreProxyService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final ConsultaCoreProxyService proxyService;

    // URL específica para login definida en application.properties
    @Value("${zima.external.login-url}")
    private String loginUrl;

@Override
public JsonNode login(LoginRequestDto loginRequest) {
    // 1. Llamada al Proxy Genérico
    ResponseEntity<JsonNode> response = proxyService.procesarPeticion(loginUrl, loginRequest);
    
    JsonNode body = response.getBody();

    // 2. Validación de respuesta nula
    if (body == null) {
        throw new BusinessException("No se recibió respuesta del servidor de autenticación.");
    }

    // 3. Validación de errores explícitos del Core (coderror != 200)
    if (body.has("coderror") && !"200".equals(body.get("coderror").asText())) {
        String mensajeCore = body.path("msgusu").asText("Credenciales incorrectas");
        throw new BusinessException(mensajeCore);
    }

    // 4. VALIDACIÓN DE SEGURIDAD: Verificar si el token existe y es válido
    // Si el token viene vacío o es "null", las credenciales son incorrectas
    String token = body.path("token").asText("");
    if (token.isEmpty() || token.equalsIgnoreCase("null")) {
        throw new BusinessException("Usuario o contraseña incorrectos.");
    }

    // 5. Validación adicional: Verificar que contenga datos del socio (mradicacion)
    // Esto evita que devuelvas un objeto exitoso sin datos reales
    if (!body.has("mradicacion") || body.path("mradicacion").isMissingNode()) {
        throw new BusinessException("Error al recuperar el perfil del socio. Intente nuevamente.");
    }

    return body;
}
}


package com.daquimovile.api.dto.ZimaDto.ZimaRespose;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class LoginResponseDto {
    private String token;
    private Long cpersona;      
    private String nombreSocio; 
    private String usuario;     
    private String agencia;    
    private List<RolDto> roles; // Nueva lista de roles

    @Data
    @Builder
    public static class RolDto {
        private String id;
        private String nombre;
    }
}
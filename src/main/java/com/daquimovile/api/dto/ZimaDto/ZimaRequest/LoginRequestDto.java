package com.daquimovile.api.dto.ZimaDto.ZimaRequest;

public class LoginRequestDto {
    private String u; // Usuario
    private String p; // Password (Hash SHA-256)

    // Getters y Setters
    public String getU() { return u; }
    public void setU(String u) { this.u = u; }
    public String getP() { return p; }
    public void setP(String p) { this.p = p; }
}
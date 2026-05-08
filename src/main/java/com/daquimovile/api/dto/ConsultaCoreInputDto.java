package com.daquimovile.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class ConsultaCoreInputDto {

    @Schema(description = "Identificador de la persona a consultar", example = "44762")
    private Long cpersona;
    //getter
    public Long getCpersona() {
        return cpersona;
    }
//setter
    public void setCpersona(Long cpersona) {
        this.cpersona = cpersona;
    }
}
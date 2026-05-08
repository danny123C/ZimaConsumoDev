package com.daquimovile.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class RequestDto {
    private String u;
    private Integer m;
    private Integer t;
    private String csistemaexterno;
    private Integer crol;
    
    @JsonProperty("LOVOPERVISTA")
    private LovOpPerVista LOVOPERVISTA;

    public String getU() { return u; }
    public void setU(String u) { this.u = u; }
    public Integer getM() { return m; }
    public void setM(Integer m) { this.m = m; }
    public Integer getT() { return t; }
    public void setT(Integer t) { this.t = t; }
    public String getCsistemaexterno() { return csistemaexterno; }
    public void setCsistemaexterno(String csistemaexterno) { this.csistemaexterno = csistemaexterno; }
    public Integer getCrol() { return crol; }
    public void setCrol(Integer crol) { this.crol = crol; }
    @JsonProperty("LOVOPERVISTA")
    public LovOpPerVista getLOVOPERVISTA() { return LOVOPERVISTA; }

    @JsonProperty("LOVOPERVISTA")
    public void setLOVOPERVISTA(LovOpPerVista lOVOPERVISTA) { LOVOPERVISTA = lOVOPERVISTA; }

    public static class LovOpPerVista {
        private String bean;
        private String lista;
        private String orderby;
        private Integer pagina;
        private Integer cantidad;
        private List<Filtro> filtro;

        public String getBean() { return bean; }
        public void setBean(String bean) { this.bean = bean; }
        public String getLista() { return lista; }
        public void setLista(String lista) { this.lista = lista; }
        public String getOrderby() { return orderby; }
        public void setOrderby(String orderby) { this.orderby = orderby; }
        public Integer getPagina() { return pagina; }
        public void setPagina(Integer pagina) { this.pagina = pagina; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
        public List<Filtro> getFiltro() { return filtro; }
        public void setFiltro(List<Filtro> filtro) { this.filtro = filtro; }
    }
//esto se usa para filtrar por CPersona
// s
    public static class Filtro {
        private String campo;
        private Long valor;
        public String getCampo() { return campo; }
        public void setCampo(String campo) { this.campo = campo; }
        public Long getValor() { return valor; }
        public void setValor(Long valor) { this.valor = valor; }
    }
}

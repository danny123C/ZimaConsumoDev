package com.daquimovile.api.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import java.util.HashMap;
import java.util.Map;

public class ConsultaResponseDto {
    private Map<String, Object> data = new HashMap<>();

    public ConsultaResponseDto() {}

    @JsonAnySetter
    public void addProperty(String key, Object value) {
        data.put(key, value);
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public Object get(String key) {
        return data.get(key);
    }
}

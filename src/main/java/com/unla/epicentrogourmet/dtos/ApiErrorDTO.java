package com.unla.epicentrogourmet.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ApiErrorDTO {

    private int status;

    private String mensaje;

    private List<String> detalles;

}

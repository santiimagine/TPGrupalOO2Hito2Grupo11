package com.unla.epicentrogourmet.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class UnidadVentaDTO {
    private Integer id;

    private String nombreComercial;

    private String tipo;

    private String codigo;

    private Double superficie;

    private String responsableMail;
}
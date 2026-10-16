package com.unla.epicentrogourmet.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AsignacionDTO {
    private String festival;
    private String unidadVenta;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private boolean activo;
    private Integer festivalId;
}
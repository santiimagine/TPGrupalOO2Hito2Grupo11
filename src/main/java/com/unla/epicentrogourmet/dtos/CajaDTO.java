package com.unla.epicentrogourmet.dtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CajaDTO {
    private Integer id;
    private LocalDate fecha;
    private String unidadVenta;
    private double montoTotal;
    private String estado;
}
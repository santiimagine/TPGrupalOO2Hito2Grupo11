package com.unla.epicentrogourmet.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReporteMayoresCanonDTO {
    private String nombreComercial;

    private String codigo;

    private String tipoUnidad;

    private double canon;
}

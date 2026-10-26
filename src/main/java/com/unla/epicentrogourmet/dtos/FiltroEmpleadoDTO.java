package com.unla.epicentrogourmet.dtos;


import com.unla.epicentrogourmet.entities.enums.TipoEmpleado;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class FiltroEmpleadoDTO {

    private String texto;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaDesde;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaHasta;
    private TipoEmpleado tipoEmpleado;
    private Integer unidadVentaId;

    public boolean estaVacio(){
        return ((texto == null || texto.isBlank()) && fechaDesde == null && fechaHasta == null && tipoEmpleado == null && unidadVentaId == null);
    }

}

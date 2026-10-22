package com.unla.epicentrogourmet.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ResumenFestivalDTO {

    private int cantidadUnidades;

    private long cantidadPedidos;

    private double canonTotal;

    private double recaudacionTotal;
}

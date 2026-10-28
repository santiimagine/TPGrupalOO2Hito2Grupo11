package com.unla.epicentrogourmet.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemPedidoFormDTO {

    private Integer platoId;
    private String nombre;
    private double precio;
    private Integer cantidad = 0;

}

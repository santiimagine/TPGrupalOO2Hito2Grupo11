package com.unla.epicentrogourmet.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PedidoFormDTO {

    private String unidadVenta;
    private String festival;
    private List<ItemPedidoFormDTO> items = new ArrayList<>();

}

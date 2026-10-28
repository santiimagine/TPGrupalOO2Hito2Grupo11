package com.unla.epicentrogourmet.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public class PedidoResumenDTO {

    private Integer id;
    private List<String> detalle = new ArrayList<>();
    private double total;

}

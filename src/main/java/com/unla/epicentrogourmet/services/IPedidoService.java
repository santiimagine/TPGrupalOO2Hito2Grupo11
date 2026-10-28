package com.unla.epicentrogourmet.services;

import com.unla.epicentrogourmet.dtos.PedidoFormDTO;
import com.unla.epicentrogourmet.entities.Pedido;

public interface IPedidoService {

    PedidoFormDTO armarFormulario(String mailEmpleado);

    Pedido registrar(PedidoFormDTO pedidoFormDTO, String mailEmpleado);

}

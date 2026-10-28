package com.unla.epicentrogourmet.services;

import com.unla.epicentrogourmet.dtos.PedidoFormDTO;
import com.unla.epicentrogourmet.dtos.PedidoResumenDTO;
import com.unla.epicentrogourmet.entities.Pedido;

import java.util.List;

public interface IPedidoService {

    PedidoFormDTO armarFormulario(String mailEmpleado);

    Pedido registrar(PedidoFormDTO pedidoFormDTO, String mailEmpleado);

    List<PedidoResumenDTO> traerPedidosDeHoy(String mailEmpleado);

}

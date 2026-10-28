package com.unla.epicentrogourmet.services.implementation;

import com.unla.epicentrogourmet.dtos.ItemPedidoFormDTO;
import com.unla.epicentrogourmet.dtos.PedidoFormDTO;
import com.unla.epicentrogourmet.entities.*;
import com.unla.epicentrogourmet.repositories.IEmpleadoRepository;
import com.unla.epicentrogourmet.repositories.IPedidoRepository;
import com.unla.epicentrogourmet.repositories.IPlatoRepository;
import com.unla.epicentrogourmet.services.ICajaService;
import com.unla.epicentrogourmet.services.IPedidoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("pedidoService")
public class PedidoService implements IPedidoService {

    private final IEmpleadoRepository empleadoRepository;
    private final IPlatoRepository platoRepository;
    private final IPedidoRepository pedidoRepository;
    private final ICajaService cajaService;

    public PedidoService(IEmpleadoRepository empleadoRepository, IPlatoRepository platoRepository, IPedidoRepository pedidoRepository, ICajaService cajaService) {
        this.empleadoRepository = empleadoRepository;
        this.platoRepository = platoRepository;
        this.pedidoRepository = pedidoRepository;
        this.cajaService = cajaService;
    }

    @Override
    @Transactional(readOnly = true)
    public PedidoFormDTO armarFormulario(String mailEmpleado) {

        Cajero cajero = traerCajero(mailEmpleado);
        UnidadVenta unidadVenta = traerUnidadEnCurso(cajero);

        PedidoFormDTO dto = new PedidoFormDTO();
        dto.setUnidadVenta(unidadVenta.getNombreComercial());
        dto.setFestival(unidadVenta.getFestival().getNombre());

        for (Plato plato : platoRepository.filtrar(unidadVenta.getId(), null, null, null)){
            ItemPedidoFormDTO item = new ItemPedidoFormDTO();
            item.setPlatoId(plato.getId());
            item.setNombre(plato.getNombre());
            item.setPrecio(plato.getPrecioVenta());
            dto.getItems().add(item);
        }

        return dto;

    }

    @Override
    @Transactional
    public Pedido registrar(PedidoFormDTO pedidoFormDTO, String mailEmpleado) {

        Cajero cajero = traerCajero(mailEmpleado);
        UnidadVenta unidadVenta = traerUnidadEnCurso(cajero);

        Caja caja = cajaService.traerOAbrirCajaDelDia(cajero, unidadVenta);
        if (!caja.estaAbierta())
            throw new IllegalArgumentException("No pueden agregarse pedidos a una caja cerrada.");

        Set<ItemPedido> items = new HashSet<>();
        for (ItemPedidoFormDTO item : pedidoFormDTO.getItems()){
            if (item.getCantidad() == 0 || item.getCantidad() == null)
                continue;
            if (item.getCantidad() < 0)
                throw new IllegalArgumentException("Las cantidades no pueden ser negativas.");

            Plato plato = platoRepository.buscarEnUnidad(unidadVenta.getId(), item.getPlatoId())
                    .filter(Plato::isActivo)
                    .orElseThrow(() -> new IllegalArgumentException("Uno de los platos no está en el menú de la Unidad de Venta."));

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setPlato(plato);
            itemPedido.setCantidad(item.getCantidad());
            itemPedido.setPrecioUnitario(plato.getPrecioVenta());

            items.add(itemPedido);

        }

        if (items.isEmpty())
            throw new IllegalArgumentException("El pedido tiene que tener al menos un plato.");

        Pedido pedido = new Pedido();
        pedido.setFecha(LocalDate.now());
        pedido.setFestival(unidadVenta.getFestival());
        pedido.setUnidadVenta(unidadVenta);
        pedido.setItems(items);
        pedidoRepository.save(pedido);

        caja.registrarVenta(pedido.calcularTotal());

        return pedido;

    }

    private Cajero traerCajero(String mail) {

        Empleado empleado = empleadoRepository.traerPorMail(mail)
                .orElseThrow(() -> new IllegalArgumentException("No hay un empleado con ese mail."));

        if (!(empleado instanceof Cajero cajero))
            throw new IllegalArgumentException("Solo los cajeros pueden registrar pedidos.");

        if (!(cajero.isActivo()))
            throw  new IllegalArgumentException("No se puede registrar pedidos con usuarios dados de baja.");

        return cajero;
    }

    private UnidadVenta traerUnidadEnCurso(Cajero cajero){

        List<UnidadVenta> unidades = empleadoRepository.traerUnidadesEnCurso(cajero.getId(), LocalDate.now());
        if (unidades.isEmpty())
            throw new IllegalArgumentException("No fuiste asignado a ningún festival en curso.");

        return unidades.get(0);

    }

}

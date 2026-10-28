package com.unla.epicentrogourmet.services.implementation;

import com.unla.epicentrogourmet.dtos.CajaDTO;
import com.unla.epicentrogourmet.entities.Cajero;
import com.unla.epicentrogourmet.entities.UnidadVenta;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unla.epicentrogourmet.entities.Caja;
import com.unla.epicentrogourmet.entities.enums.EstadoCaja;
import com.unla.epicentrogourmet.services.ICajaService;
import com.unla.epicentrogourmet.repositories.ICajaRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CajaService implements ICajaService {

    private final ICajaRepository cajaRepository;

    public CajaService(ICajaRepository cajaRepository) {
        this.cajaRepository = cajaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Caja traerCajaActual(Integer cajeroId) {
        return cajaRepository.findFirstByCajeroIdAndEstadoOrderByFechaDescIdDesc(cajeroId, EstadoCaja.ABIERTA)
                .or(() -> cajaRepository.findFirstByCajeroIdOrderByFechaDescIdDesc(cajeroId))
                .orElse(null);
    }

    @Override
    @Transactional
    public void cerrarCaja(Integer cajaId, Integer cajeroId) {
        Caja caja = cajaRepository.findById(cajaId)
                .orElseThrow(() -> new IllegalArgumentException("Caja inexistente"));

        if (!caja.getCajero().getId().equals(cajeroId)) {
            throw new AccessDeniedException("Esta caja no te pertenece");
        }
        if (caja.getEstado() == EstadoCaja.CERRADA) {
            return;
        }
        caja.setEstado(EstadoCaja.CERRADA);
        cajaRepository.save(caja);
    }

    @Override
    @Transactional
    public Caja traerOAbrirCajaDelDia(Cajero cajero, UnidadVenta unidadVenta) {

        return cajaRepository.findFirstByCajeroIdAndUnidadVentaIdAndFecha(cajero.getId(), unidadVenta.getId(), LocalDate.now())
                .orElseGet(() -> {

                    for (Caja cajaVieja : cajaRepository.findByCajeroIdAndEstadoAndFechaBefore(cajero.getId(), EstadoCaja.ABIERTA, LocalDate.now())){
                        cajaVieja.setEstado(EstadoCaja.CERRADA);
                    }

                    Caja caja = new Caja();
                    caja.setFecha(LocalDate.now());
                    caja.setCajero(cajero);
                    caja.setUnidadVenta(unidadVenta);
                    caja.setEstado(EstadoCaja.ABIERTA);
                    caja.setMontoTotal(0);
                    return cajaRepository.save(caja);
                });

    }

    @Override
    @Transactional(readOnly = true)
    public List<CajaDTO> traerHistorial(Integer cajeroId) {

        List<CajaDTO> resultado = new ArrayList<>();

        for (Caja caja : cajaRepository.findByCajeroIdOrderByFechaDescIdDesc(cajeroId)){
            CajaDTO dto = new CajaDTO();
            dto.setId(caja.getId());
            dto.setFecha(caja.getFecha());
            dto.setUnidadVenta(caja.getUnidadVenta().getNombreComercial() + " (" + caja.getUnidadVenta().getFestival().getNombre() + ")");
            dto.setMontoTotal(caja.getMontoTotal());
            dto.setEstado(caja.getEstado().name());
            resultado.add(dto);
        }

        return resultado;

    }
}
package com.unla.epicentrogourmet.services.implementation;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unla.epicentrogourmet.entities.Caja;
import com.unla.epicentrogourmet.entities.enums.EstadoCaja;
import com.unla.epicentrogourmet.repositories.CajaRepository;
import com.unla.epicentrogourmet.services.ICajaService;

@Service
public class CajaService implements ICajaService {

    private final CajaRepository cajaRepository;

    public CajaService(CajaRepository cajaRepository) {
        this.cajaRepository = cajaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Caja traerCajaActual(Integer cajeroId) {
        return cajaRepository.findFirstByCajeroIdAndEstado(cajeroId, EstadoCaja.ABIERTA)
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
}
package com.unla.epicentrogourmet.services.implementation;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unla.epicentrogourmet.dtos.PlatoFormDTO;
import com.unla.epicentrogourmet.dtos.PlatoListadoDTO;
import com.unla.epicentrogourmet.entities.Plato;
import com.unla.epicentrogourmet.entities.UnidadVenta;
import com.unla.epicentrogourmet.repositories.IPlatoRepository;
import com.unla.epicentrogourmet.services.IPlatoService;
import com.unla.epicentrogourmet.services.IResponsableService;

@Service("platoService")
public class PlatoService implements IPlatoService {

    private final IPlatoRepository platoRepository;

    private final IResponsableService responsableService;

    public PlatoService(IPlatoRepository platoRepository, IResponsableService responsableService) {
        this.platoRepository = platoRepository;
        this.responsableService = responsableService;
    }

    @Override
    @Transactional
    public PlatoListadoDTO alta(String mailResponsable, Integer festivalId, PlatoFormDTO dto) {
        UnidadVenta unidad = responsableService.traerUnidad(mailResponsable, festivalId);

        // Validacion de carga: no se cargan platos en un festival que ya termino
        if (unidad.getFestival().getFechaFin().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("El festival ya terminó, no se pueden cargar platos nuevos");
        }

        // Validacion de carga: el nombre no se repite dentro del menu de la unidad
        String nombre = dto.getNombre().trim();
        if (platoRepository.contarPorNombreEnUnidad(unidad.getId(), nombre) > 0) {
            throw new IllegalArgumentException("Ya existe un plato con ese nombre en tu unidad");
        }

        Plato plato = new Plato();
        plato.setNombre(nombre);
        plato.setPrecioVenta(dto.getPrecioVenta());
        plato.setCostoProduccion(dto.getCostoProduccion());
        plato = platoRepository.save(plato);

        // El menu es una relacion de UnidadVenta (Plato no conoce a su unidad)
        unidad.getPlatos().add(plato);

        return toListadoDTO(plato);
    }

    private PlatoListadoDTO toListadoDTO(Plato p) {
        PlatoListadoDTO dto = new PlatoListadoDTO();
        dto.setId(p.getId());
        dto.setNombre(p.getNombre());
        dto.setPrecioVenta(p.getPrecioVenta());
        dto.setCostoProduccion(p.getCostoProduccion());
        return dto;
    }
}

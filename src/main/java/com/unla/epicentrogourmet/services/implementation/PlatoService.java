package com.unla.epicentrogourmet.services.implementation;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unla.epicentrogourmet.dtos.PlatoDTO;
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

    @Override
    @Transactional(readOnly = true)
    public List<PlatoListadoDTO> listar(String mailResponsable, Integer festivalId, String nombre,
                                        Double precioMin, Double precioMax) {
        UnidadVenta unidad = responsableService.traerUnidad(mailResponsable, festivalId);

        // Validacion de carga: el rango de precios tiene que tener sentido
        if (precioMin != null && precioMax != null && precioMin > precioMax) {
            throw new IllegalArgumentException("El precio mínimo no puede ser mayor al precio máximo");
        }

        String filtroNombre = (nombre == null || nombre.isBlank()) ? null : nombre.trim();
        return platoRepository.filtrar(unidad.getId(), filtroNombre, precioMin, precioMax).stream()
                .map(this::toListadoDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PlatoDTO platoEstrella(String mailResponsable, Integer festivalId) {
        UnidadVenta unidad = responsableService.traerUnidad(mailResponsable, festivalId);

        // La consulta viene ordenada: la primera fila es el plato que aparecio en mas pedidos
        List<Object[]> filas = platoRepository.platosPorCantidadDePedidos(unidad.getId(), festivalId);
        if (filas.isEmpty()) {
            return null;
        }
        Object[] estrella = filas.get(0);
        PlatoDTO dto = new PlatoDTO();
        dto.setNombrePlato((String) estrella[1]);
        dto.setPrecioVenta(((Number) estrella[2]).doubleValue());
        dto.setCantidadPedida(((Number) estrella[3]).intValue());
        return dto;
    }

    private PlatoListadoDTO toListadoDTO(Plato p) {
        PlatoListadoDTO dto = new PlatoListadoDTO();
        dto.setId(p.getId());
        dto.setNombre(p.getNombre());
        dto.setPrecioVenta(p.getPrecioVenta());
        dto.setCostoProduccion(p.getCostoProduccion());
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public PlatoFormDTO traerParaEditar(String mailResponsable, Integer festivalId, Integer platoId) {
        UnidadVenta unidad = responsableService.traerUnidad(mailResponsable, festivalId);
        Plato plato = traerDeLaUnidad(unidad, platoId);
        PlatoFormDTO dto = new PlatoFormDTO();
        dto.setNombre(plato.getNombre());
        dto.setPrecioVenta(plato.getPrecioVenta());
        dto.setCostoProduccion(plato.getCostoProduccion());
        return dto;
    }

    @Override
    @Transactional
    public PlatoListadoDTO modificar(String mailResponsable, Integer festivalId, Integer platoId, PlatoFormDTO dto) {
        UnidadVenta unidad = responsableService.traerUnidad(mailResponsable, festivalId);
        validarFestivalVigente(unidad, "modificar platos");
        Plato plato = traerDeLaUnidad(unidad, platoId);

        // Validacion de carga: un plato dado de baja primero se reactiva
        if (!plato.isActivo()) {
            throw new IllegalArgumentException("El plato está dado de baja, reactivalo para poder modificarlo");
        }
        // Validacion de carga: el nombre no se repite con otro plato del menu de la unidad
        String nombre = dto.getNombre().trim();
        if (platoRepository.contarOtrosPorNombreEnUnidad(unidad.getId(), nombre, platoId) > 0) {
            throw new IllegalArgumentException("Ya existe otro plato con ese nombre en tu unidad");
        }

        plato.setNombre(nombre);
        plato.setPrecioVenta(dto.getPrecioVenta());
        plato.setCostoProduccion(dto.getCostoProduccion());
        return toListadoDTO(platoRepository.save(plato));
    }

    @Override
    @Transactional
    public void darDeBaja(String mailResponsable, Integer festivalId, Integer platoId) {
        UnidadVenta unidad = responsableService.traerUnidad(mailResponsable, festivalId);
        validarFestivalVigente(unidad, "dar de baja platos");
        Plato plato = traerDeLaUnidad(unidad, platoId);
        if (!plato.isActivo()) {
            throw new IllegalArgumentException("El plato ya está dado de baja");
        }
        // Baja logica: se conserva el plato porque los pedidos anteriores lo referencian
        plato.setActivo(false);
        platoRepository.save(plato);
    }

    @Override
    @Transactional
    public void reactivar(String mailResponsable, Integer festivalId, Integer platoId) {
        UnidadVenta unidad = responsableService.traerUnidad(mailResponsable, festivalId);
        validarFestivalVigente(unidad, "reactivar platos");
        Plato plato = traerDeLaUnidad(unidad, platoId);
        if (plato.isActivo()) {
            throw new IllegalArgumentException("El plato ya está activo");
        }
        plato.setActivo(true);
        platoRepository.save(plato);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlatoListadoDTO> listarBajas(String mailResponsable, Integer festivalId) {
        UnidadVenta unidad = responsableService.traerUnidad(mailResponsable, festivalId);
        return platoRepository.listarBajas(unidad.getId()).stream()
                .map(this::toListadoDTO)
                .toList();
    }

    // Validacion de carga: un festival que ya termino no se toca
    private void validarFestivalVigente(UnidadVenta unidad, String accion) {
        if (unidad.getFestival().getFechaFin().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("El festival ya terminó, no se pueden " + accion);
        }
    }

    // El plato tiene que estar en el menu de la unidad del responsable
    private Plato traerDeLaUnidad(UnidadVenta unidad, Integer platoId) {
        return platoRepository.buscarEnUnidad(unidad.getId(), platoId)
                .orElseThrow(() -> new IllegalArgumentException("El plato no existe en tu unidad"));
    }
}

package com.unla.epicentrogourmet.services.implementation;


import com.unla.epicentrogourmet.dtos.*;
import com.unla.epicentrogourmet.entities.*;
import com.unla.epicentrogourmet.repositories.IEmpleadoRepository;
import com.unla.epicentrogourmet.services.IEmpleadoService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpleadoService
        implements IEmpleadoService {

    private final IEmpleadoRepository empleadoRepository;

    public EmpleadoService(IEmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }


    @Override
    @Transactional
    public EmpleadoDTO traerPorMail(String mail) {
        Empleado e = empleadoRepository.traerPorMail(mail)
                .orElseThrow(() -> new RuntimeException("No hay un empleado asociado al usuario " + mail));
        return toEmpleadoDTO(e);
    }

    @Override
    @Transactional
    public List<AsignacionDTO> traerAsignaciones(Integer empleadoId) {
        return empleadoRepository.traerUnidades(empleadoId).stream()
                .map(this::toAsignacionDTO)
                .toList();
    }

    @Override
    @Transactional
    public AsignacionDTO traerAsignacionActual(Integer empleadoId) {
        return traerAsignaciones(empleadoId).stream()
                .filter(AsignacionDTO::isActivo)
                .findFirst()
                .orElse(null);
    }

    private AsignacionDTO toAsignacionDTO(UnidadVenta u) {
        Festival f = u.getFestival();
        return new AsignacionDTO(
                f.getNombre(),
                u.getNombreComercial(),
                f.getFechaInicio(),
                f.getFechaFin(),
                f.estaActivo(),
                f.getId());
    }

    private EmpleadoDTO toEmpleadoDTO(Empleado e) {
        EmpleadoDTO dto = new EmpleadoDTO();
        dto.setId(e.getId());
        dto.setNombre(e.getNombre());
        dto.setApellido(e.getApellido());
        dto.setDni(e.getDni());
        dto.setFechaNacimiento(e.getFechaNacimiento());
        dto.setFechaIngreso(e.getFechaIngreso());
        if (e instanceof Cocinero c) {
            dto.setTipoEmpleado("Cocinero");
            dto.setEspecialidad(c.getEspecialidad() == null ? null : c.getEspecialidad().toString());
            dto.setPlusCategoria(c.getCategoria().getPlus());
        } else if (e instanceof Cajero c) {
            dto.setTipoEmpleado("Cajero");
            dto.setTurno(c.getTurno() == null ? null : switch (c.getTurno()) {
                case MANIANA -> "Mañana";
                case NOCHE -> "Noche";
            });
        }
        return dto;
    }
}
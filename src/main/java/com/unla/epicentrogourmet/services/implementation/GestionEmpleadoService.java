package com.unla.epicentrogourmet.services.implementation;

import com.unla.epicentrogourmet.dtos.EmpleadoGestionDTO;
import com.unla.epicentrogourmet.entities.Cajero;
import com.unla.epicentrogourmet.entities.Cocinero;
import com.unla.epicentrogourmet.entities.Empleado;
import com.unla.epicentrogourmet.entities.UnidadVenta;
import com.unla.epicentrogourmet.repositories.IEmpleadoRepository;
import com.unla.epicentrogourmet.services.IGestionEmpleadoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("gestionEmpleadoService")
public class GestionEmpleadoService implements IGestionEmpleadoService {

    private final IEmpleadoRepository empleadoRepository;

    public GestionEmpleadoService(IEmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpleadoGestionDTO> traerActivos(String mailResponsable) {

        List<Empleado> empleados = empleadoRepository.traerActivosPorResponsable(mailResponsable);
        List<EmpleadoGestionDTO> resultado = new ArrayList<>();

        for (Empleado e: empleados){
            resultado.add(convertirADto(e, mailResponsable));
        }

        return resultado;

    }

    private EmpleadoGestionDTO convertirADto(Empleado e, String mailResponsable){

        EmpleadoGestionDTO dto = new EmpleadoGestionDTO();

        dto.setId(e.getId());
        dto.setNombre(e.getNombre());
        dto.setApellido(e.getApellido());
        dto.setDni(e.getDni());
        dto.setMail(e.getUsuario().getMail());

        if (e instanceof Cocinero c){
            dto.setTipo("Cocinero");
            dto.setDetalle(c.getCategoria().name());
        } else if (e instanceof Cajero c){
            dto.setTipo("Cajero");
            dto.setDetalle("Turno " + c.getTurno().name());
        }

        List<String> unidades = new ArrayList<>();
        for (UnidadVenta u : empleadoRepository.traerUnidades(e.getId())){
            if (u.getResponsable().getMail().equals(mailResponsable)){
                unidades.add(u.getNombreComercial() + " (" + u.getFestival().getNombre() + ")");
            }
        }

        dto.setUnidades(unidades);

        return dto;

    }

}

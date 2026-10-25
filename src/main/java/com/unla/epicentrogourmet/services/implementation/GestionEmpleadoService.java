package com.unla.epicentrogourmet.services.implementation;

import com.unla.epicentrogourmet.dtos.EmpleadoFormDTO;
import com.unla.epicentrogourmet.dtos.EmpleadoGestionDTO;
import com.unla.epicentrogourmet.entities.*;
import com.unla.epicentrogourmet.entities.enums.Rol;
import com.unla.epicentrogourmet.entities.enums.TipoEmpleado;
import com.unla.epicentrogourmet.repositories.IEmpleadoRepository;
import com.unla.epicentrogourmet.repositories.IUnidadVentaRepository;
import com.unla.epicentrogourmet.repositories.IUserRolRepository;
import com.unla.epicentrogourmet.repositories.IUsuarioRepository;
import com.unla.epicentrogourmet.services.IGestionEmpleadoService;
import com.unla.epicentrogourmet.services.IMailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.*;

@Service("gestionEmpleadoService")
public class GestionEmpleadoService implements IGestionEmpleadoService {

    private final IEmpleadoRepository empleadoRepository;
    private final IUsuarioRepository usuarioRepository;
    private final IUserRolRepository userRolRepository;
    private final IUnidadVentaRepository unidadVentaRepository;
    private final PasswordEncoder passwordEncoder;

    private final IMailService mailService;

    public GestionEmpleadoService(IEmpleadoRepository empleadoRepository, IUsuarioRepository usuarioRepository, IUserRolRepository userRolRepository, IUnidadVentaRepository unidadVentaRepository, PasswordEncoder passwordEncoder, IMailService mailService) {
        this.empleadoRepository = empleadoRepository;
        this.usuarioRepository = usuarioRepository;
        this.userRolRepository = userRolRepository;
        this.unidadVentaRepository = unidadVentaRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
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

    @Override
    @Transactional(readOnly = true)
    public Map<Integer, String> traerUnidadesVigentes(String mailResponsable) {

        Map<Integer, String> opciones = new LinkedHashMap<>();

        for (UnidadVenta uv : unidadVentaRepository.findByResponsableMail(mailResponsable)){
            if (!uv.getFestival().getFechaFin().isBefore(LocalDate.now())){
                opciones.put(uv.getId(), uv.getNombreComercial() + " (" + uv.getFestival().getNombre() + ")");
            }
        }

        return opciones;

    }

    @Override
    @Transactional
    public String darDeAlta(EmpleadoFormDTO empleadoFormDTO, String mailResponsable) {

        Optional<Empleado> existente = empleadoRepository.findByDni(empleadoFormDTO.getDni());
        if (existente.isPresent()){
            if (!existente.get().isActivo())
                throw new IllegalArgumentException("Ese empleado está dado de baja.");
            throw new IllegalArgumentException("Ya existe un empleado con ese DNI.");
        }
        if (empleadoFormDTO.getFechaNacimiento().plusYears(18).isAfter(LocalDate.now()))
            throw new IllegalArgumentException("El empleado debe ser mayor de edad.");
        if (usuarioRepository.existsByMail(empleadoFormDTO.getMail()))
            throw new IllegalArgumentException("Ya existe un usuario con ese mail.");
        if (empleadoFormDTO.getUnidadVentaId() == null)
            throw new IllegalArgumentException("Debe elegir la unidad de venta.");

        UnidadVenta unidadVenta = unidadVentaRepository.findById(empleadoFormDTO.getUnidadVentaId())
                .orElseThrow(() -> new IllegalArgumentException("La unidad de venta no existe."));
        if (!unidadVenta.getResponsable().getMail().equals(mailResponsable))
            throw new IllegalArgumentException("Solo puede asignar empleados a las unidades a su cargo.");
        if (unidadVenta.getFestival().getFechaFin().isBefore(LocalDate.now()))
            throw new IllegalArgumentException("El festival de esa unidad ya terminó.");

        String contrasenia = generarContrasenia();
        Usuario usuario = new Usuario(empleadoFormDTO.getMail(), passwordEncoder.encode(contrasenia));
        usuarioRepository.save(usuario);

        UserRol userRol = new UserRol();
        userRol.setUsuario(usuario);
        userRol.setRol(Rol.EMPLEADO);
        userRolRepository.save(userRol);

        Empleado empleado;

        if (empleadoFormDTO.getTipoEmpleado() == TipoEmpleado.COCINERO){
            if (empleadoFormDTO.getCategoriaCocina() == null)
                throw new IllegalArgumentException("La categoria es obligatoria para un cocinero.");

            Cocinero cocinero = new Cocinero();
            cocinero.setEspecialidad(empleadoFormDTO.getEspecialidad());
            cocinero.setCategoria(empleadoFormDTO.getCategoriaCocina());
            empleado = cocinero;

        } else {
            if (empleadoFormDTO.getTurno() == null)
                throw new IllegalArgumentException("El turno es obligatorio para un cajero.");

            Cajero cajero = new Cajero();
            cajero.setTurno(empleadoFormDTO.getTurno());
            empleado = cajero;

        }

        empleado.setNombre(empleadoFormDTO.getNombre());
        empleado.setApellido(empleadoFormDTO.getApellido());
        empleado.setDni(empleadoFormDTO.getDni());
        empleado.setFechaNacimiento(empleadoFormDTO.getFechaNacimiento());
        empleado.setFechaIngreso(empleadoFormDTO.getFechaIngreso());
        empleado.setUsuario(usuario);
        empleadoRepository.save(empleado);

        unidadVenta.getPersonal().add(empleado);
        unidadVentaRepository.save(unidadVenta);

        mailService.enviarMailInicial(empleadoFormDTO.getMail(), empleadoFormDTO.getNombre(), contrasenia);

        return contrasenia;

    }

    private EmpleadoGestionDTO convertirADto(Empleado e, String mailResponsable){

        EmpleadoGestionDTO dto = new EmpleadoGestionDTO();

        dto.setId(e.getId());
        dto.setNombre(e.getNombre());
        dto.setApellido(e.getApellido());
        dto.setDni(e.getDni());
        dto.setMail(e.getUsuario().getMail());

        if (e instanceof Cocinero c){
            dto.setTipo(TipoEmpleado.COCINERO.name());
            dto.setDetalle(c.getCategoria().name());
        } else if (e instanceof Cajero c){
            dto.setTipo(TipoEmpleado.CAJERO.name());
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

    private String generarContrasenia(){

        StringBuilder contrasenia = new StringBuilder();

        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456789";
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < 10; i++){
            contrasenia.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }

        return contrasenia.toString();

    }

    private Empleado traerPropio (Integer id, String mailResponsable){

        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El empleado no existe."));
        if (empleadoRepository.perteneceAResponsable(id, mailResponsable) == 0)
            throw new IllegalArgumentException("No se puede acceder a empleados que no estén a su cargo.");

        return empleado;

    }

    @Override
    @Transactional(readOnly = true)
    public EmpleadoFormDTO traerEmpleadoAModificar(Integer id, String mailResponsable){

        Empleado e = traerPropio(id, mailResponsable);
        if (!e.isActivo())
            throw new IllegalArgumentException("No se puede editar un empleado inactivo.");

        EmpleadoFormDTO dto = new EmpleadoFormDTO();

        dto.setId(e.getId());
        dto.setNombre(e.getNombre());
        dto.setApellido(e.getApellido());
        dto.setDni(e.getDni());
        dto.setFechaNacimiento(e.getFechaNacimiento());
        dto.setFechaIngreso(e.getFechaIngreso());
        dto.setMail(e.getUsuario().getMail());

        if (e instanceof Cocinero c){
            dto.setTipoEmpleado(TipoEmpleado.COCINERO);
            dto.setCategoriaCocina(((Cocinero) e).getCategoria());
            dto.setEspecialidad(((Cocinero) e).getEspecialidad());
        } else if (e instanceof Cajero c){
            dto.setTipoEmpleado(TipoEmpleado.CAJERO);
            dto.setTurno(((Cajero) e).getTurno());
        }

        return dto;

    }

    @Override
    @Transactional
    public void modificar(Integer id, EmpleadoFormDTO dto, String mailResponsable){

        Empleado e = traerPropio(id, mailResponsable);
        if (!e.isActivo()) {
            throw new IllegalArgumentException("No se puede editar un empleado dado de baja.");
        }
        if (dto.getFechaNacimiento().plusYears(18).isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("El empleado tiene que ser mayor de edad.");
        }
        if (empleadoRepository.existsByDniAndIdNot(dto.getDni(), id)) {
            throw new IllegalArgumentException("Ya existe otro empleado con ese dni.");
        }

        e.setNombre(dto.getNombre());
        e.setApellido(dto.getApellido());
        e.setDni(dto.getDni());
        e.setFechaNacimiento(dto.getFechaNacimiento());
        e.setFechaIngreso(dto.getFechaIngreso());

        if (e instanceof Cocinero c) {
            if (dto.getCategoriaCocina() == null) {
                throw new IllegalArgumentException("La categoría es obligatoria para un cocinero");
            }
            c.setCategoria(dto.getCategoriaCocina());
            c.setEspecialidad(dto.getEspecialidad());
        } else if (e instanceof Cajero c) {
            if (dto.getTurno() == null) {
                throw new IllegalArgumentException("El turno es obligatorio para un cajero");
            }
            c.setTurno(dto.getTurno());
        }
        empleadoRepository.save(e);

    }

    @Override
    @Transactional
    public void darDeBaja(Integer id, String mailResponsable) {

        Empleado e = traerPropio(id, mailResponsable);
        if (!e.isActivo())
            throw new IllegalArgumentException("El empleado ya está dado de baja.");

        e.setActivo(false);
        empleadoRepository.save(e);

    }

    @Override
    @Transactional (readOnly = true)
    public List<EmpleadoGestionDTO> traerInactivos(String mailResponsable) {

        List<EmpleadoGestionDTO> resultado = new ArrayList<>();

        for (Empleado e: empleadoRepository.traerInactivosPorResponsable(mailResponsable)){
            resultado.add(convertirADto(e, mailResponsable));
        }

        return resultado;

    }

    @Override
    @Transactional
    public void reactivar(Integer id, String mailResponsable) {

        Empleado e = traerPropio(id, mailResponsable);
        if (e.isActivo())
            throw new IllegalArgumentException("El empleado ya está activo.");

        e.setActivo(true);
        empleadoRepository.save(e);

    }

}

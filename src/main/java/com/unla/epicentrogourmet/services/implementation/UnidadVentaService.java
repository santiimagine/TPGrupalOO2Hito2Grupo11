package com.unla.epicentrogourmet.services.implementation;

import java.time.LocalDate;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unla.epicentrogourmet.dtos.RegistroUnidadVentaDTO;
import com.unla.epicentrogourmet.entities.Festival;
import com.unla.epicentrogourmet.entities.FoodTruck;
import com.unla.epicentrogourmet.entities.PuestoDesarmable;
import com.unla.epicentrogourmet.entities.UnidadVenta;
import com.unla.epicentrogourmet.entities.UserRol;
import com.unla.epicentrogourmet.entities.Usuario;
import com.unla.epicentrogourmet.entities.enums.Rol;
import com.unla.epicentrogourmet.entities.enums.TipoUnidad;
import com.unla.epicentrogourmet.repositories.IFestivalRepository;
import com.unla.epicentrogourmet.repositories.IUnidadVentaRepository;
import com.unla.epicentrogourmet.repositories.IUserRolRepository;
import com.unla.epicentrogourmet.repositories.IUsuarioRepository;
import com.unla.epicentrogourmet.services.IUnidadVentaService;

@Service("unidadVentaService")
public class UnidadVentaService implements IUnidadVentaService {

    private final IUnidadVentaRepository unidadVentaRepository;

    private final IUsuarioRepository usuarioRepository;

    private final IUserRolRepository userRolRepository;

    private final IFestivalRepository festivalRepository;

    private final PasswordEncoder passwordEncoder;

    public UnidadVentaService(IUnidadVentaRepository unidadVentaRepository, IUsuarioRepository usuarioRepository,
                              IUserRolRepository userRolRepository, IFestivalRepository festivalRepository,
                              PasswordEncoder passwordEncoder) {
        this.unidadVentaRepository = unidadVentaRepository;
        this.usuarioRepository = usuarioRepository;
        this.userRolRepository = userRolRepository;
        this.festivalRepository = festivalRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UnidadVenta registrar(RegistroUnidadVentaDTO dto) {
        if (!dto.getContrasenia().equals(dto.getRepetirContrasenia())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }
        if (usuarioRepository.existsByMail(dto.getMail())) {
            throw new IllegalArgumentException("El mail ya está registrado");
        }
        if (unidadVentaRepository.existsByCodigo(dto.getCodigo())) {
            throw new IllegalArgumentException("El código ya está en uso");
        }
        Festival festival = festivalRepository.findById(dto.getFestivalId())
                .filter(f -> !f.getFechaFin().isBefore(LocalDate.now()))
                .orElseThrow(() -> new IllegalArgumentException("El festival elegido no existe o ya terminó"));

        if (dto.getTipo() == TipoUnidad.FOOD_TRUCK
                && (dto.getPatente() == null || dto.getPatente().isBlank())) {
            throw new IllegalArgumentException("La patente es obligatoria para un food truck");
        }
        if (dto.getTipo() == TipoUnidad.PUESTO_DESARMABLE
                && (dto.getCantidadCarpas() == null || dto.getTiempoMontaje() == null)) {
            throw new IllegalArgumentException(
                    "La cantidad de carpas y el tiempo de montaje son obligatorios para un puesto desarmable");
        }

        Usuario usuario = new Usuario(dto.getMail(), passwordEncoder.encode(dto.getContrasenia()));
        usuarioRepository.save(usuario);

        UserRol userRol = new UserRol();
        userRol.setUsuario(usuario);
        userRol.setRol(Rol.RESPONSABLE);
        userRolRepository.save(userRol);

        UnidadVenta unidad;
        if (dto.getTipo() == TipoUnidad.FOOD_TRUCK) {
            FoodTruck foodTruck = new FoodTruck();
            foodTruck.setPatente(dto.getPatente());
            foodTruck.setRequiereElectricidad(dto.isRequiereElectricidad());
            unidad = foodTruck;
        } else {
            PuestoDesarmable puesto = new PuestoDesarmable();
            puesto.setCantidadCarpas(dto.getCantidadCarpas());
            puesto.setTiempoMontaje(dto.getTiempoMontaje());
            unidad = puesto;
        }
        unidad.setNombreComercial(dto.getNombreComercial());
        unidad.setSuperficie(dto.getSuperficie());
        unidad.setCodigo(dto.getCodigo());
        unidad.setFestival(festival);
        unidad.setResponsable(usuario);

        return unidadVentaRepository.save(unidad);
    }
}
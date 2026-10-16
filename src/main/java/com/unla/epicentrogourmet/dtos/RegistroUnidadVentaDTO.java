package com.unla.epicentrogourmet.dtos;

import com.unla.epicentrogourmet.entities.enums.TipoUnidad;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor

public class RegistroUnidadVentaDTO {

    // ---- Datos del responsable ----
    @NotBlank(message = "El mail es obligatorio")
    @Email(message = "El mail no es válido")
    @Size(max = 100, message = "El mail no puede superar los 100 caracteres")
    private String mail;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 50, message = "La contraseña debe tener entre 6 y 50 caracteres")
    private String contrasenia;

    @NotBlank(message = "Debe repetir la contraseña")
    private String repetirContrasenia;

    // ---- Datos de la unidad de venta ----
    @NotBlank(message = "El nombre comercial es obligatorio")
    @Size(max = 100, message = "El nombre comercial no puede superar los 100 caracteres")
    private String nombreComercial;

    @NotNull(message = "La superficie es obligatoria")
    @Positive(message = "La superficie debe ser mayor a 0")
    private Double superficie;

    @Pattern(regexp = "^[A-Z0-9]{10}$", message = "El codigo debe tener 10 caracteres: solo mayusculas y numeros")
    private String codigo;

    @NotNull(message = "Debe elegir un festival")
    private Integer festivalId;

    @NotNull(message = "Debe elegir el tipo de unidad")
    private TipoUnidad tipo;

    // ---- Solo si es Food Truck ----
    private String patente;

    private boolean requiereElectricidad;

    // ---- Solo si es Puesto Desarmable ----
    @Min(value = 1, message = "Debe tener al menos 1 carpa")
    private Integer cantidadCarpas;

    @Min(value = 0, message = "El tiempo de montaje no puede ser negativo")
    private Integer tiempoMontaje;

}

package com.unla.epicentrogourmet.dtos;


import com.unla.epicentrogourmet.entities.enums.CategoriaCocina;
import com.unla.epicentrogourmet.entities.enums.TipoEmpleado;
import com.unla.epicentrogourmet.entities.enums.Turno;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class EmpleadoFormDTO {

    private Integer id;

    // DATOS PERSONALES:
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "El dni es obligatorio")
    private String dni;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento tiene que ser anterior a hoy")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;

    @NotNull(message = "La fecha de ingreso es obligatoria")
    @PastOrPresent(message = "La fecha de ingreso no puede ser futura")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaIngreso;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El mail no es válido")
    private String mail;

    //@NotNull(message = "Debe elegir la unidad de venta")
    private Integer unidadVentaId;

    @NotNull(message = "Debe elegir el tipo de empleado")
    private TipoEmpleado tipoEmpleado;

    // COCINERO:
    private String especialidad;

    private CategoriaCocina categoriaCocina;

    //CAJERO:
    private Turno turno;

}

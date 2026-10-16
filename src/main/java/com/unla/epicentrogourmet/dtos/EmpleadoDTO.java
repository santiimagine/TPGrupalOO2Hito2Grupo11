package com.unla.epicentrogourmet.dtos;

import com.unla.epicentrogourmet.entities.Cocinero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor
public class EmpleadoDTO {

    private Integer id;
    private String nombre;
    private String apellido;
    private String dni;
    private LocalDate fechaNacimiento;
    private LocalDate fechaIngreso;
    private Boolean activo;

    private String tipoEmpleado; //Cocinero o Cajero


    //Cocinero
    private String especialidad;
    private Double plusCategoria;

    //Cajero
    private String turno; // Mañana - Noche

}


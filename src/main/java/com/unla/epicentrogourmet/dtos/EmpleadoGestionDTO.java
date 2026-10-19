package com.unla.epicentrogourmet.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public class EmpleadoGestionDTO {

    private Integer id;
    private String nombre;
    private String apellido;
    private String dni;
    private String mail;
    private String tipo; //Si es cocinero o cajero
    private String detalle; //Categoria o Turno, dependiendo el tipo
    private List<String> unidades; //En donde trabaja (en que unidades o festivales)

}

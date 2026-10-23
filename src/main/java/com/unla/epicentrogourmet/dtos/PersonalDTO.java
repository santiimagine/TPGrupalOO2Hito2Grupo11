package com.unla.epicentrogourmet.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class PersonalDTO {
    private String nombreCompleto;

    private String dni;

    private String puesto;

    private String detalle;

    private boolean activo;
}

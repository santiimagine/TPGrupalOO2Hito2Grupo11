package com.unla.epicentrogourmet.services;

import com.unla.epicentrogourmet.dtos.*;

import java.util.List;

public interface IEmpleadoService {
	EmpleadoDTO traerPorMail(String mail);
	AsignacionDTO traerAsignacionActual(Integer empleadoId);   // null si no hay
	List<AsignacionDTO> traerAsignaciones(Integer empleadoId);
	byte[] generarPdf(Integer empleadoId, Integer festivalId);
}

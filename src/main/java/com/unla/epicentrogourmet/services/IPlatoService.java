package com.unla.epicentrogourmet.services;

import java.util.List;

import com.unla.epicentrogourmet.dtos.PlatoDTO;
import com.unla.epicentrogourmet.dtos.PlatoFormDTO;
import com.unla.epicentrogourmet.dtos.PlatoListadoDTO;

public interface IPlatoService {

	//Agrega un plato al menu de la unidad que el responsable tiene en ese festival
	PlatoListadoDTO alta(String mailResponsable, Integer festivalId, PlatoFormDTO dto);

	//Platos activos del menu de la unidad, filtrados por uno o varios atributos (todos opcionales)
	List<PlatoListadoDTO> listar(String mailResponsable, Integer festivalId, String nombre, Double precioMin, Double precioMax);

	//Plato de la unidad que aparecio en mas pedidos en el festival. null si la unidad no tuvo pedidos.
	PlatoDTO platoEstrella(String mailResponsable, Integer festivalId);

	//Datos actuales de un plato de la unidad, para precargar el formulario de modificacion
	PlatoFormDTO traerParaEditar(String mailResponsable, Integer festivalId, Integer platoId);

	//Modifica nombre, precio y costo de un plato activo de la unidad. El nombre no puede repetirse en la unidad.
	PlatoListadoDTO modificar(String mailResponsable, Integer festivalId, Integer platoId, PlatoFormDTO dto);

	//Baja logica: el plato queda inactivo (no se borra) y deja de aparecer en el listado
	void darDeBaja(String mailResponsable, Integer festivalId, Integer platoId);

	//Vuelve a activar un plato dado de baja
	void reactivar(String mailResponsable, Integer festivalId, Integer platoId);

	//Platos dados de baja de la unidad en el festival
	List<PlatoListadoDTO> listarBajas(String mailResponsable, Integer festivalId);
}

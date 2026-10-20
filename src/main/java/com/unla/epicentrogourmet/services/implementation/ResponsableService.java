package com.unla.epicentrogourmet.services.implementation;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unla.epicentrogourmet.dtos.FestivalDTO;
import com.unla.epicentrogourmet.entities.Festival;
import com.unla.epicentrogourmet.entities.UnidadVenta;
import com.unla.epicentrogourmet.repositories.IUnidadVentaRepository;
import com.unla.epicentrogourmet.services.IResponsableService;

@Service("responsableService")
public class ResponsableService implements IResponsableService {

	private final IUnidadVentaRepository unidadVentaRepository;

	public ResponsableService(IUnidadVentaRepository unidadVentaRepository) {
		this.unidadVentaRepository = unidadVentaRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<FestivalDTO> traerFestivales(String mail) {
		return unidadVentaRepository.findByResponsableMail(mail).stream()
				.map(UnidadVenta::getFestival)
				.distinct()
				.sorted(Comparator.comparing(Festival::getFechaInicio).reversed())
				.map(f -> new FestivalDTO(f.getId(), f.getNombre(), f.getTemporada(), f.getFechaInicio(), f.getFechaFin()))
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public UnidadVenta traerUnidad(String mail, Integer festivalId) {
		return unidadVentaRepository.findByResponsableMail(mail).stream()
				.filter(u -> u.getFestival().getId().equals(festivalId))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("No tenés una unidad de venta en ese festival"));
	}
}

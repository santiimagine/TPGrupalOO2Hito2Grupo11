package com.unla.epicentrogourmet.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.unla.epicentrogourmet.dtos.FestivalDTO;
import com.unla.epicentrogourmet.helpers.ViewRouteHelper;
import com.unla.epicentrogourmet.services.IRentabilidadService;
import com.unla.epicentrogourmet.services.IResponsableService;

//Rentabilidad de la unidad del responsable logueado (la ruta /responsable/** solo la ve el rol RESPONSABLE)
@Controller
@RequestMapping("/responsable/rentabilidad")
public class RentabilidadController {

	private final IRentabilidadService rentabilidadService;

	private final IResponsableService responsableService;

	public RentabilidadController(IRentabilidadService rentabilidadService, IResponsableService responsableService) {
		this.rentabilidadService = rentabilidadService;
		this.responsableService = responsableService;
	}

	@GetMapping("")
	public ModelAndView index(Principal principal, @RequestParam(required = false) Integer festivalId) {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.RENTABILIDAD_INDEX);
		List<FestivalDTO> festivales = responsableService.traerFestivales(principal.getName());
		mAV.addObject("festivales", festivales);
		Integer elegido = festivalId != null ? festivalId : (festivales.isEmpty() ? null : festivales.get(0).getId());
		mAV.addObject("festivalId", elegido);
		if (elegido != null) {
			try {
				mAV.addObject("rentabilidad", rentabilidadService.rentabilidadNeta(principal.getName(), elegido));
			} catch (IllegalArgumentException e) {
				mAV.addObject("error", e.getMessage());
			}
		}
		return mAV;
	}
}

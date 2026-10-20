package com.unla.epicentrogourmet.controllers;

import java.util.List;


import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.unla.epicentrogourmet.dtos.FestivalDTO;
import com.unla.epicentrogourmet.helpers.ViewRouteHelper;
import com.unla.epicentrogourmet.services.IFestivalService;
import com.unla.epicentrogourmet.services.IUnidadVentaService;
import com.unla.epicentrogourmet.services.IReporteService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	private final IFestivalService festivalService;

	private final IUnidadVentaService unidadVentaService;

	private final IReporteService reporteService;

	private final ModelMapper modelMapper;

	public AdminController(IFestivalService festivalService, IUnidadVentaService unidadVentaService,
						   IReporteService reporteService, ModelMapper modelMapper) {
		this.festivalService = festivalService;
		this.unidadVentaService = unidadVentaService;
		this.reporteService = reporteService;
		this.modelMapper = modelMapper;
	}

	//GET Example: SERVER/admin  o  SERVER/admin?festivalId=1&vista=ranking o SERVER/admin?festivalId=1&vista=canon&cantidad=3
	@GetMapping("")
	public ModelAndView index(@RequestParam(required = false) Integer festivalId,
							  @RequestParam(defaultValue = "unidades") String vista,
							  @RequestParam(defaultValue = "3") int cantidad) {
		ModelAndView modelAndView = new ModelAndView(ViewRouteHelper.ADMIN_INDEX);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication != null ? authentication.getPrincipal() : null;
		if (principal instanceof User user) {
			modelAndView.addObject("username", user.getUsername());
		}

		List<FestivalDTO> festivales = festivalService.getAll()
				.stream()
				.map(festival -> modelMapper.map(festival, FestivalDTO.class))
				.toList();
		modelAndView.addObject("festivales", festivales);

		festivales.stream()
				.filter(festival -> festival.getId().equals(festivalId))
				.findFirst()
				.ifPresent(festival -> {
					modelAndView.addObject("festivalSeleccionado", festival);
					if ("ranking".equals(vista)) {
						modelAndView.addObject("vista", "ranking");
						modelAndView.addObject("ranking", reporteService.rankingPorRecaudacion(festival.getId()));
					} else if ("canon".equals(vista)) {
						int x = Math.max(cantidad, 1);
						modelAndView.addObject("vista", "canon");
						modelAndView.addObject("cantidad", x);
						modelAndView.addObject("mayoresCanon", reporteService.mayoresCanon(festival.getId(), x));
					}else {
						modelAndView.addObject("vista", "unidades");
						modelAndView.addObject("unidades", unidadVentaService.traerPorFestival(festival.getId()));
					}
				});
		return modelAndView;
	}
}
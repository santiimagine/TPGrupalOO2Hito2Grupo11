package com.unla.epicentrogourmet.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.unla.epicentrogourmet.dtos.FestivalDTO;
import com.unla.epicentrogourmet.dtos.PlatoFormDTO;
import com.unla.epicentrogourmet.helpers.ViewRouteHelper;
import com.unla.epicentrogourmet.services.IPlatoService;
import com.unla.epicentrogourmet.services.IResponsableService;

import jakarta.validation.Valid;

//Platos de la unidad del responsable logueado (la ruta /responsable/** solo la ve el rol RESPONSABLE)
@Controller
@RequestMapping("/responsable/platos")
public class PlatoController {

	private static final String PLATO_ATTR = "plato";

	private final IPlatoService platoService;

	private final IResponsableService responsableService;

	public PlatoController(IPlatoService platoService, IResponsableService responsableService) {
		this.platoService = platoService;
		this.responsableService = responsableService;
	}

	@GetMapping("")
	public ModelAndView index(Principal principal, @RequestParam(required = false) Integer festivalId,
			@RequestParam(required = false) String nombre, @RequestParam(required = false) Double precioMin,
			@RequestParam(required = false) Double precioMax) {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.PLATO_INDEX);
		cargarFestivales(mAV, principal.getName(), festivalId);
		Integer elegido = (Integer) mAV.getModel().get("festivalId");
		mAV.addObject("nombre", nombre);
		mAV.addObject("precioMin", precioMin);
		mAV.addObject("precioMax", precioMax);
		if (elegido != null) {
			try {
				mAV.addObject("platos", platoService.listar(principal.getName(), elegido, nombre, precioMin, precioMax));
			} catch (IllegalArgumentException e) {
				mAV.addObject("error", e.getMessage());
			}
		}
		return mAV;
	}

	@GetMapping("/estrella")
	public ModelAndView estrella(Principal principal, @RequestParam(required = false) Integer festivalId) {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.PLATO_ESTRELLA);
		cargarFestivales(mAV, principal.getName(), festivalId);
		Integer elegido = (Integer) mAV.getModel().get("festivalId");
		if (elegido != null) {
			try {
				mAV.addObject("platoEstrella", platoService.platoEstrella(principal.getName(), elegido));
			} catch (IllegalArgumentException e) {
				mAV.addObject("error", e.getMessage());
			}
		}
		return mAV;
	}

	@GetMapping("/nuevo")
	public ModelAndView nuevo(Principal principal, @RequestParam(required = false) Integer festivalId) {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.PLATO_NEW);
		cargarFestivales(mAV, principal.getName(), festivalId);
		mAV.addObject(PLATO_ATTR, new PlatoFormDTO());
		return mAV;
	}

	@PostMapping("/crear")
	public ModelAndView crear(Principal principal, @RequestParam Integer festivalId,
			@Valid @ModelAttribute(PLATO_ATTR) PlatoFormDTO plato, BindingResult result,
			RedirectAttributes redirectAttributes) {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.PLATO_NEW);
		if (!result.hasErrors()) {
			try {
				platoService.alta(principal.getName(), festivalId, plato);
				redirectAttributes.addFlashAttribute("exito", "El plato se agregó al menú de tu unidad");
				return new ModelAndView("redirect:" + ViewRouteHelper.PLATO_ROOT + "?festivalId=" + festivalId);
			} catch (IllegalArgumentException e) {
				mAV.addObject("error", e.getMessage());
			}
		}
		cargarFestivales(mAV, principal.getName(), festivalId);
		return mAV;
	}

	//Carga el selector de festivales del responsable. Si no se eligio ninguno usa el mas reciente.
	private void cargarFestivales(ModelAndView mAV, String mail, Integer festivalId) {
		List<FestivalDTO> festivales = responsableService.traerFestivales(mail);
		mAV.addObject("festivales", festivales);
		Integer elegido = festivalId != null ? festivalId : (festivales.isEmpty() ? null : festivales.get(0).getId());
		mAV.addObject("festivalId", elegido);
	}
}

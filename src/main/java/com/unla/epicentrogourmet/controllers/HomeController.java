package com.unla.epicentrogourmet.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.unla.epicentrogourmet.dtos.FestivalDTO;
import com.unla.epicentrogourmet.entities.enums.Rol;
import com.unla.epicentrogourmet.helpers.ViewRouteHelper;
import com.unla.epicentrogourmet.services.IFestivalService;

@Controller
@RequestMapping("/")
public class HomeController {

	private final IFestivalService festivalService;

	private final ModelMapper modelMapper;

	public HomeController(IFestivalService festivalService, ModelMapper modelMapper) {
		this.festivalService = festivalService;
		this.modelMapper = modelMapper;
	}

	//GET Example: SERVER/index
	@GetMapping("/index")
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView(ViewRouteHelper.INDEX);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication != null ? authentication.getPrincipal() : null;
		if (principal instanceof User user) {
			boolean isAdmin = user.getAuthorities().stream()
					.anyMatch(authority -> authority.getAuthority().equals(Rol.ADMIN.name()));
			if (isAdmin) {
				return new ModelAndView(new RedirectView(ViewRouteHelper.ADMIN_ROOT));
			}
			modelAndView.addObject("username", user.getUsername());
		} else {
			modelAndView.addObject("username", "anonymous");
		}
		modelAndView.addObject("festivales", festivalService.getAll()
				.stream()
				.map(festival -> modelMapper.map(festival, FestivalDTO.class))
				.toList());
		return modelAndView;
	}

	@GetMapping("/")
	public RedirectView redirectToHomeIndex() {
		return new RedirectView(ViewRouteHelper.ROUTE);
	}
}

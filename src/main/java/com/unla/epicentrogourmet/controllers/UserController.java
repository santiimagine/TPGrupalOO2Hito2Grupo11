package com.unla.epicentrogourmet.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.unla.epicentrogourmet.entities.enums.Rol;
import com.unla.epicentrogourmet.helpers.ViewRouteHelper;


@Controller
public class UserController {

	@GetMapping("/login")
	public String login(Model model,
						@RequestParam(required=false) String error,
						@RequestParam(required=false) String logout,
						@RequestParam(required=false) String registro,
						Authentication authentication) {
		if (authentication != null) {
			String panel = panelSegunRol(authentication);
			if (panel != null) return "redirect:" + panel;
		}
		model.addAttribute("error", error);
		model.addAttribute("logout", logout);
		model.addAttribute("registro", registro);
		return ViewRouteHelper.USER_LOGIN;
	}

	@GetMapping("/logout")
	public String logout(Model model) {
		return ViewRouteHelper.USER_LOGOUT;
	}

	@GetMapping("/loginsuccess")
	public String loginCheck(Authentication authentication) {

		String panel = panelSegunRol(authentication);

		return "redirect:" + (panel != null ? panel : ViewRouteHelper.ROUTE);
	}

	private boolean tieneRol(Authentication authentication, Rol rol){

		return authentication.getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals(rol.name()));

	}

	private String panelSegunRol(Authentication authentication) {
		if (tieneRol(authentication, Rol.ADMIN)) return ViewRouteHelper.ADMIN_ROOT;
		if (tieneRol(authentication, Rol.EMPLEADO)) return ViewRouteHelper.EMPLEADO_ROOT;
		if (tieneRol(authentication, Rol.RESPONSABLE)) return ViewRouteHelper.RESPONSABLE_ROOT;
		return null;
	}

}

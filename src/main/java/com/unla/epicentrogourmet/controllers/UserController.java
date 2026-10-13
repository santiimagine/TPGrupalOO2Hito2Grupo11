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
						Authentication authentication) {
		if (authentication != null && authentication.getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals(Rol.ADMIN.name()))) {
			return "redirect:" + ViewRouteHelper.ADMIN_ROOT;
		}
		model.addAttribute("error", error);
		model.addAttribute("logout", logout);
		return ViewRouteHelper.USER_LOGIN;
	}

	@GetMapping("/logout")
	public String logout(Model model) {
		return ViewRouteHelper.USER_LOGOUT;
	}

	@GetMapping("/loginsuccess")
	public String loginCheck(Authentication authentication) {
		boolean isAdmin = authentication.getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals(Rol.ADMIN.name()));
		if (isAdmin) {
			return "redirect:" + ViewRouteHelper.ADMIN_ROOT;
		}
		return "redirect:" + ViewRouteHelper.ROUTE;
	}
}

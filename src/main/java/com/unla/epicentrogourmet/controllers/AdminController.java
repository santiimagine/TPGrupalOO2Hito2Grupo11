package com.unla.epicentrogourmet.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.unla.epicentrogourmet.helpers.ViewRouteHelper;

@Controller
@RequestMapping("/admin")
public class AdminController {

	//GET Example: SERVER/admin
	@GetMapping("")
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView(ViewRouteHelper.ADMIN_INDEX);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication != null ? authentication.getPrincipal() : null;
		if (principal instanceof User user) {
			modelAndView.addObject("username", user.getUsername());
		}
		return modelAndView;
	}
}

package com.unla.epicentrogourmet.controllers;

import com.unla.epicentrogourmet.helpers.ViewRouteHelper;
import com.unla.epicentrogourmet.services.IGestionEmpleadoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
@RequestMapping("/responsable/empleados")
public class EmpleadoGestionController {

    private final IGestionEmpleadoService gestionEmpleadoService;

    public EmpleadoGestionController(IGestionEmpleadoService gestionEmpleadoService) {
        this.gestionEmpleadoService = gestionEmpleadoService;
    }

    @GetMapping("")
    public ModelAndView listar(Principal principal){
        ModelAndView mav = new ModelAndView(ViewRouteHelper.EMPLEADO_LIST);

        mav.addObject("username", principal.getName());
        mav.addObject("empleados", gestionEmpleadoService.traerActivos(principal.getName()));

        return mav;
    }

}

package com.unla.epicentrogourmet.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.unla.epicentrogourmet.dtos.EmpleadoDTO;
import com.unla.epicentrogourmet.helpers.ViewRouteHelper;
import com.unla.epicentrogourmet.services.IEmpleadoService;


@Controller
@RequestMapping("/empleado")
public class EmpleadoController {

    private final IEmpleadoService empleadoService;

    public EmpleadoController(IEmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }
    //@GetMapping({""})
    @GetMapping({"", "/"})
    public ModelAndView index(Principal principal) {
        ModelAndView mv = new ModelAndView(ViewRouteHelper.EMPLEADO_INDEX);

        EmpleadoDTO empleado = empleadoService.traerPorMail(principal.getName());

        mv.addObject("empleado", empleado);
        mv.addObject("asignacionActual", empleadoService.traerAsignacionActual(empleado.getId()));
        mv.addObject("asignaciones", empleadoService.traerAsignaciones(empleado.getId()));
        mv.addObject("esCajero", "Cajero".equals(empleado.getTipoEmpleado()));

        return mv;
    }
}














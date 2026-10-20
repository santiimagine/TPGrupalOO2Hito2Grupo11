package com.unla.epicentrogourmet.controllers;

import com.unla.epicentrogourmet.dtos.EmpleadoFormDTO;
import com.unla.epicentrogourmet.entities.enums.CategoriaCocina;
import com.unla.epicentrogourmet.helpers.ViewRouteHelper;
import com.unla.epicentrogourmet.services.IGestionEmpleadoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/new")
    public ModelAndView nuevo(Principal principal) {
        return formulario(new EmpleadoFormDTO(), principal);
    }

    @PostMapping("/create")
    public ModelAndView crear (@Valid @ModelAttribute("empleado")EmpleadoFormDTO empleadoFormDTO, BindingResult bindingResult,
                               Principal principal, RedirectAttributes redirectAttributes){

        if (bindingResult.hasErrors())
            return formulario(empleadoFormDTO, principal);

        try {
            gestionEmpleadoService.darDeAlta(empleadoFormDTO, principal.getName());
        } catch (IllegalArgumentException e){
            ModelAndView mav = formulario(empleadoFormDTO, principal);
            mav.addObject("error", e.getMessage());
            return mav;
        }

        redirectAttributes.addFlashAttribute("mensaje", "Empleado" + empleadoFormDTO.getNombre() + " " + empleadoFormDTO.getApellido() + " dado de alta correctamente.");

        return new ModelAndView("redirect:" + ViewRouteHelper.EMPLEADOS_ROOT);

    }

    private ModelAndView formulario(EmpleadoFormDTO empleadoFormDTO, Principal principal){

        ModelAndView mav = new ModelAndView(ViewRouteHelper.EMPLEADO_FORM);

        mav.addObject("username", principal.getName());
        mav.addObject("empleado", empleadoFormDTO);
        mav.addObject("unidades", gestionEmpleadoService.traerUnidadesVigentes(principal.getName()));
        mav.addObject("categorias", CategoriaCocina.values());

        return mav;

    }

}

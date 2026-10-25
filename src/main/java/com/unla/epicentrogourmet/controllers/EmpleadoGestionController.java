package com.unla.epicentrogourmet.controllers;

import com.unla.epicentrogourmet.dtos.EmpleadoFormDTO;
import com.unla.epicentrogourmet.entities.enums.CategoriaCocina;
import com.unla.epicentrogourmet.helpers.ViewRouteHelper;
import com.unla.epicentrogourmet.services.IGestionEmpleadoService;
import jakarta.validation.Valid;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
        } catch (MailException e) {
            ModelAndView mav = formulario(empleadoFormDTO, principal);
            mav.addObject("error", "No se pudo enviar el mail al empleado, por lo que no se completó el alta.");
            return mav;
        }

        redirectAttributes.addFlashAttribute("mensaje", "Empleado " + empleadoFormDTO.getNombre() + " " + empleadoFormDTO.getApellido() + " dado de alta correctamente.");

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

    @GetMapping("/{id}/edit")
    public ModelAndView editar(@PathVariable int id, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            return formulario(gestionEmpleadoService.traerEmpleadoAModificar(id, principal.getName()), principal);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return new ModelAndView("redirect:" + ViewRouteHelper.EMPLEADOS_ROOT);
        }
    }

    @PostMapping("/{id}/update")
    public ModelAndView actualizar(@PathVariable int id,
                                   @Valid @ModelAttribute("empleado") EmpleadoFormDTO dto, BindingResult bindingResult,
                                   Principal principal, RedirectAttributes redirectAttributes) {
        dto.setId(id);
        if (bindingResult.hasErrors()) {
            return formulario(dto, principal);
        }
        try {
            gestionEmpleadoService.modificar(id, dto, principal.getName());
        } catch (IllegalArgumentException e) {
            ModelAndView mav = formulario(dto, principal);
            mav.addObject("error", e.getMessage());
            return mav;
        }
        redirectAttributes.addFlashAttribute("mensaje",
                "Empleado " + dto.getNombre() + " " + dto.getApellido() + " modificado correctamente");
        return new ModelAndView("redirect:" + ViewRouteHelper.EMPLEADOS_ROOT);
    }

    @PostMapping("/{id}/baja")
    public ModelAndView darDeBaja(@PathVariable Integer id, Principal principal, RedirectAttributes redirectAttributes){

        try {
            gestionEmpleadoService.darDeBaja(id, principal.getName());
            redirectAttributes.addFlashAttribute("mensaje", "Empleado dado de baja correctamente.");
        } catch (IllegalArgumentException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return new ModelAndView("redirect:" + ViewRouteHelper.EMPLEADOS_ROOT);

    }

    @PostMapping("/{id}/reactivar")
    public ModelAndView reactivar (@PathVariable Integer id, Principal principal, RedirectAttributes redirectAttributes){

        try {
            gestionEmpleadoService.reactivar(id, principal.getName());
            redirectAttributes.addFlashAttribute("mensaje", "Empleado reactivado correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return new ModelAndView("redirect:" + ViewRouteHelper.EMPLEADOS_ROOT + "/bajas");
        }

        return new ModelAndView("redirect:" + ViewRouteHelper.EMPLEADOS_ROOT);

    }

    @GetMapping("/bajas")
    public ModelAndView listarBajas(Principal principal){

        ModelAndView mav = new ModelAndView(ViewRouteHelper.EMPLEADO_BAJAS);

        mav.addObject("username", principal.getName());
        mav.addObject("empleados", gestionEmpleadoService.traerInactivos(principal.getName()));

        return mav;

    }

}

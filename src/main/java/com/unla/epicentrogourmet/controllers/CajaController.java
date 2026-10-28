package com.unla.epicentrogourmet.controllers;

import com.unla.epicentrogourmet.dtos.CajaDTO;
import com.unla.epicentrogourmet.dtos.EmpleadoDTO;
import com.unla.epicentrogourmet.helpers.ViewRouteHelper;
import com.unla.epicentrogourmet.services.ICajaService;
import com.unla.epicentrogourmet.services.IEmpleadoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/empleado/cajas")
public class CajaController {

    private final IEmpleadoService empleadoService;
    private final ICajaService cajaService;

    public CajaController(IEmpleadoService empleadoService, ICajaService cajaService) {
        this.empleadoService = empleadoService;
        this.cajaService = cajaService;
    }

    @GetMapping("")
    public ModelAndView historial (Principal principal){

        ModelAndView mav = new ModelAndView(ViewRouteHelper.CAJA_HISTORIAL);
        mav.addObject("username", principal.getName());

        EmpleadoDTO empleadoDTO = empleadoService.traerPorMail(principal.getName());
        if (!"Cajero".equals(empleadoDTO.getTipoEmpleado())){
            mav.addObject("error", "Solo los cajeros pueden acceder al historial de cajas.");
            mav.addObject("cajas", List.of());
            mav.addObject("totalRecaudado", 0.0);
            return mav;
        }

        List<CajaDTO> cajas = cajaService.traerHistorial(empleadoDTO.getId());
        mav.addObject("cajas", cajas);
        mav.addObject("totalRecaudado", cajas.stream().mapToDouble(CajaDTO::getMontoTotal).sum());
        return mav;

    }

}

package com.unla.epicentrogourmet.controllers;

import java.nio.charset.StandardCharsets;
import java.security.Principal;

import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    // GET /empleado/festivales/{id}/recibo -> URL que ya usa el botón "Descargar recibo (PDF)"
    @GetMapping("/festivales/{id}/recibo")
    public ResponseEntity<byte[]> descargarRecibo(@PathVariable("id") Integer festivalId, Principal principal) {
        // ¡ El empleado sale SIEMPRE del usuario logueado, nunca de la URL!
        EmpleadoDTO empleado = empleadoService.traerPorMail(principal.getName());
        try {
            byte[] pdf = empleadoService.generarPdf(empleado.getId(), festivalId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.attachment()
                    .filename("recibo-sueldo-festival-" + festivalId + ".pdf")
                    .build());

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        } catch (IllegalArgumentException ex) {
            // No trabajó en ese festival (o no existe). Se manda el motivo como texto para poder verlo (devuelve json)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(ex.getMessage().getBytes(StandardCharsets.UTF_8));
        } catch (IllegalStateException ex) {
            // El festival todavía no terminó (devuelve json)
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(ex.getMessage().getBytes(StandardCharsets.UTF_8));
        }
    }

}














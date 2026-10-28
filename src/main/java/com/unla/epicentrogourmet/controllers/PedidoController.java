package com.unla.epicentrogourmet.controllers;

import com.unla.epicentrogourmet.dtos.PedidoFormDTO;
import com.unla.epicentrogourmet.dtos.PedidoResumenDTO;
import com.unla.epicentrogourmet.entities.Pedido;
import com.unla.epicentrogourmet.helpers.ViewRouteHelper;
import com.unla.epicentrogourmet.services.IPedidoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/empleado/pedidos")
public class PedidoController {

    private final IPedidoService pedidoService;

    public PedidoController(IPedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/new")
    public ModelAndView nuevo (Principal principal){

        ModelAndView mav = new ModelAndView(ViewRouteHelper.PEDIDO_FORM);

        mav.addObject("username", principal.getName());
        try {
            mav.addObject("pedido", pedidoService.armarFormulario(principal.getName()));
        } catch (IllegalArgumentException e){
            mav.addObject("error", e.getMessage());
        }

        return mav;

    }

    @PostMapping("/create")
    public ModelAndView crear(@ModelAttribute("pedido")PedidoFormDTO pedidoFormDTO, Principal principal, RedirectAttributes redirectAttributes){

        try {
            Pedido pedido = pedidoService.registrar(pedidoFormDTO, principal.getName());
            redirectAttributes.addFlashAttribute("mensaje", "Pedido #" + pedido.getId() + " registrado. Total: $" + String.format("%,.2f", pedido.calcularTotal()));
        } catch (IllegalArgumentException e){
            ModelAndView mav = new ModelAndView(ViewRouteHelper.PEDIDO_FORM);
            mav.addObject("username", principal.getName());
            mav.addObject("pedido", pedidoFormDTO);
            mav.addObject("error", e.getMessage());
            return mav;
        }

        return new ModelAndView("redirect:" + ViewRouteHelper.PEDIDO_NEW);

    }

    @GetMapping("/hoy")
    public ModelAndView hoy (Principal principal){

        ModelAndView mav = new ModelAndView(ViewRouteHelper.PEDIDO_HOY);
        mav.addObject("username", principal.getName());

        try {
            List<PedidoResumenDTO> pedidos = pedidoService.traerPedidosDeHoy(principal.getName());
            mav.addObject("pedidos", pedidos);
            mav.addObject("totalDelDia", pedidos.stream().mapToDouble(PedidoResumenDTO::getTotal).sum());
        } catch (IllegalArgumentException e){
            mav.addObject("error", e.getMessage());
            mav.addObject("pedidos", List.of());
            mav.addObject("totalDelDia", 0.0);
        }

        return mav;

    }

}

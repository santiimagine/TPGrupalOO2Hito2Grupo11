package com.unla.epicentrogourmet.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.unla.epicentrogourmet.dtos.FestivalDTO;
import com.unla.epicentrogourmet.dtos.RegistroUnidadVentaDTO;
import com.unla.epicentrogourmet.entities.enums.TipoUnidad;
import com.unla.epicentrogourmet.helpers.ViewRouteHelper;
import com.unla.epicentrogourmet.services.IFestivalService;
import com.unla.epicentrogourmet.services.IUnidadVentaService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/unidadventa")
public class UnidadVentaController {
    private static final String REGISTRO_ATTR = "registro";

    private final IUnidadVentaService unidadVentaService;

    private final IFestivalService festivalService;

    private final ModelMapper modelMapper;

    public UnidadVentaController(IUnidadVentaService unidadVentaService, IFestivalService festivalService,
                                 ModelMapper modelMapper) {
        this.unidadVentaService = unidadVentaService;
        this.festivalService = festivalService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/new")
    public ModelAndView nuevo() {
        return formulario(new RegistroUnidadVentaDTO());
    }

    @PostMapping("/create")
    public ModelAndView create(@Valid @ModelAttribute(REGISTRO_ATTR) RegistroUnidadVentaDTO dto,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return formulario(dto);
        }
        try {
            unidadVentaService.registrar(dto);
        } catch (IllegalArgumentException e) {
            ModelAndView mAV = formulario(dto);
            mAV.addObject("error", e.getMessage());
            return mAV;
        }
        return new ModelAndView(new RedirectView(ViewRouteHelper.LOGIN_REGISTRO_OK));
    }

    private ModelAndView formulario(RegistroUnidadVentaDTO dto) {
        ModelAndView mAV = new ModelAndView(ViewRouteHelper.UNIDADVENTA_NEW);
        mAV.addObject(REGISTRO_ATTR, dto);
        mAV.addObject("festivales", festivalService.getVigentes()
                .stream()
                .map(festival -> modelMapper.map(festival, FestivalDTO.class))
                .toList());
        mAV.addObject("tipos", TipoUnidad.values());
        mAV.addObject("username", "anonymous");
        return mAV;
    }
}
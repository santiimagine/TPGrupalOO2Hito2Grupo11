package com.unla.epicentrogourmet.controllers.api.v1;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.unla.epicentrogourmet.dtos.ApiErrorDTO;

@RestControllerAdvice(basePackages = "com.unla.epicentrogourmet.controllers.api")
public class ApiExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorDTO> reglaDeNegocio(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(new ApiErrorDTO(400, e.getMessage(), List.of()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDTO> validacion(MethodArgumentNotValidException e) {
        List<String> detalles = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        return ResponseEntity.badRequest()
                .body(new ApiErrorDTO(400, "Hay datos inválidos en la solicitud", detalles));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorDTO> jsonInvalido(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest()
                .body(new ApiErrorDTO(400, "El cuerpo de la solicitud no es un JSON válido", List.of()));
    }
}

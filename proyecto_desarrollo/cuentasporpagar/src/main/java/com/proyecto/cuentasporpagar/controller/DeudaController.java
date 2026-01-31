package com.proyecto.cuentasporpagar.controller;

import com.proyecto.cuentasporpagar.modelo.Deuda;
import com.proyecto.cuentasporpagar.repository.DeudaRepository;
import com.proyecto.cuentasporpagar.repository.ProveedorRepository;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/deudas")
public class DeudaController {

    @Autowired
    private DeudaRepository deudaRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("deudas", deudaRepository.findAll());
        model.addAttribute("proveedores", proveedorRepository.findAll());
        return "lista-deudas";
    }

    @GetMapping("/nueva")
    public String formularioNueva(Model model) {
        Deuda deuda = new Deuda();
        deuda.setMonto_pagado(0.0);
        deuda.setEstado("Pendiente");
        model.addAttribute("deuda", deuda);
        model.addAttribute("proveedores", proveedorRepository.findAll());
        return "deudas/formulario-deuda";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Integer id, Model model) {
        Deuda deuda = deudaRepository.findById(id).orElseThrow();
        model.addAttribute("deuda", deuda);
        model.addAttribute("proveedores", proveedorRepository.findAll());
        return "deudas/formulario-deuda";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("deuda") Deuda deuda, BindingResult result, Model model) {
    
    // 1. REVISAR VALIDACIONES (Aquí cae el error de fecha pasada o montos inválidos)
    if (result.hasErrors()) {
        // Si hay errores, necesitamos recargar la lista de proveedores para el select
        model.addAttribute("proveedores", proveedorRepository.findAll());
        // IMPORTANTE: Retornamos la VISTA del formulario, no un redirect
        return "deudas/formulario-deuda"; 
    }

    // 2. LÓGICA DE NEGOCIO (Si todo está bien, procedemos)
    if (deuda.getId_deuda() == null) {
        deuda.setFecha_emision(LocalDateTime.now());
        deuda.setMonto_pagado(0.0);
        deuda.setEstado("Pendiente");
    } else {
        // Lógica de actualización de estados
        if (deuda.getMonto_pagado() >= deuda.getMonto_total()) {
            deuda.setEstado("Pagada");
        } else if (deuda.getMonto_pagado() > 0) {
            deuda.setEstado("Parcial");
        } else {
            deuda.setEstado("Pendiente");
        }
    }

    deudaRepository.save(deuda);
    return "redirect:/deudas";
    }
}
package com.proyecto.cuentasporpagar.controller;

import com.proyecto.cuentasporpagar.modelo.Proveedor;
import com.proyecto.cuentasporpagar.repository.ProveedorRepository;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorRepository proveedorRepository;

    // Listar todos los proveedores
    @GetMapping
    public String listarProveedores(Model model) {
        model.addAttribute("proveedores", proveedorRepository.findAll());
        return "proveedores/lista-proveedores";
    }

    // Mostrar formulario para nuevo proveedor
    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("proveedor", new Proveedor());
        return "proveedores/formulario-proveedor";
    }

    // Guardar (sirve para crear y para editar)
   @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("proveedor") Proveedor proveedor, 
                        BindingResult result, 
                        Model model) {
        
        // 1. Verificar si hay errores de validación (como el RUC corto o largo)
        if (result.hasErrors()) {
            // Si hay errores, no guarda y regresa al formulario
            // para que el usuario vea los mensajes en rojo
            return "proveedores/formulario-proveedor";
        }

        // 2. Si no hay errores, guarda (sirve para Crear y Editar)
        proveedorRepository.save(proveedor);
        
        // 3. Redirige a la lista
        return "redirect:/proveedores";
    }
    // Cargar datos para editar
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Integer id, Model model) {
        Proveedor proveedor = proveedorRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ID inválido"));
        model.addAttribute("proveedor", proveedor);
        return "proveedores/formulario-proveedor";
    }

    // Eliminar
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Integer id) {
        proveedorRepository.deleteById(id);
        return "redirect:/proveedores";
    }
}
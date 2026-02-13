package com.proyecto.cuentasporpagar.controller;

import com.proyecto.cuentasporpagar.modelo.Deuda;
import com.proyecto.cuentasporpagar.repository.DeudaRepository;
import com.proyecto.cuentasporpagar.repository.ProveedorRepository;

import jakarta.validation.Valid;
import com.proyecto.cuentasporpagar.service.DeudaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.servlet.http.HttpServletResponse; 
import java.io.IOException; 
import java.util.List; 
import com.proyecto.cuentasporpagar.service.ReporteDeudaService;

@Controller
@RequestMapping("/deudas")
public class DeudaController {

    @Autowired
    private DeudaRepository deudaRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired 
    private ReporteDeudaService reporteDeudaService;

    @Autowired
    private DeudaService deudaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("deudas", deudaService.listarDeudas());
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
    
    // 1. Validar fecha manualmente (esto reemplaza al @FutureOrPresent)
    if (deuda.getFecha_vencimiento() != null) {
        LocalDate hoy = LocalDate.now();
        LocalDate vencimiento = deuda.getFecha_vencimiento().toLocalDate();
        LocalDate limiteMinimo = hoy.plusDays(5);

        if (vencimiento.isBefore(hoy)) {
            result.rejectValue("fecha_vencimiento", "error.fecha", "La fecha no puede ser pasada.");
        } 
    }

    // 2. Si hay errores (como campos vacíos), volver al formulario
    if (result.hasErrors()) {
        model.addAttribute("proveedores", proveedorRepository.findAll());
        return "deudas/formulario-deuda"; 
    }

    // 3. Lógica de guardado
    try {
        if (deuda.getId_deuda() == null) {
            deuda.setFecha_emision(LocalDateTime.now());
            deuda.setMonto_pagado(0.0);
            deuda.setEstado("Pendiente");
        } else {
            // Actualizar estado basado en monto
            double total = deuda.getMonto_total();
            double pagado = (deuda.getMonto_pagado() != null) ? deuda.getMonto_pagado() : 0.0;
            
            if (pagado >= total) deuda.setEstado("Pagada");
            else if (pagado > 0) deuda.setEstado("Parcial");
            else deuda.setEstado("Pendiente");
        }

        deudaRepository.save(deuda);
        return "redirect:/deudas";
    } catch (Exception e) {
        model.addAttribute("proveedores", proveedorRepository.findAll());
        return "deudas/formulario-deuda";
        }
    }
    // exportar pdf
    @GetMapping("/exportar-pdf")
    public void exportarDeudas(
        @RequestParam(value = "proveedor", required = false) String proveedor,
        @RequestParam(value = "mes", required = false) String mes,
        HttpServletResponse response) throws IOException {
    
    response.setContentType("application/pdf");
    response.setHeader("Content-Disposition", "attachment; filename=Reporte_Deudas.pdf");

    // Traemos todas primero
    List<Deuda> deudas = deudaRepository.findAll();

    String titulo = "Reporte General de Deudas ";

    // Filtramos manualmente la lista antes de mandarla al PDF
    if (proveedor != null && !proveedor.isEmpty()) {
        deudas = deudas.stream()
                .filter(d -> d.getProveedor().getNombre().equalsIgnoreCase(proveedor))
                .toList();
        titulo = "Reporte de Deudas: " + proveedor;
    }
    
    if (mes != null && !mes.isEmpty()) {
        deudas = deudas.stream()
                .filter(d -> d.getFecha_vencimiento() != null && d.getFecha_vencimiento().toString().startsWith(mes))
                .toList();
        String[] partes = mes.split("-"); // Separa el año del mes
        String anio = partes[0];
        int numMes = Integer.parseInt(partes[1]);
        String[] nombresMeses = {"", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", 
                                 "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        
        String nombreMesBonito = nombresMeses[numMes];
        if (proveedor != null && !proveedor.isEmpty()) {
            titulo = "Reporte de " + proveedor + " - " + nombreMesBonito + " " + anio;
        } else {
            titulo = "Deudas que vencen en " + nombreMesBonito + " " + anio;
        }
    }

    reporteDeudaService.exportarDeudasPDF(deudas, titulo, response);
    }
}
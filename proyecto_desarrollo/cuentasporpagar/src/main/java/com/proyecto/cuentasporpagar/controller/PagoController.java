package com.proyecto.cuentasporpagar.controller;

import com.proyecto.cuentasporpagar.modelo.Deuda;
import com.proyecto.cuentasporpagar.modelo.Pago;
import com.proyecto.cuentasporpagar.modelo.Detalle_pago;
import com.proyecto.cuentasporpagar.repository.DeudaRepository;
import com.proyecto.cuentasporpagar.repository.PagoRepository;
import com.proyecto.cuentasporpagar.repository.Detalle_pagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/pagos")
public class PagoController {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private DeudaRepository deudaRepository; 

    @Autowired
    private Detalle_pagoRepository detalle_pagoRepository; 

    @GetMapping("/nuevo/{id}")
    public String formularioPago(@PathVariable("id") Integer id, Model model) {
        Deuda deuda = deudaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deuda no encontrada"));
        
        Double pagado = (deuda.getMonto_pagado() != null) ? deuda.getMonto_pagado() : 0.0;
        Double saldoPendiente = deuda.getMonto_total() - pagado;
        
        model.addAttribute("deuda", deuda);
        model.addAttribute("pago", new Pago());
        model.addAttribute("saldoPendiente", saldoPendiente);
        
        return "registrar-pago";
    }

    @PostMapping("/guardar")
    public String guardarPago(@ModelAttribute Pago pago, @RequestParam("descripcion") String descripcion) {
    
    
    // Si el monto es nulo, menor o igual a cero, rechazamos la operación
    if (pago.getMonto_pago() == null || pago.getMonto_pago() <= 0) {
        return "redirect:/pagos/nuevo/" + pago.getDeuda().getId_deuda() + "?error=monto_invalido";
    }
    

    // Buscamos la deuda
    Deuda deuda = deudaRepository.findById(pago.getDeuda().getId_deuda())
            .orElseThrow(() -> new RuntimeException("Deuda no encontrada"));

    // Validación extra: No permitir pagar más de lo que se debe
    Double pagadoActual = (deuda.getMonto_pagado() != null) ? deuda.getMonto_pagado() : 0.0;
    Double saldoPendiente = deuda.getMonto_total() - pagadoActual;
    
    if (pago.getMonto_pago() > saldoPendiente) {
        return "redirect:/pagos/nuevo/" + deuda.getId_deuda() + "?error=excede_saldo";
    }

    // Actualizamos montos de la deuda
    Double nuevoTotalPagado = pagadoActual + pago.getMonto_pago();
    deuda.setMonto_pagado(nuevoTotalPagado);
    deuda.setEstado(nuevoTotalPagado >= deuda.getMonto_total() ? "Pagada" : "Parcial");
    deudaRepository.save(deuda);

    // Guardamos el Pago
    pago.setFecha_pago(LocalDateTime.now());
    Pago pagoGuardado = pagoRepository.save(pago);

    // Guardamos el Detalle del Pago
    Detalle_pago detalle = new Detalle_pago();
    detalle.setPago(pagoGuardado);
    detalle.setDeuda(deuda);
    detalle.setDescripcion(descripcion);
    detalle_pagoRepository.save(detalle); 

    return "redirect:/deudas";
    }
   
    @GetMapping("/historial/{id}")
    public String verHistorial(@PathVariable("id") Integer id, Model model) {
    Deuda deuda = deudaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Deuda no encontrada"));
    
    // Usamos el nuevo nombre del método definido con @Query
    model.addAttribute("detalles", detalle_pagoRepository.buscarPorDeudaId(id));
    model.addAttribute("deuda", deuda);
    
    return "historial-pagos";
    }
}
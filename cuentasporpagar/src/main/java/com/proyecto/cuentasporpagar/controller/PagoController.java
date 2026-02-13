package com.proyecto.cuentasporpagar.controller;

import com.proyecto.cuentasporpagar.modelo.Deuda;
import com.proyecto.cuentasporpagar.modelo.Pago;
import com.proyecto.cuentasporpagar.modelo.Detalle_pago;
import com.proyecto.cuentasporpagar.repository.DeudaRepository;
import com.proyecto.cuentasporpagar.repository.PagoRepository;
import com.proyecto.cuentasporpagar.service.ReporteHistorialService;
import com.proyecto.cuentasporpagar.repository.Detalle_pagoRepository;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import java.security.Principal; // IMPORTANTE: Para obtener el usuario
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
    
    @Autowired
    private ReporteHistorialService reporteHistorialService;

    @GetMapping("/nuevo/{id}")
    public String formularioPago(@PathVariable("id") Integer id, Model model) {
        Deuda deuda = deudaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deuda no encontrada"));
        
        Double pagado = (deuda.getMonto_pagado() != null) ? deuda.getMonto_pagado() : 0.0;
        Double saldoPendiente = deuda.getMonto_total() - pagado;
        
        long cantidadPagosAnteriores = detalle_pagoRepository.buscarPorDeudaId(deuda.getId_deuda()).size();
        
        model.addAttribute("deuda", deuda);
        model.addAttribute("pago", new Pago());
        model.addAttribute("saldoPendiente", saldoPendiente);
        model.addAttribute("descripcionSugerida", "Cuota " + (cantidadPagosAnteriores + 1));
        
        return "registrar-pago";
    }

    @PostMapping("/guardar")
    public String guardarPago(@Valid @ModelAttribute("pago") Pago pago, 
                             BindingResult result, 
                             @RequestParam("cuotaAutomatica") String cuota, 
                             @RequestParam("notasAdicionales") String notas, 
                             Principal principal, // AGREGADO: Para capturar la sesión
                             Model model) {

        // 1. Validar errores de anotaciones
        if (result.hasErrors()) {
            Deuda deuda = deudaRepository.findById(pago.getDeuda().getId_deuda()).get();
            Double pagado = (deuda.getMonto_pagado() != null) ? deuda.getMonto_pagado() : 0.0;
            
            model.addAttribute("deuda", deuda);
            model.addAttribute("saldoPendiente", deuda.getMonto_total() - pagado);
            model.addAttribute("descripcionSugerida", cuota);
            return "registrar-pago";
        }

        // 2. Buscamos la deuda
        Deuda deuda = deudaRepository.findById(pago.getDeuda().getId_deuda())
                .orElseThrow(() -> new RuntimeException("Deuda no encontrada"));

        Double pagadoActual = (deuda.getMonto_pagado() != null) ? deuda.getMonto_pagado() : 0.0;
        Double saldoPendiente = deuda.getMonto_total() - pagadoActual;

        // Validación: No permitir pagar más de lo que se debe
        if (pago.getMonto_pago() > saldoPendiente) {
            model.addAttribute("error", "El monto ingresado excede el saldo pendiente ($" + saldoPendiente + ")");
            model.addAttribute("deuda", deuda);
            model.addAttribute("saldoPendiente", saldoPendiente);
            model.addAttribute("descripcionSugerida", cuota);
            return "registrar-pago";
        }

        // 3. Proceso de Guardado
        Double nuevoTotalPagado = pagadoActual + pago.getMonto_pago();
        deuda.setMonto_pagado(nuevoTotalPagado);
        deuda.setEstado(nuevoTotalPagado >= deuda.getMonto_total() ? "Pagada" : "Parcial");
        deudaRepository.save(deuda);

        pago.setFecha_pago(LocalDateTime.now());
        Pago pagoGuardado = pagoRepository.save(pago);

        // --- LÓGICA DEL USUARIO LOGUEADO ---
        String nombreUsuario = (principal != null) ? principal.getName() : "Desconocido";
        String descripcionFinal = cuota;
        
        if (notas != null && !notas.trim().isEmpty()) {
            descripcionFinal += " (" + notas.trim() + ")";
        }
        
        // Añadimos la firma de quién hizo el registro
        descripcionFinal += " | Reg. por: " + nombreUsuario;

        // 4. Guardamos el Detalle del Pago
        Detalle_pago detalle = new Detalle_pago();
        detalle.setPago(pagoGuardado);
        detalle.setDeuda(deuda);
        detalle.setDescripcion(descripcionFinal); 
        detalle_pagoRepository.save(detalle); 

        return "redirect:/deudas";
    }
   
    @GetMapping("/historial/{id}")
    public String verHistorial(@PathVariable("id") Integer id, Model model) {
        Deuda deuda = deudaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deuda no encontrada"));
        
        model.addAttribute("detalles", detalle_pagoRepository.buscarPorDeudaId(id));
        model.addAttribute("deuda", deuda);
        
        return "historial-pagos";
    }
    @GetMapping("/historial/{id}/pdf")
    public void descargarHistorialPdf(@PathVariable("id") Integer id, HttpServletResponse response) throws Exception {
    response.setContentType("application/pdf");
    String cabecera = "Content-Disposition";
    String valor = "attachment; filename=Historial_Deuda_" + id + ".pdf";
    response.setHeader(cabecera, valor);

    Deuda deuda = deudaRepository.findById(id).get();
    List<Detalle_pago> detalles = detalle_pagoRepository.buscarPorDeudaId(id);

    reporteHistorialService.exportar(response, deuda, detalles);
    }
}
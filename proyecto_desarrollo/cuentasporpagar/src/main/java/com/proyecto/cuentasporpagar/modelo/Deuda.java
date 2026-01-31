package com.proyecto.cuentasporpagar.modelo;


import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Data;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Data
public class Deuda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_deuda;
    

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") 
    private LocalDateTime fecha_emision;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @FutureOrPresent(message = "La fecha de vencimiento no puede ser una fecha pasada")
    private LocalDateTime fecha_vencimiento;
    private Double monto_total;
    private String estado;
    private Double monto_pagado = 0.0;

    

    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;
    
    public Double getSaldoPendiente() {
    return this.monto_total - this.monto_pagado;
    }
}

package com.proyecto.cuentasporpagar.modelo;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_pago;

    private LocalDateTime fecha_pago;
    private String metodo_pago;
    private Double monto_pago;
    private String referencia_bancaria;

    @ManyToOne
    @JoinColumn(name = "id_deuda")
    private Deuda deuda;

    @ManyToOne
    @JoinColumn(name = "id_empleado")
    private Empleado empleado;
}
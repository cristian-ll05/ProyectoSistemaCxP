package com.proyecto.cuentasporpagar.modelo;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Detalle_pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_detalle;

    @ManyToOne
    @JoinColumn(name = "id_pago")
    private Pago pago;

    @ManyToOne
    @JoinColumn(name = "id_deuda")
    private Deuda deuda;

    private String descripcion;
}
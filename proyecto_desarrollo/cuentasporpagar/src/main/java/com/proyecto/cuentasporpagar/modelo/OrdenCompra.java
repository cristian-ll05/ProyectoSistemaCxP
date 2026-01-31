

package com.proyecto.cuentasporpagar.modelo;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class OrdenCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_OC;

    private Integer id_cotizacion;
    private LocalDateTime fecha_emision;
    private String estado_OC;
    private Double monto_total_OC;
    private Integer cantidad_items;

    @ManyToOne // Un proveedor tiene muchas órdenes de compra
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;
}
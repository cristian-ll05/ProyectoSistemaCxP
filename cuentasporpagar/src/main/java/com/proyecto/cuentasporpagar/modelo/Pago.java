package com.proyecto.cuentasporpagar.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto a pagar debe ser mayor a cero")
    @Max(value = 1000000, message = "El monto excede el límite permitido")
    private Double monto_pago;

    // VALIDACIÓN PARA REFERENCIA BANCARIA:
    @NotBlank(message = "La referencia bancaria es obligatoria")
    @Pattern(regexp = "^[0-9]+$", message = "La referencia debe ser numeros positivos")
    private String referencia_bancaria;

    @ManyToOne
    @JoinColumn(name = "id_deuda")
    private Deuda deuda;

    /*@ManyToOne
    @JoinColumn(name = "id_empleado")
    private Empleado empleado;*/
}
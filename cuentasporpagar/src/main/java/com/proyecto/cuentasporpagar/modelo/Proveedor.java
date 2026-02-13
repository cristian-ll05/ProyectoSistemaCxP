package com.proyecto.cuentasporpagar.modelo;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.Size;


@Entity
@Data
public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_proveedor; 

    private String nombre;
    @Column(length = 13)
    @Size(min = 10, max = 13, message = "La identificación debe tener entre 10 y 13 dígitos")
    private String ruc;
    private Integer celular;
    private String direccion;
    private String correo;
}
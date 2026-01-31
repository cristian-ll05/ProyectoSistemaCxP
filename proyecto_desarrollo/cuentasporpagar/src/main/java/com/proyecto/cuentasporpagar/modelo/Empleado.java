package com.proyecto.cuentasporpagar.modelo;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_empleado;

    private Integer celular;
    private String direccion;
    private String correo;
}
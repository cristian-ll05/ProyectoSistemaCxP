package com.proyecto.cuentasporpagar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.cuentasporpagar.modelo.Empleado;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
}
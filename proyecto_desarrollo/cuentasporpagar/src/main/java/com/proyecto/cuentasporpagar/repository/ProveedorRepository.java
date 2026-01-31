package com.proyecto.cuentasporpagar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.cuentasporpagar.modelo.Proveedor;

public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {
}
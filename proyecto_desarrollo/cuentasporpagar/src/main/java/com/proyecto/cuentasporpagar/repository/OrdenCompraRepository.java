package com.proyecto.cuentasporpagar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.cuentasporpagar.modelo.OrdenCompra;

public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Integer> {
}
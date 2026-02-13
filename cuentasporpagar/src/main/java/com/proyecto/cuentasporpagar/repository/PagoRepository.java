package com.proyecto.cuentasporpagar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.cuentasporpagar.modelo.Pago;

public interface PagoRepository extends JpaRepository<Pago, Integer> {
}
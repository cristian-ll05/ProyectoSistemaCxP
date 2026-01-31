package com.proyecto.cuentasporpagar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.cuentasporpagar.modelo.Deuda;

public interface DeudaRepository extends JpaRepository<Deuda, Integer> {
}
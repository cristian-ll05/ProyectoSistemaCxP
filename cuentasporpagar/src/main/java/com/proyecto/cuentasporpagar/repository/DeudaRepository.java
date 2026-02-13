package com.proyecto.cuentasporpagar.repository;

import com.proyecto.cuentasporpagar.modelo.Deuda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DeudaRepository extends JpaRepository<Deuda, Integer> {

    // Usamos nativeQuery = true para que use el nombre real de la columna en la tabla
    @Query(value = "SELECT * FROM deuda ORDER BY fecha_vencimiento ASC", nativeQuery = true)
    List<Deuda> listarDeudasOrdenadas();
}
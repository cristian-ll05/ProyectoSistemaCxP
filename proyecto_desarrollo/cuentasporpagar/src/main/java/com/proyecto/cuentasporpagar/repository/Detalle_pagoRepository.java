package com.proyecto.cuentasporpagar.repository;

import com.proyecto.cuentasporpagar.modelo.Detalle_pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface Detalle_pagoRepository extends JpaRepository<Detalle_pago, Integer> {
    
    // Usamos una consulta manual para evitar el conflicto con los guiones bajos
    @Query("SELECT d FROM Detalle_pago d WHERE d.deuda.id_deuda = :idDeuda")
    List<Detalle_pago> buscarPorDeudaId(@Param("idDeuda") Integer idDeuda);
}
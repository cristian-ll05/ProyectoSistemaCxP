package com.proyecto.cuentasporpagar.repository;

import com.proyecto.cuentasporpagar.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    // Esto nos servirá para buscar al usuario por su nombre al hacer login
    Optional<Usuario> findByUsername(String username);
}